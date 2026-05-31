package fr.spirolad.application.usecase;

import fr.spirolad.application.port.inbound.SkillUseCase;
import fr.spirolad.application.port.outbound.SkillPersistencePort;
import fr.spirolad.domain.exception.SkillNotFoundException;
import fr.spirolad.domain.model.Skill;
import java.util.List;

public class SkillUseCaseImpl implements SkillUseCase {
    private final SkillPersistencePort skillPersistencePort;

    public SkillUseCaseImpl(SkillPersistencePort skillPersistencePort) {
        this.skillPersistencePort = skillPersistencePort;
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
    public Skill saveSkill(Skill skill) {
        return skillPersistencePort.save(skill);
    }

    @Override
    public Skill updateSkill(Long skillId, Skill skill) {
        Skill existingSkill = skillPersistencePort.findById(skillId)
                .orElseThrow(() -> new SkillNotFoundException(skillId));
        existingSkill.setName(skill.getName());
        existingSkill.setCategory(skill.getCategory());
        return skillPersistencePort.update(existingSkill);
    }

    @Override
    public void deleteSkill(Long skillId) {
        skillPersistencePort.deleteById(skillId);
    }
}
