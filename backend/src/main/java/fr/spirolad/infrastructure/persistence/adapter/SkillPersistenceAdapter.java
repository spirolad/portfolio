package fr.spirolad.infrastructure.persistence.adapter;

import fr.spirolad.application.port.outbound.SkillPersistencePort;
import fr.spirolad.domain.model.Skill;
import fr.spirolad.infrastructure.persistence.database.SkillEntity;
import fr.spirolad.infrastructure.persistence.mapper.SkillPersistenceMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class SkillPersistenceAdapter implements SkillPersistencePort {
    private final SkillPersistenceMapper mapper;
    private final EntityManager em;

    @Inject
    public SkillPersistenceAdapter(SkillPersistenceMapper mapper, EntityManager em) {
        this.mapper = mapper;
        this.em = em;
    }

    @Override
    public List<Skill> findAll() {
        List<SkillEntity> entities = SkillEntity.<SkillEntity>listAll();
        return entities.stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Skill> findById(Long skillId) {
        return SkillEntity.findByIdOptional(skillId)
                .map(entity -> mapper.toDomain((SkillEntity) entity));
    }

    @Override
    @Transactional
    public Skill save(Skill skill) {
        SkillEntity entity = mapper.toEntity(skill);
        if (entity.id == null) {
            entity.persist();
            return mapper.toDomain(entity);
        }
        SkillEntity mergedEntity = em.merge(entity);
        return mapper.toDomain(mergedEntity);
    }

    @Override
    @Transactional
    public Skill update(Skill skill) {
        SkillEntity entity = mapper.toEntity(skill);
        SkillEntity merged = em.merge(entity);
        return mapper.toDomain(merged);
    }

    @Override
    @Transactional
    public void deleteById(Long skillId) {
        SkillEntity.deleteById(skillId);
    }
}
