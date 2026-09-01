package com.sunrise.dentalclinic.exception;

public class TreatmentTypeNotFoundException extends RuntimeException {
    public TreatmentTypeNotFoundException() {
        super();
    }

    public TreatmentTypeNotFoundException(String message) {
        super(message);
    }

    public TreatmentTypeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
