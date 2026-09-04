package com.sunrise.dentalclinic.exception;

public class PatientHasAppointmentsException extends RuntimeException {
    public PatientHasAppointmentsException() {
        super();
    }

    public PatientHasAppointmentsException(String message) {
        super(message);
    }

    public PatientHasAppointmentsException(String message, Throwable cause) {
        super(message, cause);
    }
}
