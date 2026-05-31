package fr.spirolad.infrastructure.adapter;

import fr.spirolad.domain.model.Education;
import fr.spirolad.domain.port.EducationPersistencePort;
import fr.spirolad.infrastructure.database.EducationEntity;
import fr.spirolad.infrastructure.mapper.EducationPersistenceMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class EducationPersistenceAdapter implements EducationPersistencePort {

	private final EducationPersistenceMapper educationPersistenceMapper;

	public EducationPersistenceAdapter(EducationPersistenceMapper educationPersistenceMapper) {
		this.educationPersistenceMapper = educationPersistenceMapper;
	}

	@Override
	public List<Education> findAll() {
		return EducationEntity.<EducationEntity>listAll()
			.stream()
			.map(educationPersistenceMapper::toDomain)
			.toList();
	}

	@Override
	public Education save(Education education) {
		EducationEntity entity = educationPersistenceMapper.toEntity(education);
		if (entity.id == null) {
			entity.persist();
			return educationPersistenceMapper.toDomain(entity);
		}

		EducationEntity managedEntity = EducationEntity.findById(entity.id);
		if (managedEntity == null) {
			entity.persist();
			return educationPersistenceMapper.toDomain(entity);
		}

		managedEntity.institution = entity.institution;
		managedEntity.degree = entity.degree;
		managedEntity.startDate = entity.startDate;
		managedEntity.endDate = entity.endDate;
		return educationPersistenceMapper.toDomain(managedEntity);
	}

	@Override
	public void deleteById(Long id) {
		EducationEntity.deleteById(id);
	}

	@Override
	public Optional<Education> findById(Long id) {
		EducationEntity entity = EducationEntity.findById(id);
		if (entity == null) return Optional.empty();
		return Optional.of(educationPersistenceMapper.toDomain(entity));
	}
}