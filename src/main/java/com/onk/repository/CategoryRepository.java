package com.onk.repository;

import com.onk.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByNameIgnoreCase(String name);

    @Query("select c from Category c where c.isActive = true")
    Set<Category> findAllByIsActiveTrue();

    @Query("select c from Category c where c.isActive = false")
    Set<Category> findAllByIsActiveFalse();

    @Query("select c from Category c where c.parentCategory = :category")
    List<Category> getCategoriesByParentCategoryId(Category category);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update Category category " +
            "set category.isActive =:state where category.id=:id")
    void changeActivation(@Param("state") Boolean state, @Param("id") Long id);



}
