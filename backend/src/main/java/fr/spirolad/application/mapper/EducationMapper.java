package fr.spirolad.application.mapper;

import fr.spirolad.domain.model.Education;
import fr.spirolad.dto.EducationRequest;
import fr.spirolad.dto.EducationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi")
public interface EducationMapper {

    EducationResponse toDto(Education education);

    @Mapping(target = "id", ignore = true)
    Education toDomain(EducationRequest educationRequest);

}
