package com.sunrise.dentalclinic.exception;

public class PatientNotFoundException extends RuntimeException {
    public PatientNotFoundException() {
        super();
    }

    public PatientNotFoundException(String message) {
        super(message);
    }

    public PatientNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
