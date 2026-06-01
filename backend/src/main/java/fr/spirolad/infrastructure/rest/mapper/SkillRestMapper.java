package fr.spirolad.infrastructure.rest.mapper;

import fr.spirolad.domain.model.Skill;
import fr.spirolad.dto.SkillRequest;
import fr.spirolad.dto.SkillResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi", uses = {CategoryRestMapper.class})
public interface SkillRestMapper {
    @Mapping(target = "id", ignore = true)
    Skill toDomain(SkillRequest dto);

    SkillResponse toResponse(Skill domain);
}
