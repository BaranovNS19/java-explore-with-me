package practicum.ru.product.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import practicum.ru.product.dto.CategoryDto;
import practicum.ru.product.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public Category createCategory(CategoryDto categoryDto) {
        return categoryRepository.save(categoryMapper.toCategory(categoryDto));
    }

    @Override
    public Category updateCategory(Long catId, CategoryDto categoryDto) {
        Category category = getCategoryById(catId);
        category.setName(categoryDto.getName());
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getCategories(int from, int size) {
        return categoryRepository.findAll().stream()
                .skip(from)
                .limit(size)
                .collect(Collectors.toList());
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Категория с id [" + id + "] не найдена"));
    }

    @Override
    public void deleteCategory(Long id) {
        getCategoryById(id);
        categoryRepository.deleteById(id);
    }
}
