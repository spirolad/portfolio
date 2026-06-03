package fr.spirolad.infrastructure.rest.mapper;

import fr.spirolad.domain.model.Project;
import fr.spirolad.dto.ProjectResponse;
import fr.spirolad.dto.ProjectUploadRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.net.URI;
import java.util.Base64;
import java.util.List;

@Mapper(componentModel = "cdi")
public interface ProjectRestMapper {

    @Mapping(target = "id", ignore = true)
    Project toDomain(ProjectUploadRequest request);

    ProjectResponse toResponse(Project project);

    default String map(URI value) {
        return value != null ? value.toString() : null;
    }

    default URI map(String value) {
        return value != null ? URI.create(value) : null;
    }

}