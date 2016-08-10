package com.budjb.asynchronoustasks

public enum AsynchronousTaskState {
    /**
     * Task has not yet started.
     */
    NOT_RUNNING,

    /**
     * Task is currently running.
     */
    RUNNING,

    /**
     * An error in the process occurred.
     *
     * An error can be considered an issue that occurred not because of a problem
     * in the application's code, but rather in business logic contained either
     * within the application or in another system.
     */
    ERROR,

    /**
     * A hard failure occurred.
     *
     * A failure can be considered an issue that occurred in the application code,
     * such as an unhandled exception.
     */
    FAILURE,

    /**
     * Task completed successfully.
     */
    COMPLETED

    /**
     * Returns the enum string lower cased.
     *
     * @return
     */
    public String toString() {
        return name().toLowerCase()
    }
}
