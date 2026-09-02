package com.sunrise.dentalclinic.exception;

public class BillNotFoundException extends RuntimeException {
    public BillNotFoundException() {
        super();
    }

    public BillNotFoundException(String message) {
        super(message);
    }

    public BillNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
