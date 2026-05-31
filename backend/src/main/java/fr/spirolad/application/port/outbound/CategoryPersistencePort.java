package fr.spirolad.application.port.outbound;

import fr.spirolad.domain.model.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryPersistencePort {
    List<Category> findAll();
    Optional<Category> findById(Long categoryId);
    Category save(Category category);
    Category update(Category category);
    void deleteById(Long categoryId);
}
