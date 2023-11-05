package com.onk.repository;

import com.onk.model.Category;
import com.onk.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByName(String productName);

    Optional<Product> findByTitle(String title);

    List<Product> findProductsByIsPublishedTrue();

    @Query("select p from Product p where p.category.id = :categoryId")
    List<Product> findProductsByCategoryId(Long categoryId);

    List<Product> findProductsByIsPublishedFalse();

    List<Product> findAllByCategoryOrderByCreatedDate(Category category);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update Product product " +
            "set product.isPublished =:state where product.id=:id")
    void changeProductPublish(@Param("id") Long id, @Param("state") Boolean state);


}
