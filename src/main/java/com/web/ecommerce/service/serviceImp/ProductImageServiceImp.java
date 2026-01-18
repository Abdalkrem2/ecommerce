package com.web.ecommerce.service.serviceImp;

import com.web.ecommerce.dto.product.productImageDTO.ProductImageRequest;
import com.web.ecommerce.dto.product.productImageDTO.ProductImageResponse;
import com.web.ecommerce.exceptions.APIException;
import com.web.ecommerce.model.Product;
import com.web.ecommerce.model.ProductImage;
import com.web.ecommerce.repository.ProductImageRepository;
import com.web.ecommerce.repository.ProductRepository;
import com.web.ecommerce.service.ProductImageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductImageServiceImp implements ProductImageService {
    @Autowired
    private ProductImageRepository productImageRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public List<ProductImageResponse> getProductImages(Long productId) {
        productRepository.findById(productId).orElseThrow(()->new APIException("Product not found"));
        List<ProductImage> images=productImageRepository.findProductImagesByProduct_ProductId(productId);
        return images.stream().map(image->modelMapper.map(image, ProductImageResponse.class)).collect(Collectors.toList());
    }

    @Override
    public List<ProductImageResponse> addImages(Long productId, List<ProductImageRequest> requests) {

        if (requests == null || requests.isEmpty()) {
            throw new APIException("Images list is empty");
        }
        Product product=productRepository.findById(productId).orElseThrow(()->new APIException("Product not found"));
        ProductImageRequest Primary=requests.stream().filter(r->Boolean.TRUE.equals(r.getIsPrimary())).findFirst().orElse(null);
        if(Primary!=null) {
        productImageRepository.findByProduct_ProductIdAndIsPrimaryTrue(productId).ifPresent(existing->{
            existing.setIsPrimary(false);
        productImageRepository.save(existing);
        });
        }

        for(int i=0;i<requests.size();i++) {
            ProductImageRequest request=requests.get(i);
            ProductImage productImage=new ProductImage();
            productImage.setUrl(request.getUrl());
            productImage.setProduct(product);
            if(Primary!=null&&request==Primary||(Primary == null && product.getImages().isEmpty() && i == 0)) {
                productImage.setIsPrimary(true);
            }
            product.getImages().add(productImage);

        }
        productRepository.save(product);

        return product.getImages().stream()
                .sorted(Comparator.comparing(ProductImage::getImageId))
                .map(img -> modelMapper.map(img, ProductImageResponse.class))
                .toList();
    }


    @Override
    public ProductImageResponse setPrimary(Long productId, Long imageId) {
        productRepository.findById(productId).orElseThrow(()->new APIException("Product not found"));

        ProductImage image=productImageRepository.findByImageIdAndProduct_ProductId(imageId,productId).orElseThrow(()->new APIException("Image not found"));

        productImageRepository.findByProduct_ProductIdAndIsPrimaryTrue(productId)
                .ifPresent(existing -> {
                    if (!existing.getImageId().equals(image.getImageId())) {
                        existing.setIsPrimary(false);
                        productImageRepository.save(existing);
                    }
                });

        image.setIsPrimary(true);
        productImageRepository.save(image);
        return modelMapper.map(image, ProductImageResponse.class);
    }

    @Override
    public ProductImageResponse updateImage(Long productId, Long imageId, ProductImageRequest request) {
        productRepository.findById(productId).orElseThrow(()->new APIException("Product not found"));
        ProductImage image=productImageRepository.findByImageIdAndProduct_ProductId(imageId,productId).orElseThrow(()->new APIException("Image not found"));
        if (request.getUrl() != null && !request.getUrl().isBlank()) {
            image.setUrl(request.getUrl());
        }

        ProductImage saved = productImageRepository.save(image);
        if(request.getIsPrimary()){
            return setPrimary(productId, imageId);
        }

        return modelMapper.map(saved, ProductImageResponse.class);
    }

    @Override
    public String deleteImage(Long productId, Long imageId) {
        Product product= productRepository.findById(productId).orElseThrow(()->new APIException("Product not found"));
        ProductImage image=productImageRepository.findByImageIdAndProduct_ProductId(imageId,productId).orElseThrow(()->new APIException("Image not found"));

        image.setIsPrimary(false);
        boolean wasPrimary = image.getIsPrimary();


        product.getImages().remove(image);
        productRepository.save(product);


        if (wasPrimary) {
            List<ProductImage> remaining = productImageRepository.findAllByProduct_ProductId(productId);
            if (!remaining.isEmpty()) {
                ProductImage newPrimary = remaining.get(0);
                newPrimary.setIsPrimary(true);
                productImageRepository.save(newPrimary);
            }
        }
        return "Image deleted successfully";
    }
}
