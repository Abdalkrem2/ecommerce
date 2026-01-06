package com.web.ecommerce.service.serviceImp;

import com.web.ecommerce.dto.product.*;
import com.web.ecommerce.exceptions.APIException;
import com.web.ecommerce.model.Category;
import com.web.ecommerce.model.Product;
import com.web.ecommerce.model.ProductImage;
import com.web.ecommerce.repository.CategoryRepository;
import com.web.ecommerce.repository.ProductRepository;
import com.web.ecommerce.service.ProductService;
import org.jspecify.annotations.Nullable;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImp implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public ProductPageResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortOrder, String sortType) {
        Sort sort=sortOrder.equalsIgnoreCase("asc")
                ?Sort.by(sortType).ascending()
                :Sort.by(sortType).descending();

        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);

        Page<Product> products=productRepository.findAll(pageable);

        ProductPageResponse productPageResponse=new ProductPageResponse();


        List<ProductResponse> productResponse= products.getContent().stream().map(product ->

        {
           ProductResponse dto= modelMapper.map(product, ProductResponse.class);

         String primaryUrl=   product.getImages().stream().filter(img->Boolean.TRUE.equals(img.getIsPrimary())).map(ProductImage::getUrl).findFirst()
                 .orElseGet(() ->
                 product.getImages().stream()
                 .findFirst()
                 .map(ProductImage::getUrl)
                 .orElse(null));

         dto.setPrimaryImageUrl(primaryUrl);
         return dto;
            }).toList() ;



        productPageResponse.setContent(productResponse);
        productPageResponse.setPageNumber(products.getNumber());
        productPageResponse.setPageSize(products.getSize());
        productPageResponse.setTotalElements(products.getTotalElements());
        productPageResponse.setTotalPages(products.getTotalPages());
        productPageResponse.setLastPage(products.isLast());

        return productPageResponse;
    }


    @Override
    public ProductDetailsResponse createProduct(ProductRequest productRequest, Long categoryId) {
        Category category=categoryRepository.findById(categoryId).orElseThrow(()-> new APIException("Category not found"));


       Product product=new Product();
       product.setName(productRequest.getName());
       product.setDescription(productRequest.getDescription());
       product.setPrice(productRequest.getPrice());
      product.setStockQuantity(productRequest.getStockQuantity());
      product.setCategory(category);


      if(productRequest.getImageUrls()!=null&&!productRequest.getImageUrls().isEmpty()){
          int primaryIndex=(productRequest.getPrimaryImageIndex()!=null) ? productRequest.getPrimaryImageIndex() : 0;

          if (primaryIndex < 0 || primaryIndex >= productRequest.getImageUrls().size()) {
              primaryIndex = 0;
          }
          for(int i=0; i<productRequest.getImageUrls().size(); i++){
              String imageUrl=productRequest.getImageUrls().get(i);
               ProductImage productImage=new ProductImage();

               productImage.setUrl(imageUrl);
               productImage.setIsPrimary(i==primaryIndex);
               productImage.setProduct(product);

               product.getImages().add(productImage);
          }

      }
        category.getProducts().add(product);
        categoryRepository.save(category);
      Product savedProduct=productRepository.save(product);

        return modelMapper.map(savedProduct, ProductDetailsResponse.class);
    }

    @Override
    public ProductResponse updateProduct(UpdateProductRequest productRequest, Long productId) {
        Product product= productRepository.findProductByProductId(productId);
        if(product==null){
            throw new APIException("Product not found");
        }
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setStockQuantity(productRequest.getStockQuantity());
        product.setActive(productRequest.getActive());
        Product updatedProduct=productRepository.save(product);
        ProductResponse productResponse=modelMapper.map(updatedProduct, ProductResponse.class);
        return productResponse;
    }


    @Override
    public String deleteProduct(Long productId) {
        if(!productRepository.existsById(productId)){
            throw new APIException("Product not found");
        }
        productRepository.deleteById(productId);
        return "Product deleted successfully";
    }

    @Override
    public ProductPageResponse getProductsByCategory(Pageable pageable, Long categoryId) {
        categoryRepository.findById(categoryId).orElseThrow(()-> new APIException("Category not found"));

        Page<Product> products= productRepository.findProductsByCategory_CategoryId(categoryId,pageable);

        List<ProductResponse> productResponse=products.getContent() .stream().map(product->modelMapper.map(product, ProductResponse.class)).toList();


        ProductPageResponse productPageResponse=new ProductPageResponse();

        productPageResponse.setTotalPages(products.getTotalPages());
        productPageResponse.setTotalElements(products.getTotalElements());
        productPageResponse.setLastPage(products.isLast());
        productPageResponse.setPageNumber(products.getNumber());
        productPageResponse.setPageSize(products.getSize());
        productPageResponse.setContent(productResponse);

        return productPageResponse;
    }

    @Override
    public @Nullable ProductPageResponse searchProducts(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isBlank()) {
            throw new APIException("keyword must not be empty");
        }
        Page<Product> page = productRepository
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                        keyword.trim(), keyword.trim(), pageable
                );

        List<ProductResponse> content = page.getContent().stream()
                .map(p -> modelMapper.map(p, ProductResponse.class))
                .toList();

        ProductPageResponse res = new ProductPageResponse();
        res.setContent(content);
        res.setPageNumber(page.getNumber());
        res.setPageSize(page.getSize());
        res.setTotalElements(page.getTotalElements());
        res.setTotalPages(page.getTotalPages());
        res.setLastPage(page.isLast());

        return res;
    }

    @Override
    public @Nullable ProductPageResponse searchProductsInCategory(Long categoryId, String keyword, Pageable pageable) {
        if (keyword == null || keyword.isBlank()) {
            throw new APIException("keyword must not be empty");
        }
        Page<Product> products=productRepository.searchInCategory(categoryId,keyword.trim(),pageable);

        List<ProductResponse> content = products.getContent().stream()
                .map(p -> modelMapper.map(p, ProductResponse.class))
                .toList();

        ProductPageResponse res = new ProductPageResponse();
        res.setContent(content);
        res.setPageNumber(products.getNumber());
        res.setPageSize(products.getSize());
        res.setTotalElements(products.getTotalElements());
        res.setTotalPages(products.getTotalPages());
        res.setLastPage(products.isLast());

        return res;
    }

    @Override
    public ProductDetailsResponse getProductById(Long productId) {
        Product product=productRepository.findById(productId).orElseThrow(()-> new APIException("Product not found"));
        return modelMapper.map(product, ProductDetailsResponse.class);
    }

}
