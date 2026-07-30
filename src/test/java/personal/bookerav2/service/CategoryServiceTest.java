package personal.bookerav2.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import personal.bookerav2.entities.Category;
import personal.bookerav2.exceptions.ResourceDuplicateException;
import personal.bookerav2.exceptions.ResourceNotFound;
import personal.bookerav2.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CategoryService unit tests")
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .categoryId(1)
                .categoryName("Fantasy")
                .build();
    }

    @Nested
    @DisplayName("createCategory")
    class CreateCategory {

        @Test
        void shouldCreateCategorySuccessfully() {
            when(categoryRepository.findByCategoryName("Fantasy")).thenReturn(Optional.empty());
            when(categoryRepository.save(any(Category.class))).thenReturn(category);

            Category result = categoryService.createCategory(category);

            assertNotNull(result);
            assertEquals("Fantasy", result.getCategoryName());
            verify(categoryRepository).findByCategoryName("Fantasy");
            verify(categoryRepository).save(any(Category.class));
        }

        @Test
        void shouldThrowWhenDuplicateName() {
            when(categoryRepository.findByCategoryName("Fantasy")).thenReturn(Optional.of(category));

            assertThrows(ResourceDuplicateException.class, () -> categoryService.createCategory(category));
            verify(categoryRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("getCategoryById")
    class GetCategoryById {

        @Test
        void shouldReturnCategoryWhenFound() {
            when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

            Category result = categoryService.getCategoryById(1);

            assertNotNull(result);
            assertEquals("Fantasy", result.getCategoryName());
        }

        @Test
        void shouldThrowWhenNotFound() {
            when(categoryRepository.findById(99)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFound.class, () -> categoryService.getCategoryById(99));
        }
    }

    @Nested
    @DisplayName("getAllCategories")
    class GetAllCategories {

        @Test
        void shouldReturnAllCategories() {
            when(categoryRepository.findAll()).thenReturn(List.of(category));

            List<Category> result = categoryService.getAllCategories();

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Fantasy", result.getFirst().getCategoryName());
        }
    }

    @Nested
    @DisplayName("getCategoryByName")
    class GetCategoryByName {

        @Test
        void shouldReturnCategoryWhenFound() {
            when(categoryRepository.findByCategoryName("Fantasy")).thenReturn(Optional.of(category));

            Category result = categoryService.getCategoryByName("Fantasy");

            assertNotNull(result);
            assertEquals("Fantasy", result.getCategoryName());
        }

        @Test
        void shouldThrowWhenNotFound() {
            when(categoryRepository.findByCategoryName("Unknown")).thenReturn(Optional.empty());

            assertThrows(ResourceNotFound.class, () -> categoryService.getCategoryByName("Unknown"));
        }
    }

    @Nested
    @DisplayName("updateCategory")
    class UpdateCategory {

        @Test
        void shouldUpdateCategorySuccessfully() {
            when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
            when(categoryRepository.findByCategoryName("Sci-fi")).thenReturn(Optional.empty());
            when(categoryRepository.save(any(Category.class))).thenReturn(category);

            Category result = categoryService.updateCategory(1, "Sci-fi");

            assertNotNull(result);
            assertEquals("Sci-fi", result.getCategoryName());
            verify(categoryRepository).findById(1);
            verify(categoryRepository).save(any(Category.class));
        }

        @Test
        void shouldThrowWhenNotFound() {
            when(categoryRepository.findById(99)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFound.class, () -> categoryService.updateCategory(99, "NewName"));
        }

        @Test
        void shouldThrowWhenDuplicateName() {
            Category existing = Category.builder().categoryId(2).categoryName("Sci-fi").build();
            when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
            when(categoryRepository.findByCategoryName("Sci-fi")).thenReturn(Optional.of(existing));

            assertThrows(ResourceDuplicateException.class, () -> categoryService.updateCategory(1, "Sci-fi"));
            verify(categoryRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("deleteCategoryById")
    class DeleteCategoryById {

        @Test
        void shouldDeleteCategorySuccessfully() {
            when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

            categoryService.deleteCategoryById(1);

            verify(categoryRepository).findById(1);
            verify(categoryRepository).delete(category);
        }

        @Test
        void shouldThrowWhenNotFound() {
            when(categoryRepository.findById(99)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFound.class, () -> categoryService.deleteCategoryById(99));
        }
    }
}
