package personal.bookerav2.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import personal.bookerav2.entities.Category;
import personal.bookerav2.exceptions.ResourceDuplicateException;
import personal.bookerav2.exceptions.ResourceNotFound;
import personal.bookerav2.repository.CategoryRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Category createCategory(Category category){
        Category c = new Category();
        findDuplicates(category.getCategoryName());
        c.setCategoryName(category.getCategoryName());
        return categoryRepository.save(c);
    }
    public void deleteCategoryById(Integer id){
        Category c = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFound("Category with id " + id + " doesn't exists")
        );
        categoryRepository.delete(c);
    }
    public Category updateCategory(Integer id, String name){
        Category c = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFound("Category with id " + id + " doesn't exists")
        );
        findDuplicates(name);
        c.setCategoryName(name);
        return categoryRepository.save(c);

    }
    public Category getCategoryById(Integer id){
        return findById(id);
    }
    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }
    public Category getCategoryByName(String name){
        return categoryRepository.findByCategoryName(name).orElseThrow(
                () -> new ResourceNotFound("Category with name " + name + " doesn't exists")
        );
    }




    private Category findById(int id){
        return categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFound("Category with id " + id + "doesn't exists")
        );
    }

    private void findDuplicates(String name){
        if(categoryRepository.findByCategoryName(name).isPresent()){
            log.error("Category with {} already exists", name);
            throw new ResourceDuplicateException("Category with name +" + name + " already existis!");
        }
    }
}
