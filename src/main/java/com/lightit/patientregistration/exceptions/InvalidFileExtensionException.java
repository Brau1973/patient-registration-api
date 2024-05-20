package com.lightit.patientregistration.exceptions;

public class InvalidFileExtensionException extends RuntimeException {
    public InvalidFileExtensionException(String message) {
        super(message);
    }
}