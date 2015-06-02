package com.budjb.asynchronoustasks

import com.budjb.asynchronoustasks.exception.PersistentAsynchronousTaskLoadException
import com.budjb.asynchronoustasks.exception.PersistentAsynchronousTaskNotFoundException
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

/**
 * An implementation of an asynchronous task that is backed by a database.
 */
abstract class PersistentAsynchronousTask extends AbstractAsynchronousTask {
    /**
     * Task domain associated with the given task.
     */
    protected AsynchronousTaskDomain task

    /**
     * Creates a brand new task instance.
     */
    PersistentAsynchronousTask() {
        task = new AsynchronousTaskDomain()
        task.name = getTaskName()
        save()
    }

    /**
     * Saves the task to the database.
     */
    protected void save() {
        task.save(flush: true, failOnError: true)
    }

    /**
     * Loads an existing task.
     *
     * @param taskId
     */
    PersistentAsynchronousTask(int taskId) {
        // Load the task
        try {
            task = AsynchronousTaskDomain.read(taskId)
        }
        catch (Exception e) {
            throw new PersistentAsynchronousTaskLoadException("Unable to load task with ID '$taskId'", e)
        }

        // Ensure the task was found
        if (!task) {
            throw new PersistentAsynchronousTaskNotFoundException("Task with ID '$taskId' was not found")
        }
    }

    /**
     * Returns the task's ID.
     *
     * @return
     */
    @Override
    int getTaskId() {
        return task.id
    }

    /**
     * Returns the task's progress.
     *
     * @return
     */
    @Override
    int getProgress() {
        return task.progress
    }

    /**
     * Returns the description of the current step in the task.
     *
     * @return
     */
    @Override
    String getDescription() {
        return task.description
    }

    /**
     * Returns the results associated with a task that has ended.
     *
     * @return
     */
    @Override
    Object getResults() {
        return unserialize(task.results)
    }

    /**
     * Gets the current state of the task.
     *
     * @return
     */
    @Override
    AsynchronousTaskState getState() {
        return task.state
    }

    /**
     * Gets the time when the task was created.
     *
     * @return
     */
    @Override
    Date getCreatedTime() {
        return task.dateCreated
    }

    /**
     * Gets the time when the task was last updated.
     *
     * @return
     */
    @Override
    Date getUpdatedTime() {
        return task.lastUpdated
    }

    /**
     * Gets the time when the task was started.
     *
     * @return
     */
    @Override
    Date getStartTime() {
        return task.startTime
    }

    /**
     * Gets the time when the task was ended.
     *
     * @return
     */
    @Override
    Date getEndTime() {
        return task.endTime
    }

    /**
     * Gets the error code associated with a failed task.
     */
    @Override
    String getErrorCode() {
        return task.errorCode
    }

    /**
     * Marks a task as started.
     */
    @Override
    protected void onStart() {
        task.state = AsynchronousTaskState.RUNNING
        task.startTime = new Date()
        task.progress = 0
        save()
    }

    /**
     * Updates the progress of the task.
     *
     * @param progress Task's percentage complete.
     */
    @Override
    protected void update(int progress) {
        update(progress, task.description)
    }

    /**
     * Updates the progress of the task.
     *
     * @param progress Task's percentage complete.
     * @param description Description of the current step in the overall process of the task.
     */
    @Override
    protected void update(int progress, String description) {
        task.progress = progress
        task.description = description
        save()
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
        // Set the progress to 100% for successfully completed tasks.
        task.progress = 100

        // Complete the task.
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
        task.errorCode = errorCode
        task.state = state
        task.results = serialize(results)
        task.endTime = new Date()
        save()
    }

    /**
     * Converts the results associated with the task to a string.
     *
     * @param results
     * @return
     */
    private String serialize(Object results) {
        // Nothing to do if the object is null
        if (results == null) {
            return null
        }

        // Nothing to do if it's already a string
        if (results instanceof String) {
            return results
        }

        // Check for JSON conversion
        if (results instanceof List || results instanceof Map) {
            return new JsonBuilder(results).toString()
        }

        // Return the string representation of the object
        return results.toString()
    }

    /**
     * Un-marshalls results stored in the database.
     *
     * @param results
     * @return
     */
    private Object unserialize(String results) {
        // Nothing to do if the object is null
        if (results == null) {
            return null
        }

        // Attempt to convert from JSON
        try {
            return new JsonSlurper().parseText(results)
        }
        catch (Exception e) {
            // Continue
        }

        return results
    }

    /**
     * Returns the internal task state data.
     *
     * @return
     */
    protected Object getInternalTaskData() {
        return unserialize(task.internalTaskData)
    }

    /**
     * Sets the internal task state data.
     *
     * @param data
     */
    protected void setInternalTaskData(Object data) {
        task.internalTaskData = serialize(data)
    }
}
