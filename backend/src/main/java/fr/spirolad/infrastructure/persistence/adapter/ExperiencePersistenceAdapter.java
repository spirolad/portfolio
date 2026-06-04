package fr.spirolad.infrastructure.persistence.adapter;

import fr.spirolad.application.port.outbound.ExperiencePersistencePort;
import fr.spirolad.domain.model.Experience;
import fr.spirolad.infrastructure.persistence.database.ExperienceEntity;
import fr.spirolad.infrastructure.persistence.mapper.ExperiencePersistenceMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ExperiencePersistenceAdapter implements ExperiencePersistencePort {

    private final ExperiencePersistenceMapper experiencePersistenceMapper;
    private final EntityManager entityManager;

    @Inject
    public ExperiencePersistenceAdapter(ExperiencePersistenceMapper experiencePersistenceMapper,
                                        EntityManager entityManager) {
        this.experiencePersistenceMapper = experiencePersistenceMapper;
        this.entityManager = entityManager;
    }

    @Override
    public List<Experience> findAll() {
        return ExperienceEntity.listAll()
                .stream()
                .map(ExperienceEntity.class::cast)
                .map(experiencePersistenceMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional
    public Experience save(Experience experience) {
        ExperienceEntity entity = experiencePersistenceMapper.toEntity(experience);
        if (entity.getId() == null) {
            entity.persist();
            return experiencePersistenceMapper.toDomain(entity);
        }

        ExperienceEntity mergedEntity = entityManager.merge(entity);
        return experiencePersistenceMapper.toDomain(mergedEntity);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        ExperienceEntity.deleteById(id);
    }

    @Override
    public Optional<Experience> findById(Long id) {
        ExperienceEntity entity = ExperienceEntity.findById(id);
        if (entity == null) {
            return Optional.empty();
        }
        return Optional.of(experiencePersistenceMapper.toDomain(entity));
    }
}