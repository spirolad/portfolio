package fr.spirolad.application.port.inbound;

import fr.spirolad.domain.model.Skill;
import java.util.List;

public interface SkillUseCase {
    List<Skill> getAllSkills();
    Skill getSkill(Long skillId);
    Skill saveSkill(Skill skill);
    Skill updateSkill(Long skillId, Skill skill);
    void deleteSkill(Long skillId);
}
