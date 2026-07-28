package fr.spirolad.application.usecase;

import fr.spirolad.application.command.SkillCommand;
import fr.spirolad.application.port.inbound.SkillUseCase;
import fr.spirolad.application.port.outbound.CategoryPersistencePort;
import fr.spirolad.application.port.outbound.SkillPersistencePort;
import fr.spirolad.domain.exception.CategoryNotFoundException;
import fr.spirolad.domain.exception.SkillInvalideException;
import fr.spirolad.domain.exception.SkillNotFoundException;
import fr.spirolad.domain.model.Category;
import fr.spirolad.domain.model.Skill;
import java.util.List;

public class SkillUseCaseImpl implements SkillUseCase {
    private final SkillPersistencePort skillPersistencePort;
    private final CategoryPersistencePort categoryPersistencePort;

    public SkillUseCaseImpl(SkillPersistencePort skillPersistencePort, CategoryPersistencePort categoryPersistencePort) {
        this.skillPersistencePort = skillPersistencePort;
        this.categoryPersistencePort = categoryPersistencePort;
    }

    @Override
    public List<Skill> getAllSkills() {
        return skillPersistencePort.findAll();
    }

    @Override
    public Skill getSkill(Long skillId) {
        return skillPersistencePort.findById(skillId)
                .orElseThrow(() -> new SkillNotFoundException(skillId));
    }

    @Override
    public Skill saveSkill(SkillCommand command) {
        return skillPersistencePort.save(toDomain(command));
    }

    @Override
    public Skill updateSkill(Long skillId, SkillCommand command) {
        Skill existingSkill = skillPersistencePort.findById(skillId)
                .orElseThrow(() -> new SkillNotFoundException(skillId));
        Skill updatedSkill = toDomain(command);
        existingSkill.setName(updatedSkill.getName());
        existingSkill.setCategory(updatedSkill.getCategory());
        return skillPersistencePort.update(existingSkill);
    }

    @Override
    public void deleteSkill(Long skillId) {
        skillPersistencePort.deleteById(skillId);
    }

    private Skill toDomain(SkillCommand command) {
        try {
            Category category = categoryPersistencePort.findById(command.categoryId())
                    .orElseThrow(() -> new CategoryNotFoundException(command.categoryId()));
            return new Skill(null, command.name(), category);
        } catch (CategoryNotFoundException exception) {
            throw new SkillInvalideException(exception.getMessage());
        }
    }
}
