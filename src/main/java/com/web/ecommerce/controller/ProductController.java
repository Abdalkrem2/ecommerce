package com.web.ecommerce.controller;

import com.web.ecommerce.config.AppConstant;
import com.web.ecommerce.dto.product.*;
import com.web.ecommerce.dto.product.productImageDTO.ProductImageRequest;
import com.web.ecommerce.dto.product.productImageDTO.ProductImageResponse;
import com.web.ecommerce.model.Product;
import com.web.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;




    @GetMapping("/public/products")
    public ResponseEntity<ProductPageResponse> getProducts(@RequestParam(name = "pageNumber" ,defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) Integer pageNumber, @RequestParam(name = "pageSize",defaultValue = AppConstant.DEFAULT_PAGE_SIZE)Integer pageSize,
            @RequestParam(name = "sortOrder",defaultValue = AppConstant.DEFAULT_SORT_ORDER)String sortOrder, @RequestParam(name = "sortType",defaultValue = AppConstant.SORT_PRODUCT_BY)String sortType) {

  ProductPageResponse products=productService.getAllProducts(pageNumber,pageSize,sortOrder,sortType);
    return new ResponseEntity<>(products, HttpStatus.OK);

}
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/categories/{categoryId}/products")
    public ResponseEntity<ProductDetailsResponse> createProduct(@RequestBody @Valid ProductRequest productRequest, @PathVariable Long categoryId) {
        ProductDetailsResponse productDetailsResponse=productService.createProduct(productRequest,categoryId);
        return new ResponseEntity<>(productDetailsResponse, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(@RequestBody @Valid UpdateProductRequest productRequest, @PathVariable Long productId) {
        ProductResponse productResponse=productService.updateProduct(productRequest,productId);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        String status=productService.deleteProduct(productId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductPageResponse>getProductsByCategory(Pageable pageable, @PathVariable Long categoryId) {
    ProductPageResponse productPageResponse=productService.getProductsByCategory(pageable,categoryId);
    return new ResponseEntity<>(productPageResponse, HttpStatus.OK);
    }

    @GetMapping("/public/products/search")
    public ResponseEntity<ProductPageResponse> searchProducts(@RequestParam String keyword, Pageable pageable) {
        ProductPageResponse productPageResponse=productService.searchProducts(keyword,pageable);
        return new ResponseEntity<>(productPageResponse, HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}/products/search")
    public ResponseEntity<ProductPageResponse> searchProductsInCategory(@PathVariable Long categoryId, @RequestParam String keyword,Pageable pageable) {
        ProductPageResponse productResponse=productService.searchProductsInCategory(categoryId, keyword,pageable);
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }



    @GetMapping("/public/products/{productId}")
    public ResponseEntity<ProductDetailsResponse> getProduct(@PathVariable Long productId){
        ProductDetailsResponse productDetailsResponse=productService.getProductById(productId);
        return new ResponseEntity<>(productDetailsResponse, HttpStatus.OK);
    }


}
