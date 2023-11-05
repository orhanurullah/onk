package com.onk.service;

import com.onk.core.results.DataResult;
import com.onk.core.results.Result;
import com.onk.dto.ImageDto;
import com.onk.dto.ProductDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    Result addProduct(ProductDto productDto);

    DataResult<ProductDto> getProductById(Long id);

    DataResult<ProductDto> getProductByName(String name);

    DataResult<ProductDto> getProductByTitle(String title);

    DataResult<List<ProductDto>> getProductsByCategoryId(Long categoryId);

    DataResult<List<ProductDto>> getAllProducts();

    DataResult<List<ProductDto>> getProductsByIsPublishedTrue();

    DataResult<List<ProductDto>> getProductsByIsPublishedFalse();

    Result updateProduct(ProductDto productDto, Long id);

    Result deleteProduct(Long id);

    Result changeProductPublish(Long id);

    Result addImagesToProduct(MultipartFile[] images, Long id);

    Result deleteImagesFromProduct(List<ImageDto> dtoImages, Long id);

}
