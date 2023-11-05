package com.onk.service;

import com.onk.core.results.DataResult;
import com.onk.core.results.Result;
import com.onk.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    DataResult<CategoryDto> getCategory(Long id);

    DataResult<CategoryDto> getCategoryByName(String categoryName);

    Result addCategory(CategoryDto categoryDto);

    DataResult<List<CategoryDto>> allCategories();

    Result changeActivate(Long categoryId);

    DataResult<List<CategoryDto>> allCategoriesByIsActiveTrue();

    DataResult<List<CategoryDto>> allCategoriesByIsActiveFalse();

    DataResult<List<CategoryDto>> allSubCategories(Long id);

    Result addSubCategory(Long parentId, Long subCategoryId);

    Result changeParentCategory(Long categoryId, Long newParentCategoryId);

    Result deleteCategory(Long id);
}
