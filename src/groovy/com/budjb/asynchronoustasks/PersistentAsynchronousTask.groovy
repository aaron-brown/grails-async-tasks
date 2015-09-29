package com.budjb.asynchronoustasks

import com.budjb.asynchronoustasks.exception.PersistentAsynchronousTaskLoadException
import com.budjb.asynchronoustasks.exception.PersistentAsynchronousTaskNotFoundException
import grails.validation.ValidationException
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import org.apache.log4j.Logger

/**
 * An implementation of an asynchronous task that is backed by a database.
 */
abstract class PersistentAsynchronousTask extends AbstractAsynchronousTask {
    /**
     * Logger.
     */
    Logger log = Logger.getLogger(PersistentAsynchronousTask)

    /**
     * Creates a brand new task instance.
     */
    PersistentAsynchronousTask() {
        withSession {
            AsynchronousTaskDomain domain = new AsynchronousTaskDomain()

            domain.name = getTaskName()
            domain.description = getDescription()

            if (!domain.validate()) {
                throw new ValidationException("can not create a domain instance for task ${getTaskName()} due to validation errors", domain.errors)
            }

            domain.save(flush: true, failOnError: true)

            taskId = domain.id

            createdTime = domain.dateCreated
            updatedTime = domain.lastUpdated
        }
    }

    /**
     * Saves the task to the database.
     */
    void save() {
        withSession {
            AsynchronousTaskDomain domain = AsynchronousTaskDomain.get(taskId)

            domain.name = getTaskName()
            domain.description = getDescription()

            domain.dateCreated = getCreatedTime()
            domain.lastUpdated = getUpdatedTime()
            domain.startTime = getStartTime()
            domain.endTime = getEndTime()

            domain.errorCode = getErrorCode()
            domain.progress = getProgress()
            domain.currentOperation = getCurrentOperation()
            domain.state = getState()

            domain.internalTaskData = serialize(getInternalTaskData())
            domain.results = serialize(getResults())

            if (!domain.validate()) {
                throw new ValidationException("task ${getTaskName()} with ID ${domain.id} does not validate", domain.errors)
            }

            domain.save(flush: true, failOnError: true)
        }
    }

    /**
     * Does a batch of processes before saving the changes to database at the end.
     *
     * @param c
     */
    void save(Closure c) {
        c.delegate = this
        c.resolveStrategy = Closure.DELEGATE_FIRST
        c.call()

        save()
    }

    /**
     * Loads an existing task.
     *
     * @param taskId
     */
    PersistentAsynchronousTask(int taskId) {
        try {
            withSession {
                AsynchronousTaskDomain domain = AsynchronousTaskDomain.read(taskId)

                if (!domain) {
                    throw new PersistentAsynchronousTaskNotFoundException("task with ID $taskId was not found")
                }

                this.taskId = taskId

                createdTime = domain.dateCreated
                updatedTime = domain.lastUpdated
                startTime = domain.startTime
                endTime = domain.endTime

                errorCode = domain.errorCode
                progress = domain.progress
                currentOperation = domain.currentOperation
                state = domain.state

                internalTaskData = unserialize(domain.internalTaskData)
                results = unserialize(domain.results)
            }
        }
        catch (PersistentAsynchronousTaskNotFoundException e) {
            throw e
        }
        catch (Exception e) {
            throw new PersistentAsynchronousTaskLoadException("Unable to load task with ID '$taskId'", e)
        }
    }

    /**
     * Marks a task as started.
     */
    @Override
    protected void onStart() {
        state = AsynchronousTaskState.RUNNING
        startTime = new Date()
        progress = 0
    }

    /**
     * Updates the progress of the task.
     *
     * @param progress Task's percentage complete.
     */
    @Override
    protected void update(int progress) {
        update(progress, currentOperation, results)
    }

    /**
     * Updates the progress of the task.
     *
     * @param progress Task's percentage complete.
     * @param currentOperation Description of the current operation the task is performing.
     */
    @Override
    protected void update(int progress, String currentOperation) {
        update(progress, currentOperation, results)
    }

    /**
     * Updates the progress of the task.
     *
     * @param progress Task's percentage complete.
     * @param currentOperation Description of the current operation the task is performing.
     * @param results Results to store with the task.
     */
    @Override
    protected void update(int progress, String currentOperation, Object results) {
        this.progress = progress
        this.currentOperation = currentOperation
        this.results = results
    }

    /**
     * Sets the task in an error state.\
     *
     * @param errorCode Error code associated with a failed task.
     */
    @Override
    protected void error(String errorCode) {
        error(errorCode, null)
    }

    /**
     * Sets the task in an error state.
     *
     * @param errorCode Error code associated with a failed task.
     * @param results
     */
    @Override
    protected void error(String errorCode, Object results) {
        completeTask(AsynchronousTaskState.ERROR, errorCode, results)
    }

    /**
     * Sets the task in a failure state.
     *
     * @param errorCode Error code associated with a failed task.
     */
    @Override
    protected void failure(String errorCode) {
        failure(errorCode, null)
    }

    /**
     * Sets the task in a failure state.
     *
     * @param errorCode Error code associated with a failed task.
     * @param results
     */
    @Override
    protected void failure(String errorCode, Object results) {
        completeTask(AsynchronousTaskState.FAILURE, errorCode, results)
    }

    /**
     * Completes the task.
     */
    @Override
    protected void complete() {
        complete(null)
    }

    /**
     * Completes the task.
     *
     * @param results
     */
    @Override
    protected void complete(Object results) {
        progress = 100

        completeTask(AsynchronousTaskState.COMPLETED, null, results)
    }

    /**
     * Completes the task with the given state and results.
     *
     * @param state End state of the task.
     */
    private void completeTask(AsynchronousTaskState state) {
        completeTask(state, null, null)
    }

    /**
     * Completes the task with the given state and results.
     *
     * @param state End state of the task.
     * @param errorCode Error code associated with a failed task.
     * @param results Data associated with the completion of the task.
     */
    private void completeTask(AsynchronousTaskState state, String errorCode, Object results) {
        this.errorCode = errorCode
        this.state = state
        this.results = results
        this.endTime = new Date()
    }

    /**
     * Converts the results associated with the task to a string.
     *
     * @param results
     * @return
     */
    private String serialize(Object results) {
        if (results == null) {
            return null
        }

        if (results instanceof String) {
            return results
        }

        if (results instanceof List || results instanceof Map) {
            return new JsonBuilder(results).toString()
        }

        return results.toString()
    }

    /**
     * Un-marshalls results stored in the database.
     *
     * @param results
     * @return
     */
    private Object unserialize(String results) {
        if (results == null) {
            return null
        }

        try {
            return new JsonSlurper().parseText(results)
        }
        catch (Exception e) {
            // Continue
        }

        return results
    }

    /**
     * Runs the given closure containing database operations with a new Hibernate session.
     *
     * @param c
     */
    void withSession(Closure c) {
        AsynchronousTaskDomain.withNewSession c
    }
}
