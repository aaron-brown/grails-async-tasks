package com.budjb.asynchronoustasks

/**
 * A PersistentReadOnlyTask is a generic class that uses the values from the domain
 * representing a task and makes them available for reporting without having to know
 * what kind of task the domain represents.  This is useful for APIs that poll an
 * existing task.
 */
class PersistentReadOnlyTask extends PersistentAsynchronousTask {
    /**
     * Disable the base constructor.
     */
    public PersistentReadOnlyTask() {
        throw new IllegalStateException("can not create a new read-only task")
    }

    /**
     * Loads a task from the database.
     *
     * @param taskId ID of the task in the database.
     */
    public PersistentReadOnlyTask(int taskId) {
        super(taskId)
    }

    /**
     * Disable task modification.
     */
    @Override
    protected void onStart() {
        throw new IllegalStateException("can not onStart a read-only task")
    }

    /**
     * Disable task modification.
     */
    @Override
    protected void update(int progress) {
        throw new IllegalStateException("can not update a read-only task")
    }

    /**
     * Disable task modification.
     */
    @Override
    protected void update(int progress, String description) {
        throw new IllegalStateException("can not update a read-only task")
    }

    /**
     * Disable task modification
     */
    @Override
    protected void update(int progress, String currentOperation, Object results) {
        throw new IllegalStateException("can not update a read-only task")
    }

    /**
     * Disable task modification.
     */
    @Override
    protected void error(String errorCode) {
        throw new IllegalStateException("can not trigger an error on a read-only task")
    }

    /**
     * Disable task modification.
     */
    @Override
    protected void error(String errorCode, Object results) {
        throw new IllegalStateException("can not trigger an error on a read-only task")
    }

    /**
     * Disable task modification.
     */
    @Override
    protected void failure(String errorCode) {
        throw new IllegalStateException("can not trigger a failure a read-only task")
    }

    /**
     * Disable task modification.
     */
    @Override
    protected void failure(String errorCode, Object results) {
        throw new IllegalStateException("can not trigger a failure a read-only task")
    }

    /**
     * Disable task modification.
     */
    @Override
    protected void complete() {
        throw new IllegalStateException("can not complete a read-only task")
    }

    /**
     * Disable task modification.
     */
    @Override
    protected void complete(Object results) {
        throw new IllegalStateException("can not complete a read-only task")
    }

    /**
     * Disable task modification.
     */
    @Override
    public void run() {
        throw new IllegalStateException("can not run a read-only task")
    }

    /**
     * Disable task modification.
     */
    @Override
    protected void process() {
        throw new IllegalStateException("can not process a read-only task")
    }

    /**
     * Disable task modification.
     */
    @Override
    protected void save() {
        throw new IllegalStateException("can not save a read-only task")
    }
}
