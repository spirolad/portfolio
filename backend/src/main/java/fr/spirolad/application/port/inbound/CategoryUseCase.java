package fr.spirolad.application.port.inbound;

import fr.spirolad.domain.model.Category;
import java.util.List;

public interface CategoryUseCase {
    List<Category> getAllCategories();
    Category getCategory(Long categoryId);
    Category saveCategory(Category category);
    Category updateCategory(Long categoryId, Category category);
    void deleteCategory(Long categoryId);
}
