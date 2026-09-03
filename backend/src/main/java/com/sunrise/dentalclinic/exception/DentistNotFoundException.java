package com.sunrise.dentalclinic.exception;

public class DentistNotFoundException extends RuntimeException {
    public DentistNotFoundException() {
        super();
    }

    public DentistNotFoundException(String message) {
        super(message);
    }

    public DentistNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
