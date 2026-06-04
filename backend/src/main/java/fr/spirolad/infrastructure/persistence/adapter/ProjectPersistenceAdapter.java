package fr.spirolad.infrastructure.persistence.adapter;

import fr.spirolad.application.port.outbound.ProjectPersistencePort;
import fr.spirolad.domain.model.Project;
import fr.spirolad.infrastructure.persistence.database.ProjectEntity;
import fr.spirolad.infrastructure.persistence.mapper.ProjectPersistenceMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ProjectPersistenceAdapter implements ProjectPersistencePort {

    private final ProjectPersistenceMapper projectPersistenceMapper;
    private final EntityManager entityManager;

    @Inject
    public ProjectPersistenceAdapter(ProjectPersistenceMapper projectPersistenceMapper, EntityManager entityManager) {
        this.projectPersistenceMapper = projectPersistenceMapper;
        this.entityManager = entityManager;
    }

    @Override
    public List<Project> findAll() {
        return ProjectEntity.listAll().stream()
                .map(ProjectEntity.class::cast)
                .map(projectPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional
    public Project save(Project project) {
        ProjectEntity entity = projectPersistenceMapper.toEntity(project);
        if (entity.getId() == null) {
            entity.persist();
            return projectPersistenceMapper.toDomain(entity);
        }

        ProjectEntity mergedEntity = entityManager.merge(entity);
        return projectPersistenceMapper.toDomain(mergedEntity);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        ProjectEntity.deleteById(id);
    }

    @Override
    public Optional<Project> findById(Long id) {
        ProjectEntity entity = ProjectEntity.findById(id);
        if (entity == null) {
            return Optional.empty();
        }
        return Optional.of(projectPersistenceMapper.toDomain(entity));
    }
}