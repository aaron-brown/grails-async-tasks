package com.budjb.asynchronoustasks

interface AsynchronousTask {
    /**
     * Returns the task ID of the task.
     *
     * @return
     */
    int getTaskId()

    /**
     * Returns the task name.
     */
    String getTaskName()

    /**
     * Gets the current progress of the task.
     *
     * @return
     */
    int getProgress()

    /**
     * Gets the description of the current step of the task.
     */
    String getDescription()

    /**
     * Gets the results associated with a task that has ended.
     *
     * @return
     */
    Object getResults()

    /**
     * Gets the current state of the task.
     *
     * @return
     */
    AsynchronousTaskState getState()

    /**
     * Gets the time when the task was created.
     *
     * @return
     */
    Date getCreatedTime()

    /**
     * Gets the time when the task was last updated.
     *
     * @return
     */
    Date getUpdatedTime()

    /**
     * Gets the time when the task was started.
     *
     * @return
     */
    Date getStartTime()

    /**
     * Gets the time when the task was ended.
     *
     * @return
     */
    Date getEndTime()

    /**
     * Runs the task.
     */
    void run()

    /**
     * Gets the error code of a failed task.
     */
    String getErrorCode()

    /**
     * Creates a map of all of the properties of the task.
     */
    Map toMap()
}
