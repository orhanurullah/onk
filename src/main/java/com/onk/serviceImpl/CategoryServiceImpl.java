package com.onk.serviceImpl;

import com.onk.component.MessageService;
import com.onk.core.exception.GenericException;
import com.onk.core.results.*;
import com.onk.dto.CategoryDto;
import com.onk.model.Category;
import com.onk.repository.CategoryRepository;
import com.onk.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final MessageService messageService;

    @Override
    public DataResult<CategoryDto> getCategory(Long id) {
        try{
            var category = categoryRepository.findById(id)
                    .orElseThrow(EntityNotFoundException::new);
            return new SuccessDataResult<>(
                    convertCategoryToCategoryDto(category),
                    messageService.getMessage("category.select.success.message", new Object[]{category.getName()})
            );
        }catch (EntityNotFoundException e){
            return new ErrorDataResult<>(
                    messageService.getMessage("category.select.error.message", new Object[]{id})
            );
        }catch (Exception e){
            return new ErrorDataResult<>(
                    messageService.getMessage("error.message", null)
            );
        }
    }

    @Override
    public DataResult<CategoryDto> getCategoryByName(String categoryName) {
        try{
            var category = categoryRepository.findByNameIgnoreCase(categoryName).orElseThrow(EntityNotFoundException::new);
            return new SuccessDataResult<>(
                    convertCategoryToCategoryDto(category),
                    messageService.getMessage("category.select.success.message", new Object[]{categoryName})
            );
        }catch (EntityNotFoundException e){
            return new ErrorDataResult<>(
                    messageService.getMessage("category.select.error.message", new Object[]{categoryName})
            );
        }catch (Exception e){
            return new ErrorDataResult<>(
                    messageService.getMessage("error.message", null)
            );
        }
    }

    @Override
    public Result addCategory(CategoryDto categoryDto){
        if(categoryRepository.findByNameIgnoreCase(categoryDto.getName().trim()).isPresent()){
            return new ErrorResult(
                    messageService.getMessage("category.reInsert.error.message", new Object[]{categoryDto.getName()})
            );
        }
        try{
            var category = convertCategoryDtoToCategory(categoryDto);
            var savedCategory = categoryRepository.save(category);
            return new SuccessResult(
                    messageService.getMessage("category.insert.success.message", new Object[]{savedCategory.getName()})
            );
        }catch(GenericException e){
            return new ErrorResult(
                    messageService.getMessage("category.insert.error.message", new Object[]{categoryDto.getName()})
            );
        }
    }

    @Override
    public DataResult<List<CategoryDto>> allCategories(){
        try{
            var categories = categoryRepository.findAll();
            var dtoCategories = categories.stream().map(CategoryServiceImpl::convertCategoryToCategoryDto).toList();
            return new SuccessDataResult<>(
                    dtoCategories,
                    messageService.getMessage("success.data.message", null)
            );
        }catch (Exception e){
            return new ErrorDataResult<>(
                    messageService.getMessage("error.message", null)
            );
        }
    }

    @Override
    public Result changeActivate(Long categoryId){
        try{
            var category = categoryRepository.findById(categoryId).orElseThrow(EntityNotFoundException::new);
            categoryRepository.changeActivation(!category.getIsActive(), categoryId);
            return new SuccessResult(
                    messageService.getMessage("category.change.isActive.success.message", new Object[]{category.getName()})
            );
        }catch(EntityNotFoundException e){
            return new ErrorDataResult<>(
                    messageService.getMessage("category.select.error.message", new Object[]{categoryId})
            );
        }
        catch (Exception e){
            return new ErrorResult(
                    messageService.getMessage("error.message", null)
            );
        }
    }

    @Override
    public DataResult<List<CategoryDto>> allCategoriesByIsActiveTrue(){
        try{
            var activeCategories = this.categoryRepository.findAllByIsActiveTrue();
            var activeDtoCategories = activeCategories.stream().map(CategoryServiceImpl::convertCategoryToCategoryDto).collect(Collectors.toList());
            return new SuccessDataResult<>(
                    activeDtoCategories,
                    messageService.getMessage("success.data.message", null)
            );
        }catch (Exception e){
            return new ErrorDataResult<>(
                    messageService.getMessage("error.message", null)
            );
        }
    }

    @Override
    public DataResult<List<CategoryDto>> allCategoriesByIsActiveFalse(){
        try{
            var notActiveCategories = this.categoryRepository.findAllByIsActiveFalse();
            var notActiveDtoCategories = notActiveCategories.stream().map(CategoryServiceImpl::convertCategoryToCategoryDto).toList();
            return new SuccessDataResult<>(
                    notActiveDtoCategories,
                    messageService.getMessage("success.data.message", null)
            );
        }catch (Exception e){
            return new ErrorDataResult<>(
                    messageService.getMessage("error.message", null)
            );
        }
    }

    @Override
    public DataResult<List<CategoryDto>> allSubCategories(Long id){
        try{
            var category = this.categoryRepository.findById(id).orElse(null);
            if(category == null){
                return new ErrorDataResult<>(
                        messageService.getMessage("category.select.error.message", new Object[]{id})
                );
            }
            List<Category> categories = categoryRepository.getCategoriesByParentCategoryId(category);
            if(categories.isEmpty()){
                return new SuccessDataResult<>(
                        null,
                        messageService.getMessage("success.null.data.message", null)
                        );
            }
            var subDtoCategories = categories.stream().map(CategoryServiceImpl::convertCategoryToCategoryDto).toList();
            return new SuccessDataResult<>(
                    subDtoCategories,
                    messageService.getMessage("success.data.message", null)
            );
        }catch (Exception e){
            return new ErrorDataResult<>(
                    messageService.getMessage("error.message", null)
            );
        }
    }

    @Override
    public Result addSubCategory(Long parentId, Long subCategoryId){
        try{
            Category category = categoryRepository.findById(parentId).orElse(null);
            Category subCategory = categoryRepository.findById(subCategoryId).orElse(null);
            if(category == null || subCategory == null){
                return new ErrorResult(
                        messageService.getMessage("category.select.error.message", null)
                );
            }
            subCategory.setParentCategory(category);
            categoryRepository.save(subCategory);
            return new SuccessResult(
                    messageService.getMessage("category.insertSubCategory.success.message", new Object[]{category.getName(), subCategory.getName()})
            );
        }catch (Exception e){
            return new ErrorDataResult<>(
                    messageService.getMessage("category.insertSubCategory.error.message", null)
            );
        }
    }

    @Override
    public Result changeParentCategory(Long categoryId, Long newParentCategoryId) {
       try{
           var category = this.categoryRepository.findById(categoryId).orElse(null);
           var parentCategory = this.categoryRepository.findById(newParentCategoryId).orElse(null);
           if(category == null){
               return new ErrorResult(
                       messageService.getMessage("category.select.error.message", new Object[]{categoryId})
               );
           }
           category.setParentCategory(parentCategory);
           return new SuccessResult(
                   messageService.getMessage("category.changeParentCategory.message", new Object[]{category.getName()})
           );
       }catch(Exception e){
            return new ErrorResult(
                    messageService.getMessage("error.message", null)
            );
       }
    }


    @Override
    public Result deleteCategory(Long id){
        try{
            var category = categoryRepository.findById(id).orElse(null);
            if(category == null){
                return new ErrorResult(
                        messageService.getMessage("error.not_found", new Object[]{id})
                );
            }
            categoryRepository.delete(category);
            return new ErrorResult(
                    messageService.getMessage("category.delete.success.message", new Object[]{category.getName()})
            );
        }catch (Exception e){
            return new ErrorDataResult<>(
                    messageService.getMessage("error.message", null)
            );
        }
    }

    private static Category convertCategoryDtoToCategory(CategoryDto categoryDto){
        return Category.builder()
                .name(categoryDto.getName())
                .description(categoryDto.getDescription())
                .isActive(categoryDto.isActive())
                .build();
    }

    private static CategoryDto convertCategoryToCategoryDto(Category category){
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .isActive(category.getIsActive())
                .build();
    }
}
