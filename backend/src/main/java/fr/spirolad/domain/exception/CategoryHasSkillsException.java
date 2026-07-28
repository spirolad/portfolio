package fr.spirolad.domain.exception;

public class CategoryHasSkillsException extends RuntimeException {
    public CategoryHasSkillsException(String message) {
        super(message);
    }

    public CategoryHasSkillsException(Long categoryId) {
        super("Cannot delete category " + categoryId + " because it has associated skills. Please delete the skills first.");
    }
}

