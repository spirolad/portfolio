package fr.spirolad.infrastructure.rest.mapper;

import fr.spirolad.domain.model.Portfolio;
import fr.spirolad.domain.model.PortfolioProfile;
import fr.spirolad.dto.PortfolioProfileResponse;
import fr.spirolad.dto.PortfolioRequest;
import fr.spirolad.dto.PortfolioResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi", uses = {EducationRestMapper.class, ExperienceRestMapper.class, ProjectRestMapper.class, SkillRestMapper.class})
public interface PortfolioRestMapper {

    PortfolioProfile toDomain(PortfolioRequest request);

    PortfolioProfileResponse toResponse(PortfolioProfile profile);

    PortfolioResponse toResponse(Portfolio portfolio);
}