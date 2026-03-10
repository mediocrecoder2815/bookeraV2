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

    public Category createCategory(String name){
        Category c = new Category();
        c.setCategoryName(name);
        return categoryRepository.save(c);
    }
    public void deleteCategoryById(Long id){
        Category c = categoryRepository.findById(id).orElseThrow();
        categoryRepository.delete(c);
    }
    public Category updateCategory(Long id, String name){
        Category c = categoryRepository.findById(id).orElseThrow();
        c.setCategoryName(name);
        return c;
    }
    public Category getCategoryById(Long id){
        return categoryRepository.findById(id).orElseThrow();
    }
    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }
    public Category getCategoryByName(String name){
        return categoryRepository.findByCategoryName(name);
    }

}
