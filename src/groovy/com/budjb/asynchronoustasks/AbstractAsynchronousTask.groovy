package com.budjb.asynchronoustasks

import groovy.json.JsonBuilder
import org.apache.log4j.Logger

abstract class AbstractAsynchronousTask implements AsynchronousTask {
    /**
     * Logger.
     */
    Logger log = Logger.getLogger(getClass())

    /**
     * Starts a task.
     */
    abstract protected void start()

    /**
     * Updates the progress of the task.
     *
     * @param progress Task's percentage complete.
     */
    abstract protected void update(int progress)

    /**
     * Updates the progress of the task.
     *
     * @param progress Task's percentage complete.
     * @param description Description of the current step in the overall process of the task.
     */
    abstract protected void update(int progress, String description)

    /**
     * Sets the task in an error state.
     *
     * @param errorCode Error code associated with a failed task.
     */
    abstract protected void error(String errorCode)

    /**
     * Sets the task in an error state.
     *
     * @param errorCode Error code associated with a failed task.
     * @param results
     */
    abstract protected void error(String errorCode, Object results)

    /**
     * Sets the task in a failure state.
     *
     * @param errorCode Error code associated with a failed task.
     */
    abstract protected void failure(String errorCode)

    /**
     * Sets the task in a failure state.
     *
     * @param errorCode Error code associated with a failed task.
     * @param results
     */
    abstract protected void failure(String errorCode, Object results)

    /**
     * Completes the task.
     */
    abstract protected void complete()

    /**
     * Completes the task.
     *
     * @param results
     */
    abstract protected void complete(Object results)

    /**
     * Does the actual work of the task.
     */
    abstract protected void process()

    /**
     * Returns the task's name.
     *
     * @return
     */
    public abstract String getTaskName()

    /**
     * Marks the task as started and starts actual processing.
     */
    @Override
    public void fire() {
        try {
            start()
            process()
        }
        catch (Exception e) {
            log.error("Unhandled exception caught while running task '${getTaskName()}'", e)
            failure("unhandledException", "unhandled exception '${e.getClass().toString()}' caught while running task")
        }
    }

    /**
     * Creates a map of all of the properties of the task.
     */
    @Override
    public Map toMap() {
        return [
            'taskId': this.taskId,
            'name': this.taskName,
            'progress': this.progress,
            'state': this.state.toString(),
            'description': this.description,
            'results': this.results,
            'createdTime': this.createdTime,
            'startTime': this.startTime,
            'updatedTime': this.updatedTime,
            'endTime': this.endTime
        ]
    }

    /**
     * Creates a JSON string of all of the properties of the task.
     */
    @Override
    public String toJson() {
        return new JsonBuilder(toMap()).toString()
    }
}
