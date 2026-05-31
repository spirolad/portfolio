package fr.spirolad.domain.exception;

public class EducationNotFoundException extends RuntimeException {

    public EducationNotFoundException(String message) {
        super(message);
    }
}