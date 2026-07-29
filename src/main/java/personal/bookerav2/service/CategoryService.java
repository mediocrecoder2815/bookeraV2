package personal.bookerav2.service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import personal.bookerav2.entities.Category;
import personal.bookerav2.repository.CategoryRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Category createCategory(Category category){
        Category c = new Category();
        if (categoryRepository.findByCategoryName(category.getCategoryName()).isPresent()){
            throw new IllegalArgumentException("Category already exists");
        }
        c.setCategoryName(category.getCategoryName());
        return categoryRepository.save(c);
    }
    public void deleteCategoryById(Integer id){
        Category c = categoryRepository.findById(id).orElseThrow();
        categoryRepository.delete(c);
    }
    public Category updateCategory(Integer id, String name){
        Category c = categoryRepository.findById(id).orElseThrow();
        if (categoryRepository.findByCategoryName(name).isPresent()){
            throw new IllegalArgumentException("CATEGORY ALREADY EXISTS");
        }
        c.setCategoryName(name);
        return categoryRepository.save(c);

    }
    public Category getCategoryById(Integer id){
        return categoryRepository.findById(id).orElseThrow();
    }
    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }
    public Category getCategoryByName(String name){
        return categoryRepository.findByCategoryName(name).orElseThrow();
    }

}
