package fr.spirolad.domain.exception;

public class SkillNotFoundException extends RuntimeException {
    public SkillNotFoundException(String message) {
        super(message);
    }

    public SkillNotFoundException(Long skillId) {
        super("Skill not found: " + skillId);
    }
}
