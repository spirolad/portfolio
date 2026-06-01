package fr.spirolad.application.usecase;

import fr.spirolad.application.port.inbound.PortfolioUseCase;
import fr.spirolad.application.port.outbound.EducationPersistencePort;
import fr.spirolad.application.port.outbound.ExperiencePersistencePort;
import fr.spirolad.application.port.outbound.PortfolioPersistencePort;
import fr.spirolad.application.port.outbound.ProjectPersistencePort;
import fr.spirolad.application.port.outbound.SkillPersistencePort;
import fr.spirolad.domain.model.Portfolio;
import fr.spirolad.domain.model.PortfolioProfile;

public class PortfolioUseCaseImpl implements PortfolioUseCase {

    private final PortfolioPersistencePort portfolioPersistencePort;
    private final EducationPersistencePort educationPersistencePort;
    private final ExperiencePersistencePort experiencePersistencePort;
    private final ProjectPersistencePort projectPersistencePort;
    private final SkillPersistencePort skillPersistencePort;

    public PortfolioUseCaseImpl(PortfolioPersistencePort portfolioPersistencePort,
                                 EducationPersistencePort educationPersistencePort,
                                 ExperiencePersistencePort experiencePersistencePort,
                                 ProjectPersistencePort projectPersistencePort,
                                 SkillPersistencePort skillPersistencePort) {
        this.portfolioPersistencePort = portfolioPersistencePort;
        this.educationPersistencePort = educationPersistencePort;
        this.experiencePersistencePort = experiencePersistencePort;
        this.projectPersistencePort = projectPersistencePort;
        this.skillPersistencePort = skillPersistencePort;
    }

    @Override
    public Portfolio getPortfolio() {
        return new Portfolio(
                portfolioPersistencePort.findSingleton().orElseGet(() -> new PortfolioProfile(null, null, null, null, null)),
                educationPersistencePort.findAll(),
                experiencePersistencePort.findAll(),
                projectPersistencePort.findAll(),
                skillPersistencePort.findAll()
        );
    }

    @Override
    public Portfolio updatePortfolio(PortfolioProfile portfolioProfile) {
        portfolioPersistencePort.save(portfolioProfile);
        return getPortfolio();
    }
}