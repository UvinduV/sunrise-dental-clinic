package com.sunrise.dentalclinic.exception;

public class DoubleBookingException extends RuntimeException {
    public DoubleBookingException() {
        super();
    }

    public DoubleBookingException(String message) {
        super(message);
    }

    public DoubleBookingException(String message, Throwable cause) {
        super(message, cause);
    }
}
