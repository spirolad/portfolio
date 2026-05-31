package fr.spirolad.infrastructure.mapper;

import fr.spirolad.domain.model.Experience;
import fr.spirolad.infrastructure.database.ExperienceEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface ExperiencePersistenceMapper {

    Experience toDomain(ExperienceEntity entity);

    ExperienceEntity toEntity(Experience experience);
}
