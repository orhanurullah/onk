package com.onk.api.admin;

import com.onk.core.utils.RouteConstants;
import com.onk.dto.CategoryDto;
import com.onk.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(RouteConstants.adminBaseRoute + RouteConstants.categoryBaseRoute)
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping(RouteConstants.categoryCreateRoute)
    public ResponseEntity<?> addCategory(@Valid @RequestBody CategoryDto categoryDto){
       return ResponseEntity.ok(categoryService.addCategory(categoryDto));
    }
    @GetMapping(RouteConstants.categoryFindByIdRoute)
    public ResponseEntity<?> getCategory(@PathVariable Long id){
        return ResponseEntity.ok(categoryService.getCategory(id));
    }
    @GetMapping(RouteConstants.categoryFindByNameRoute)
    public ResponseEntity<?> getCategoryByName(@RequestParam("category_name") String name){
        return ResponseEntity.ok(categoryService.getCategoryByName(name));
    }
    @GetMapping(RouteConstants.categoryFindAllRoute)
    public ResponseEntity<?> categories(){
        return ResponseEntity.ok(categoryService.allCategories());
    }

    @PostMapping(RouteConstants.categoryChangeActivationRoute)
    public ResponseEntity<?> changeActivate(@Valid @PathVariable Long id){
        return ResponseEntity.ok(categoryService.changeActivate(id));
    }
    @PostMapping(RouteConstants.categoryChangeParentCategoryRoute)
    public ResponseEntity<?> changeParentCategory(@RequestParam("category_id") Long categoryId,
                                                  @RequestParam("parent_category_id") Long newParentCategoryId){
        return ResponseEntity.ok(categoryService.changeParentCategory(categoryId, newParentCategoryId));
    }
    @GetMapping(RouteConstants.categoryFindAllActiveRoute)
    public ResponseEntity<?> activeCategories(){
        return ResponseEntity.ok(categoryService.allCategoriesByIsActiveTrue());
    }

    @GetMapping(RouteConstants.categoryFindAllNotActiveRoute)
    public ResponseEntity<?> allNotActiveCategories(){
        return ResponseEntity.ok(categoryService.allCategoriesByIsActiveFalse());
    }

    @GetMapping(RouteConstants.categoryFindSubCategoriesRoute)
    public ResponseEntity<?> allSubCategories(@PathVariable Long id){
        return ResponseEntity.ok(this.categoryService.allSubCategories(id));
    }

    @PostMapping(RouteConstants.categoryCreateSubCategory)
    public ResponseEntity<?> addSubCategory(@RequestParam("parentCategoryId") Long id,
                                            @RequestParam("subCategoryId") Long subCategoryId){
        return ResponseEntity.ok(categoryService.addSubCategory(id, subCategoryId));
    }

    @PostMapping(RouteConstants.categoryDeleteRoute)
    public ResponseEntity<?> deleteCategory(@PathVariable Long id){
        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }
}
