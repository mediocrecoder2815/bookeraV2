package personal.bookerav2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import personal.bookerav2.entities.Category;
import personal.bookerav2.service.CategoryService;

import java.util.List;


@RestController
@RequestMapping("/api/categories")
@AllArgsConstructor
@Tag(name = "Categories", description = "Endpoints for managing categories")
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(summary = "Get all categories")
    @ApiResponse(responseCode = "200", description = "List of categories",
            content = @Content(schema = @Schema(implementation = Category.class)))
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @Operation(summary = "Get a category by ID")
    @Parameter(name = "id", description = "Category ID")
    @ApiResponse(responseCode = "200", description = "Category found",
            content = @Content(schema = @Schema(implementation = Category.class)))
    @ApiResponse(responseCode = "404", description = "Category not found")
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Integer id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @Operation(summary = "Create a new category")
    @ApiResponse(responseCode = "200", description = "Category created",
            content = @Content(schema = @Schema(implementation = Category.class)))
    @ApiResponse(responseCode = "400", description = "Validation failed")
    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category c){
        return ResponseEntity.ok(categoryService.createCategory(c));
    }

    @Operation(summary = "Update a category by ID")
    @Parameter(name = "id", description = "Category ID")
    @ApiResponse(responseCode = "200", description = "Category updated",
            content = @Content(schema = @Schema(implementation = Category.class)))
    @ApiResponse(responseCode = "404", description = "Category not found")
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Integer id, @RequestBody Category c){
        return ResponseEntity.ok(categoryService.updateCategory(id, c.getCategoryName()));

    }
    @Operation(summary = "Delete a category by ID")
    @Parameter(name = "id", description = "Category ID")
    @ApiResponse(responseCode = "200", description = "Category deleted")
    @ApiResponse(responseCode = "404", description = "Category not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id){
        categoryService.deleteCategoryById(id);
        return ResponseEntity.ok().build();
    }
}
