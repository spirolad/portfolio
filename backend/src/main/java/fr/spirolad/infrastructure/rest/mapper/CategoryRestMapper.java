package fr.spirolad.infrastructure.rest.mapper;

import fr.spirolad.domain.model.Category;
import fr.spirolad.dto.CategoryRequest;
import fr.spirolad.dto.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi")
public interface CategoryRestMapper {

    @Mapping(target = "id", ignore = true)
    Category toDomain(CategoryRequest dto);

    CategoryResponse toResponse(Category domain);
}
