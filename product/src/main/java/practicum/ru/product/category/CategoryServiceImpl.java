package practicum.ru.product.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import practicum.ru.product.dto.CategoryDto;
import practicum.ru.product.dto.NewCategoryDto;
import practicum.ru.product.event.EventRepository;
import practicum.ru.product.exception.ConflictException;
import practicum.ru.product.exception.NotFoundException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final EventRepository eventRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper, EventRepository eventRepository) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.eventRepository = eventRepository;
    }

    @Override
    public Category createCategory(NewCategoryDto categoryDto) {
        if (categoryRepository.findByName(categoryDto.getName()) != null) {
            throw new ConflictException("категория с именем [" + categoryDto.getName() + "] уже существует");
        }
        return categoryRepository.save(categoryMapper.toCategory(categoryDto));
    }

    @Override
    public Category updateCategory(Long catId, CategoryDto categoryDto) {
        Category category = getCategoryById(catId);
        if (categoryRepository.findByName(categoryDto.getName()) != null &&
                !Objects.equals(categoryRepository.findByName(categoryDto.getName()).getId(), catId)) {
            throw new ConflictException("категория с именем [" + categoryDto.getName() + "] уже существует");
        }
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
        if (!eventRepository.findByCategoryId(id).isEmpty()) {
            throw new ConflictException("Категория привязана к событиям");
        }
        categoryRepository.deleteById(id);
    }
}
