package fr.spirolad.application.usecase;

import fr.spirolad.application.port.inbound.CategoryUseCase;
import fr.spirolad.application.port.outbound.CategoryPersistencePort;
import fr.spirolad.domain.exception.CategoryNotFoundException;
import fr.spirolad.domain.model.Category;
import java.util.List;

public class CategoryUseCaseImpl implements CategoryUseCase {
    private final CategoryPersistencePort categoryPersistencePort;

    public CategoryUseCaseImpl(CategoryPersistencePort categoryPersistencePort) {
        this.categoryPersistencePort = categoryPersistencePort;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryPersistencePort.findAll();
    }

    @Override
    public Category getCategory(Long categoryId) {
        return categoryPersistencePort.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
    }

    @Override
    public Category saveCategory(Category category) {
        return categoryPersistencePort.save(category);
    }

    @Override
    public Category updateCategory(Long categoryId, Category category) {
        Category existingCategory = categoryPersistencePort.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        existingCategory.setName(category.getName());
        return categoryPersistencePort.update(existingCategory);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        categoryPersistencePort.deleteById(categoryId);
    }
}
