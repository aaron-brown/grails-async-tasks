package com.budjb.asynchronoustasks.exception

class PersistentAsynchronousTaskNotFoundException extends Exception {
    public PersistentAsynchronousTaskNotFoundException() {
        super()
    }

    public PersistentAsynchronousTaskNotFoundException(String message) {
        super(message)
    }

    public PersistentAsynchronousTaskNotFoundException(String message, Throwable cause) {
        super(message, cause)
    }
}
