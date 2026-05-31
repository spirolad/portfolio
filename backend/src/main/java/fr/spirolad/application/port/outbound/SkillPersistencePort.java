package fr.spirolad.application.port.outbound;

import fr.spirolad.domain.model.Skill;
import java.util.List;
import java.util.Optional;

public interface SkillPersistencePort {
    List<Skill> findAll();
    Optional<Skill> findById(Long skillId);
    Skill save(Skill skill);
    Skill update(Skill skill);
    void deleteById(Long skillId);
}
