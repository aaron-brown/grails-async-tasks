package com.budjb.asynchronoustasks.exception

class PersistentAsynchronousTaskLoadException extends Exception {
    public PersistentAsynchronousTaskLoadException() {
        super()
    }

    public PersistentAsynchronousTaskLoadException(String message) {
        super(message)
    }

    public PersistentAsynchronousTaskLoadException(String message, Throwable cause) {
        super(message, cause)
    }
}
