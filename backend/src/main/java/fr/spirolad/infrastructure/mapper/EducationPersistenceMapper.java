package fr.spirolad.infrastructure.mapper;

import fr.spirolad.domain.model.Education;
import fr.spirolad.infrastructure.database.EducationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface EducationPersistenceMapper {

	Education toDomain(EducationEntity educationEntity);

	EducationEntity toEntity(Education education);
}