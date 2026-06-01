package fr.spirolad.infrastructure.persistence.mapper;

import fr.spirolad.domain.model.Project;
import fr.spirolad.infrastructure.persistence.database.ProjectEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface ProjectPersistenceMapper {

    Project toDomain(ProjectEntity projectEntity);

    ProjectEntity toEntity(Project project);
}