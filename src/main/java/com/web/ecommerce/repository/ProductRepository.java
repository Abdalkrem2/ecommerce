package com.web.ecommerce.repository;

import com.web.ecommerce.model.Category;
import com.web.ecommerce.model.Product;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findProductByProductId(Long productId);

    Page<Product> findProductsByCategory_CategoryId(Long categoryCategoryId, Pageable pageable);

    Page<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description, Pageable pageable);

//    Page<Product> findByCategoryIdAndNameContainingIgnoreCaseOrCategoryIdAndDescriptionContainingIgnoreCase(Long categoryId,String name, String description,Pageable pageable);

    @Query("""
SELECT p FROM Product p
WHERE p.category.categoryId = :categoryId
  AND (
       LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
    OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
  )
""")
    Page<Product> searchInCategory(@Param("categoryId") Long categoryId,
                                   @Param("keyword") String keyword,
                                   Pageable pageable);

}
