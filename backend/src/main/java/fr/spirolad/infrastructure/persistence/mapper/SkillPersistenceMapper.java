package fr.spirolad.infrastructure.persistence.mapper;

import fr.spirolad.domain.model.Skill;
import fr.spirolad.infrastructure.persistence.database.SkillEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi", uses = {CategoryPersistenceMapper.class})
public interface SkillPersistenceMapper {

    Skill toDomain(SkillEntity entity);

    SkillEntity toEntity(Skill domain);
}
