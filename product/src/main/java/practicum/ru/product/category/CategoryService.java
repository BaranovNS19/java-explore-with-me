package practicum.ru.product.category;

import practicum.ru.product.dto.CategoryDto;
import practicum.ru.product.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {

    Category createCategory(NewCategoryDto newCategoryDto);

    Category updateCategory(Long catId, CategoryDto categoryDto);

    List<Category> getCategories(int from, int size);

    Category getCategoryById(Long id);

    void deleteCategory(Long id);
}
