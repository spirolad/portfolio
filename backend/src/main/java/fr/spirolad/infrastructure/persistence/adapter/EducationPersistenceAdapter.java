package fr.spirolad.infrastructure.persistence.adapter;

import fr.spirolad.application.port.outbound.EducationPersistencePort;
import fr.spirolad.domain.model.Education;
import fr.spirolad.infrastructure.persistence.database.EducationEntity;
import fr.spirolad.infrastructure.persistence.mapper.EducationPersistenceMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class EducationPersistenceAdapter implements EducationPersistencePort {

    private final EducationPersistenceMapper educationPersistenceMapper;
    private final EntityManager entityManager;

    @Inject
    public EducationPersistenceAdapter(EducationPersistenceMapper educationPersistenceMapper,
                                       EntityManager entityManager) {
        this.educationPersistenceMapper = educationPersistenceMapper;
        this.entityManager = entityManager;
    }

    @Override
    public List<Education> findAll() {
        return EducationEntity.listAll()
                .stream()
                .map(EducationEntity.class::cast)
                .map(educationPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional
    public Education save(Education education) {
        EducationEntity entity = educationPersistenceMapper.toEntity(education);
        if (entity.id == null) {
            entity.persist();
            return educationPersistenceMapper.toDomain(entity);
        }

        EducationEntity mergedEntity = entityManager.merge(entity);
        return educationPersistenceMapper.toDomain(mergedEntity);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        EducationEntity.deleteById(id);
    }

    @Override
    public Optional<Education> findById(Long id) {
        EducationEntity entity = EducationEntity.findById(id);
        if (entity == null) {
            return Optional.empty();
        }
        return Optional.of(educationPersistenceMapper.toDomain(entity));
    }
}