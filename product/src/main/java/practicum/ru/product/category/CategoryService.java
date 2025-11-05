package practicum.ru.product.category;

import practicum.ru.product.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    Category createCategory(CategoryDto categoryDto);

    Category updateCategory(Long catId, CategoryDto categoryDto);

    List<Category> getCategories(int from, int size);

    Category getCategoryById(Long id);

    void deleteCategory(Long id);
}
