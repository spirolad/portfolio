package fr.spirolad.application.port.inbound;

import fr.spirolad.application.command.SkillCommand;
import fr.spirolad.domain.model.Skill;
import java.util.List;

public interface SkillUseCase {
    List<Skill> getAllSkills();
    Skill getSkill(Long skillId);
    Skill saveSkill(SkillCommand command);
    Skill updateSkill(Long skillId, SkillCommand command);
    void deleteSkill(Long skillId);
}
