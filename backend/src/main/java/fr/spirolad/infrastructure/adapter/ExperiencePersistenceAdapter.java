package fr.spirolad.infrastructure.adapter;

import fr.spirolad.domain.model.Experience;
import fr.spirolad.domain.port.ExperiencePersistencePort;
import fr.spirolad.infrastructure.database.ExperienceEntity;
import fr.spirolad.infrastructure.mapper.ExperiencePersistenceMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ExperiencePersistenceAdapter implements ExperiencePersistencePort {

    private final ExperiencePersistenceMapper experiencePersistenceMapper;

    @Inject
    public ExperiencePersistenceAdapter(ExperiencePersistenceMapper experiencePersistenceMapper) {
        this.experiencePersistenceMapper = experiencePersistenceMapper;
    }

    @Override
    public List<Experience> findAll() {
        return ExperienceEntity.listAll()
                .stream()
                .map(e -> (ExperienceEntity) e)
                .map(experiencePersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Experience save(Experience experience) {
        ExperienceEntity entity = experiencePersistenceMapper.toEntity(experience);
        if (entity.id == null) {
            entity.persist();
            return experiencePersistenceMapper.toDomain(entity);
        }

        ExperienceEntity managedEntity = ExperienceEntity.findById(entity.id);
        if (managedEntity == null) {
            entity.persist();
            return experiencePersistenceMapper.toDomain(entity);
        }

        managedEntity.company = entity.company;
        managedEntity.position = entity.position;
        managedEntity.mission = entity.mission;
        managedEntity.startDate = entity.startDate;
        managedEntity.endDate = entity.endDate;
        return experiencePersistenceMapper.toDomain(managedEntity);
    }

    @Override
    public void deleteById(Long id) {
        ExperienceEntity.deleteById(id);
    }

    @Override
    public Optional<Experience> findById(Long id) {
        ExperienceEntity entity = ExperienceEntity.findById(id);
        if (entity == null) return Optional.empty();
        return Optional.of(experiencePersistenceMapper.toDomain(entity));
    }
}
