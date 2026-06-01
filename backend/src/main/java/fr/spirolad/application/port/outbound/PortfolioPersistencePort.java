package fr.spirolad.application.port.outbound;

import fr.spirolad.domain.model.PortfolioProfile;

import java.util.Optional;

public interface PortfolioPersistencePort {

    Optional<PortfolioProfile> findSingleton();

    PortfolioProfile save(PortfolioProfile portfolioProfile);
}