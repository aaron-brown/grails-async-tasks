package com.budjb.asynchronoustasks

import org.apache.log4j.Logger

abstract class AbstractAsynchronousTask implements AsynchronousTask {
    /**
     * Logger.
     */
    protected Logger log = Logger.getLogger(getClass())

    /**
     * Called before a task starts.
     */
    protected void onStart() {

    }

    /**
     * Called when a task completes successfully.
     */
    protected void onSuccess() {

    }

    /**
     * Called when a task completes unsuccessfully.
     *
     * @param e
     */
    protected void onError(Throwable e) {

    }

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
     * Marks the task as started and starts actual processing.
     */
    @Override
    void run() {
        try {
            onStart()
            process()
            onSuccess()
        }
        catch (Throwable e) {
            log.error("Unhandled exception caught while running task '${getTaskName()}'", e)
            failure("unhandledException", "unhandled exception '${e.getClass().toString()}' caught while running task")
            onError(e)
        }
    }

    /**
     * Creates a map of all of the properties of the task.
     *
     * @return
     */
    @Override
    Map toMap() {
        return [
            'taskId'     : this.taskId,
            'name'       : this.taskName,
            'progress'   : this.progress,
            'state'      : this.state.toString(),
            'errorCode'  : this.errorCode,
            'description': this.description,
            'results'    : this.results,
            'createdTime': this.createdTime,
            'startTime'  : this.startTime,
            'updatedTime': this.updatedTime,
            'endTime'    : this.endTime
        ]
    }
}
