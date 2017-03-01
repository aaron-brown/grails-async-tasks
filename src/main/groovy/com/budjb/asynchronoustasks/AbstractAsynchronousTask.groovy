package com.budjb.asynchronoustasks

import org.apache.log4j.Logger

abstract class AbstractAsynchronousTask implements AsynchronousTask {
    /**
     * Logger.
     */
    Logger log = Logger.getLogger(getClass())

    /**
     * ID of the task.
     */
    int taskId

    /**
     * Name of the task.
     */
    String taskName

    /**
     * Description of the task.
     */
    String description

    /**
     * Progress of the task as the percentage complete.
     */
    int progress

    /**
     * Running state of the task.
     */
    AsynchronousTaskState state = AsynchronousTaskState.NOT_RUNNING

    /**
     * Current operation of the task.
     */
    String currentOperation

    /**
     * Error code of the task.
     */
    String errorCode

    /**
     * Resolution code of the task.
     */
    String resolutionCode

    /**
     * Results of the task.
     */
    Object results

    /**
     * Internal task data.
     */
    Object internalTaskData

    /**
     * Date the task was created.
     */
    Date createdTime

    /**
     * Date the task was started.
     */
    Date startTime

    /**
     * Date the task was updated.
     */
    Date updatedTime

    /**
     * Date the task completed.
     */
    Date endTime

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
     * @param currentOperation Description of the current operation the task is performing.
     */
    abstract protected void update(int progress, String currentOperation)

    /**
     * Updates the progress of the task.
     *
     * @param progress Task's percentage complete.
     * @param currentOperation Description of the current operation the task is performing.
     * @param results Results to update the task with.
     */
    abstract protected void update(int progress, String currentOperation, Object results)

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
     * @param currentOperation Description of the current operation the task is performing.
     */
    abstract protected void complete(String currentOperation)

    /**
     * Completes the task.
     *
     * @param results
     */
    abstract protected void complete(Object results)

    /**
     * Completes the task.
     *
     * @param currentOperation Description of the current operation the task is performing.
     * @param results
     */
    abstract protected void complete(String currentOperation, Object results)

    /**
     * Does the actual work of the task.
     */
    abstract protected void process()

    /**
     * Marks the task as started and starts actual processing.
     */
    @Override
    void run() {
        if (getState() != AsynchronousTaskState.NOT_RUNNING) {
            throw new IllegalStateException("can not start task ${getTaskId()} because it has already started")
        }

        try {
            onStart()

            process()

            onSuccess()
        }
        catch (Exception e) {
            log.error("unhandled exception caught while running task '${getTaskName()}' with task ID ${getTaskId()}", e)
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
            'taskId'          : getTaskId(),
            'name'            : getTaskName(),
            'progress'        : getProgress(),
            'state'           : getState().toString(),
            'currentOperation': getCurrentOperation(),
            'errorCode'       : getErrorCode(),
            'resolutionCode'  : getResolutionCode(),
            'description'     : getDescription(),
            'results'         : getResults(),
            'createdTime'     : getCreatedTime(),
            'startTime'       : getStartTime(),
            'updatedTime'     : getUpdatedTime(),
            'endTime'         : getEndTime()
        ]
    }
}
