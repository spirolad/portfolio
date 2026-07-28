package fr.spirolad.infrastructure.rest.mapper;

import fr.spirolad.application.command.SkillCommand;
import fr.spirolad.domain.model.Category;
import fr.spirolad.domain.model.Skill;
import fr.spirolad.dto.SkillRequest;
import fr.spirolad.dto.SkillResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * REST Mapper for Skill entities.
 * Handles conversion between REST DTOs and domain models.
 * Note: The categoryId mapping must be handled by the Resource layer
 * because the mapper needs access to CategoryUseCase to fetch the Category object.
 */
@Mapper(componentModel = "jakarta-cdi", uses = {CategoryRestMapper.class})
public interface SkillRestMapper {
    
    /**
     * Convert SkillRequest DTO to Skill domain model.
     * WARNING: The category must be set externally by the Resource layer
     * using CategoryUseCase to fetch the Category by categoryId.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    Skill toDomain(SkillRequest dto);

    @Mapping(source = "categoryId", target = "categoryId")
    SkillCommand toCommand(SkillRequest dto);

    /**
     * Convert Skill domain model to SkillResponse DTO.
     * Maps the complete category object to the response.
     */
    SkillResponse toResponse(Skill domain);
}
