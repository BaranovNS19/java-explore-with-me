package practicum.ru.product.category;

import org.springframework.stereotype.Component;
import practicum.ru.product.dto.CategoryDto;

@Component
public class CategoryMapper {

    public Category toCategory(CategoryDto categoryDto) {
        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());
        return category;
    }
}
