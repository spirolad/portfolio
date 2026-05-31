package fr.spirolad.infrastructure.persistence.mapper;

import fr.spirolad.domain.model.Education;
import fr.spirolad.infrastructure.persistence.database.EducationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface EducationPersistenceMapper {

    Education toDomain(EducationEntity educationEntity);

    EducationEntity toEntity(Education education);
}