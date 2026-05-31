package fr.spirolad.application.mapper;

import fr.spirolad.domain.model.Experience;
import fr.spirolad.dto.ExperienceRequest;
import fr.spirolad.dto.ExperienceResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi")
public interface ExperienceMapper {

    ExperienceResponse toDto(Experience experience);

    @Mapping(target = "id", ignore = true)
    Experience toDomain(ExperienceRequest request);

}
