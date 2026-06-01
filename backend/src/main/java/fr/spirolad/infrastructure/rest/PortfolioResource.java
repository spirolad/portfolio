package fr.spirolad.infrastructure.rest;

import fr.spirolad.api.PortfolioApi;
import fr.spirolad.application.port.inbound.PortfolioUseCase;
import fr.spirolad.domain.model.Portfolio;
import fr.spirolad.domain.model.PortfolioProfile;
import fr.spirolad.dto.PortfolioRequest;
import fr.spirolad.infrastructure.rest.mapper.PortfolioRestMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
@Path("/portfolio")
public class PortfolioResource implements PortfolioApi {

    private final PortfolioUseCase portfolioUseCase;
    private final PortfolioRestMapper portfolioRestMapper;

    public PortfolioResource(PortfolioUseCase portfolioUseCase, PortfolioRestMapper portfolioRestMapper) {
        this.portfolioUseCase = portfolioUseCase;
        this.portfolioRestMapper = portfolioRestMapper;
    }

    @Override
    public Response getPortfolio() {
        Portfolio portfolio = portfolioUseCase.getPortfolio();
        return Response.ok(portfolioRestMapper.toResponse(portfolio)).build();
    }

    @Override
    public Response updatePortfolio(PortfolioRequest portfolioRequest) {
        Portfolio portfolio = portfolioUseCase.updatePortfolio(portfolioRestMapper.toDomain(portfolioRequest));
        return Response.ok(portfolioRestMapper.toResponse(portfolio)).build();
    }
}