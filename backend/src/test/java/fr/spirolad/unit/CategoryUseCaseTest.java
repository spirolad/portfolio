package fr.spirolad.unit;

import fr.spirolad.application.port.outbound.CategoryPersistencePort;
import fr.spirolad.application.usecase.CategoryUseCaseImpl;
import fr.spirolad.domain.exception.CategoryNotFoundException;
import fr.spirolad.domain.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CategoryUseCaseTest {

    private CategoryPersistencePort categoryPersistencePort;
    private CategoryUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        categoryPersistencePort = mock(CategoryPersistencePort.class);
        useCase = new CategoryUseCaseImpl(categoryPersistencePort);
    }

    @Test
    void getAllCategories_delegatesToPort() {
        Category category = new Category(1L, "Languages");
        when(categoryPersistencePort.findAll()).thenReturn(List.of(category));

        List<Category> result = useCase.getAllCategories();

        assertEquals(1, result.size());
        assertEquals(category, result.get(0));
        verify(categoryPersistencePort).findAll();
    }

    @Test
    void saveCategory_delegatesToPort() {
        Category in = new Category(null, "Languages");
        Category saved = new Category(7L, "Languages");
        when(categoryPersistencePort.save(in)).thenReturn(saved);

        Category result = useCase.saveCategory(in);

        assertEquals(saved, result);
        verify(categoryPersistencePort).save(in);
    }

    @Test
    void getCategory_notFound_throws() {
        when(categoryPersistencePort.findById(2L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> useCase.getCategory(2L));
    }

    @Test
    void getCategory_found_returnsCategory() {
        Category category = new Category(2L, "Languages");
        when(categoryPersistencePort.findById(2L)).thenReturn(Optional.of(category));

        Category result = useCase.getCategory(2L);

        assertEquals(category, result);
    }

    @Test
    void updateCategory_found_updatesAndSaves() {
        Category existing = new Category(3L, "Old");
        Category updated = new Category(null, "New");

        when(categoryPersistencePort.findById(3L)).thenReturn(Optional.of(existing));
        when(categoryPersistencePort.update(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Category result = useCase.updateCategory(3L, updated);

        assertEquals(3L, result.getId());
        assertEquals("New", result.getName());
        verify(categoryPersistencePort).update(existing);
    }

    @Test
    void updateCategory_notFound_throws() {
        when(categoryPersistencePort.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> useCase.updateCategory(99L, new Category(null, "A")));
    }

    @Test
    void deleteCategory_delegatesToPort() {
        useCase.deleteCategory(12L);

        verify(categoryPersistencePort).deleteById(12L);
    }
}