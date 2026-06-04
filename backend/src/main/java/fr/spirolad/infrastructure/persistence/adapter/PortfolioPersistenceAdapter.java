package fr.spirolad.infrastructure.persistence.adapter;

import fr.spirolad.application.port.outbound.PortfolioPersistencePort;
import fr.spirolad.domain.model.PortfolioProfile;
import fr.spirolad.infrastructure.persistence.database.PortfolioEntity;
import fr.spirolad.infrastructure.persistence.mapper.PortfolioPersistenceMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.Optional;

@ApplicationScoped
public class PortfolioPersistenceAdapter implements PortfolioPersistencePort {

    private final PortfolioPersistenceMapper portfolioPersistenceMapper;
    private final EntityManager entityManager;

    @Inject
    public PortfolioPersistenceAdapter(PortfolioPersistenceMapper portfolioPersistenceMapper, EntityManager entityManager) {
        this.portfolioPersistenceMapper = portfolioPersistenceMapper;
        this.entityManager = entityManager;
    }

    @Override
    public Optional<PortfolioProfile> findSingleton() {
        return PortfolioEntity.findAll().firstResultOptional()
                .map(PortfolioEntity.class::cast)
                .map(portfolioPersistenceMapper::toDomain);
    }

    @Override
    @Transactional
    public PortfolioProfile save(PortfolioProfile portfolioProfile) {
        // The profile is a singleton so we only update the first one
        PortfolioEntity existing = PortfolioEntity.findById(1L);
        PortfolioEntity entityToUpdate = portfolioPersistenceMapper.toEntity(portfolioProfile);
        existing.setName(entityToUpdate.getName());
        existing.setEmail(entityToUpdate.getEmail());
        existing.setCurrentPosition(entityToUpdate.getCurrentPosition());
        existing.setPhoto(entityToUpdate.getPhoto());

        return portfolioPersistenceMapper.toDomain(existing);
    }
}