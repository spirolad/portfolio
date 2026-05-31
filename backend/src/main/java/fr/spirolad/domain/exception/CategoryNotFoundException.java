package fr.spirolad.domain.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(String message) {
        super(message);
    }

    public CategoryNotFoundException(Long categoryId) {
        super("Category not found: " + categoryId);
    }
}
