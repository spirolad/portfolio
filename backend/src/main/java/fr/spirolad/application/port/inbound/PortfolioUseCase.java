package fr.spirolad.application.port.inbound;

import fr.spirolad.domain.model.Portfolio;
import fr.spirolad.domain.model.PortfolioProfile;

public interface PortfolioUseCase {

    Portfolio getPortfolio();

    Portfolio updatePortfolio(PortfolioProfile portfolioProfile);
}