package com.web.ecommerce.repository;

import com.web.ecommerce.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage,Long> {
    List<ProductImage> findProductImagesByProduct_ProductId(Long productProductId);

    Optional< ProductImage> findByImageIdAndProduct_ProductId(Long imageId, Long productId);

    Optional<ProductImage> findByProduct_ProductIdAndIsPrimaryTrue(Long productId);

    List<ProductImage> findAllByProduct_ProductId(Long productId);
}
