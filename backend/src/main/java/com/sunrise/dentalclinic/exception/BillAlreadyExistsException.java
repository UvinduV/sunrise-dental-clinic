package com.sunrise.dentalclinic.exception;

public class BillAlreadyExistsException extends RuntimeException {
    public BillAlreadyExistsException() {
        super();
    }

    public BillAlreadyExistsException(String message) {
        super(message);
    }

    public BillAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
