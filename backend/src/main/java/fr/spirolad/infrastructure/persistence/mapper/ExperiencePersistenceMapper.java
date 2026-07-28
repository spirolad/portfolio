package fr.spirolad.infrastructure.persistence.mapper;

import fr.spirolad.domain.model.Experience;
import fr.spirolad.infrastructure.persistence.database.ExperienceEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "jakarta-cdi")
public interface ExperiencePersistenceMapper {

    Experience toDomain(ExperienceEntity entity);

    ExperienceEntity toEntity(Experience experience);
}