package fr.spirolad.infrastructure.persistence.mapper;

import fr.spirolad.domain.model.Category;
import fr.spirolad.infrastructure.persistence.database.CategoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface CategoryPersistenceMapper {

    Category toDomain(CategoryEntity entity);

    CategoryEntity toEntity(Category domain);
}
