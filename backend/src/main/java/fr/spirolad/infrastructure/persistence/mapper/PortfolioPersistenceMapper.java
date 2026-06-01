package fr.spirolad.infrastructure.persistence.mapper;

import fr.spirolad.domain.model.PortfolioProfile;
import fr.spirolad.infrastructure.persistence.database.PortfolioEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface PortfolioPersistenceMapper {

    PortfolioProfile toDomain(PortfolioEntity portfolioEntity);

    PortfolioEntity toEntity(PortfolioProfile portfolioProfile);
}