package fr.spirolad.infrastructure.persistence.adapter;

import fr.spirolad.application.port.outbound.CategoryPersistencePort;
import fr.spirolad.domain.model.Category;
import fr.spirolad.infrastructure.persistence.database.CategoryEntity;
import fr.spirolad.infrastructure.persistence.mapper.CategoryPersistenceMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CategoryPersistenceAdapter implements CategoryPersistencePort {
    private final CategoryPersistenceMapper mapper;
    private final EntityManager em;

    @Inject
    public CategoryPersistenceAdapter(CategoryPersistenceMapper mapper, EntityManager em) {
        this.mapper = mapper;
        this.em = em;
    }

    @Override
    public List<Category> findAll() {
        List<CategoryEntity> entities = CategoryEntity.listAll();
        return entities.stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Category> findById(Long categoryId) {
        return CategoryEntity.findByIdOptional(categoryId)
                .map(entity -> mapper.toDomain((CategoryEntity) entity));
    }

    @Override
    @Transactional
    public Category save(Category category) {
        CategoryEntity entity = mapper.toEntity(category);
        if (entity.id == null) {
            entity.persist();
            return mapper.toDomain(entity);
        }
        CategoryEntity mergedEntity = em.merge(entity);
        return mapper.toDomain(mergedEntity);
    }

    @Override
    @Transactional
    public Category update(Category category) {
        CategoryEntity entity = mapper.toEntity(category);
        CategoryEntity merged = em.merge(entity);
        return mapper.toDomain(merged);
    }

    @Override
    @Transactional
    public void deleteById(Long categoryId) {
        CategoryEntity.deleteById(categoryId);
    }
}
