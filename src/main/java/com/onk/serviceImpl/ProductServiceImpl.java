package com.onk.serviceImpl;

import com.onk.component.MessageService;
import com.onk.core.fileSystem.StorageService;
import com.onk.core.results.*;
import com.onk.dto.ImageDto;
import com.onk.dto.ProductDto;
import com.onk.model.Currency;
import com.onk.model.Image;
import com.onk.model.Product;
import com.onk.repository.CategoryRepository;
import com.onk.repository.ImageRepository;
import com.onk.repository.ProductRepository;
import com.onk.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ImageRepository imageRepository;

    private final CategoryRepository categoryRepository;

    private final StorageService storageService;

    private final MessageService messageService;

    @Value("${upload.image.limit.per.saved}")
    private String limit;

    @Override
    public Result addProduct(ProductDto productDto) {
        try{
            if(productRepository.findByName(productDto.getName()).isPresent() ||
                productRepository.findByTitle(productDto.getTitle()).isPresent()){
                return new ErrorResult(
                        messageService.getMessage("product.reInsert.error.message", new Object[]{productDto.getName()})
                );
            }
            if(categoryRepository.findByNameIgnoreCase(productDto.getCategoryName()).isEmpty()){
                return new ErrorResult(
                        messageService.getMessage("category.select.error.message", new Object[]{productDto.getCategoryName()})
                );
            }
            Product product = this.convertToProduct(productDto);
            this.productRepository.save(product);
            return new SuccessResult(
                    messageService.getMessage("product.insert.success.message", new Object[]{productDto.getTitle()})
            );
        }catch (Exception e){
            return new ErrorResult(
                    messageService.getMessage("product.insert.error.message", new Object[]{productDto.getTitle()})
            );
        }
    }

    @Override
    public Result addImagesToProduct(MultipartFile[] images, Long id) {
        try{
            if (images == null) {
                return new ErrorResult(
                        messageService.getMessage("file.empty.message", null)
                );
            }
            var product = productRepository.findById(id).orElseThrow(EntityNotFoundException::new);
            Set<Image> imageList = product.getImages();
            StringBuilder message = new StringBuilder();
            for (MultipartFile image : images) {
                String originalFilename = image.getOriginalFilename();
                if (imageRepository.existsByPath(originalFilename)) {
                    message.append(messageService.getMessage("image.hasAlready.message", new Object[]{image.getOriginalFilename()}));
                    Image image1 = imageRepository.findByPath(image.getOriginalFilename()).orElse(null);
                    if(image1 == null){
                        continue;
                    }
                    if (imageList.contains(image1)) {
                        message.append((messageService.getMessage("image.hasAlready.relation.message", new Object[]{image1.getPath()})));
                        log.info(messageService.getMessage("image.hasAlready.relation.message", new Object[]{image1.getPath()}));
                        continue;
                    }
                    if(imageList.size() < Integer.parseInt(limit)){
                        imageList.add(image1);
                    }else{
                        message.append((messageService.getMessage("image.upload.count.limit.message", new Object[]{image1.getPath()})));
                    }
                    continue;
                }
                log.info(messageService.getMessage("image.uploading.message", new Object[]{image.getOriginalFilename()}));
                message.append(messageService.getMessage("image.uploading.message",
                        new Object[]{image.getOriginalFilename()}));
                if(imageList.size() < Integer.parseInt(limit)){
                    storageService.store(image);
                    Image newImage = Image.builder()
                            .path(image.getOriginalFilename())
                            .build();
                    var savedImage = imageRepository.save(newImage);
                    imageList.add(savedImage);
                    message.append(messageService.getMessage("image.uploaded.true.message", new Object[]{newImage.getPath()}));
                }else{
                    message.append(messageService.getMessage("image.upload.count.limit.message", new Object[]{image.getOriginalFilename()}));
                }
            }
            product.setImages(imageList);
            productRepository.save(product);
            return new SuccessResult(
                message.toString()
            );
        }catch (EntityNotFoundException e){
            return new ErrorResult(
                    messageService.getMessage("product.select.error.message", new Object[]{id})
            );
        }catch (Exception e){
            return new ErrorResult(
                    messageService.getMessage("image.uploadProductImage.false.message", null)
            );
        }
    }

    @Override
    public Result deleteImagesFromProduct(List<ImageDto> dtoImages, Long id) {
        try{
            if(dtoImages.isEmpty()){
                return new ErrorResult(
                        messageService.getMessage("image.required.message", null)
                );
            }
            var product = productRepository.findById(id).orElse(null);
            if(product == null){
                return new ErrorResult(
                        messageService.getMessage("product.select.error.message", new Object[]{id})
                );
            }
            for (ImageDto path : dtoImages) {
                Set<Image> images = product.getImages();
                images.removeIf(image -> image.getPath().equals(path.getPath()));
            }
            productRepository.save(product);

            return new SuccessResult(
                    messageService.getMessage("image.removeToProduct.true.message", null)
            );
        }catch (Exception e){
            return new ErrorResult(
                    messageService.getMessage("error.message", null)
            );
        }
    }

    @Override
    public DataResult<ProductDto> getProductById(Long id) {
        try{
            var product = productRepository.findById(id).orElseThrow(EntityNotFoundException::new);
            var productDto = convertToProductDto(product);
            return new SuccessDataResult<>(
                    productDto,
                    messageService.getMessage("product.select.success.message", new Object[]{product.getTitle()})
            );
        }catch(EntityNotFoundException e){
            return new ErrorDataResult<>(
                    messageService.getMessage("error.not_found", new Object[]{id})
            );
        } catch (Exception e){
            return new ErrorDataResult<>(
                    messageService.getMessage("error.message", null)
            );
        }
    }

    @Override
    public DataResult<ProductDto> getProductByName(String name) {
        try{
            var product = convertToProductDto(productRepository.findByName(name).orElseThrow(EntityNotFoundException::new));
            return new SuccessDataResult<>(
                    product,
                    messageService.getMessage("product.select.success.message", new Object[]{name})
            );
        }catch(EntityNotFoundException e){
            return new ErrorDataResult<>(
                    messageService.getMessage("product.select.error.message", new Object[]{name})
            );
        } catch (Exception e){
            return new ErrorDataResult<>(
                    messageService.getMessage("error.message", null)
            );
        }
    }

    @Override
    public DataResult<ProductDto> getProductByTitle(String title) {
        try{
            var product = convertToProductDto(productRepository.findByTitle(title).orElseThrow(EntityNotFoundException::new));
            return new SuccessDataResult<>(
                    product,
                    messageService.getMessage("product.select.success.message", new Object[]{title})
            );
        }catch (EntityNotFoundException e){
            return new ErrorDataResult<>(
                    messageService.getMessage("product.select.error.message" , new Object[]{title})
            );
        } catch (Exception e){
            return new ErrorDataResult<>(
                    messageService.getMessage("error.message", null)
            );
        }
    }

    @Override
    public DataResult<List<ProductDto>> getProductsByCategoryId(Long categoryId) {
        try{
            if(categoryRepository.findById(categoryId).isEmpty()){
                return new ErrorDataResult<>(
                        messageService.getMessage("category.select.error.message", new Object[]{categoryId})
                );
            }
            var products = productRepository.findProductsByCategoryId(categoryId);
            var productsDto = products.stream().map(ProductServiceImpl::convertToProductDto).toList();
            return new SuccessDataResult<>(
                    productsDto,
                    messageService.getMessage("product.selectByCategory.success.message",null)
            );
        }catch (Exception e){
            return new ErrorDataResult<>(
                    messageService.getMessage("error.message", null)
            );
        }
    }

    @Override
    public DataResult<List<ProductDto>> getAllProducts() {
        try{
            var products = this.productRepository.findAll();
            if(products.isEmpty()){
                return new SuccessDataResult<>(
                        messageService.getMessage("success.null.data.message", null)
                );
            }
            var dtoProducts = products.stream().map(ProductServiceImpl::convertToProductDto).collect(Collectors.toList());
            return new SuccessDataResult<>(
                    dtoProducts,
                    messageService.getMessage("success.data.message", null)
            );
        }catch (Exception e){
            return new ErrorDataResult<>(
                    messageService.getMessage("error.message", null)
            );
        }
    }

    @Override
    public DataResult<List<ProductDto>> getProductsByIsPublishedTrue() {
        try{
            var products = productRepository.findProductsByIsPublishedTrue();
            var productsDto = products.stream().map(ProductServiceImpl::convertToProductDto).toList();
            return new SuccessDataResult<>(
                    productsDto,
                    messageService.getMessage("product.selectIsPublished.true.message", null)
            );
        }catch (Exception e){
            return new ErrorDataResult<>(
                    messageService.getMessage("error.message", null)
            );
        }
    }

    @Override
    public DataResult<List<ProductDto>> getProductsByIsPublishedFalse() {
        try{
            var products = productRepository.findProductsByIsPublishedFalse();
            var productsDto = products.stream().map(ProductServiceImpl::convertToProductDto).toList();
            return new SuccessDataResult<>(
                    productsDto,
                    messageService.getMessage("product.selectIsPublished.false.message", null)
            );
        }catch (Exception e){
            return new ErrorDataResult<>(
                    messageService.getMessage("error.message", null)
            );
        }
    }

    @Override
    public Result updateProduct(ProductDto productDto, Long id) {
        try{
            var productData = productRepository.findById(id).orElse(null);
            if(productData == null){
                return new ErrorResult(
                        messageService.getMessage("error.not_found", null)
                );
            }
            productRepository.save(updateProductFields(productData, productDto));
            return new SuccessResult(
                    messageService.getMessage("product.update.success.message", new Object[]{productDto.getTitle()})
            );
        }catch (Exception e){
            return new ErrorResult(
                    messageService.getMessage("error.message", null)
            );
        }
    }

    @Override
    public Result deleteProduct(Long id) {
        try{
            this.productRepository.deleteById(id);
            return new SuccessResult(
                    messageService.getMessage("product.delete.success.message", new Object[]{id})
            );
        }catch (Exception e){
            return new ErrorResult(
                    messageService.getMessage("error.message", null)
            );
        }
    }

    @Override
    public Result changeProductPublish(Long id) {
        try{
            var product = productRepository.findById(id).orElseThrow(EntityNotFoundException::new);
            productRepository.changeProductPublish(id, !product.getIsPublished());
            return new SuccessResult(
                    messageService.getMessage("product.change.isPublish.success.message", new Object[]{product.getTitle()})
            );
        }catch (EntityNotFoundException e){
            return new ErrorResult(
                    messageService.getMessage("error.not_found", new Object[]{id})
            );
        }
        catch (Exception e){
            return new ErrorResult(
                    messageService.getMessage("error.message", null)
            );
        }
    }

    private Product convertToProduct(ProductDto productDto){
        var category = categoryRepository.findByNameIgnoreCase(productDto.getCategoryName()).orElse(null);
        return Product.builder()
                .name(productDto.getName())
                .title(productDto.getTitle())
                .description(productDto.getDescription())
                .price(productDto.getPrice())
                .purchasePrice(productDto.getPurchasePrice())
                .quantity(productDto.getQuantity())
                .currency(Currency.TL)
                .isPublished(false)
                .category(category)
                .images(productDto.getProductImages())
                .build();
    }

    private static ProductDto convertToProductDto(Product product){
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .title(product.getTitle())
                .description(product.getDescription())
                .price(product.getPrice())
                .purchasePrice(product.getPurchasePrice())
                .quantity(product.getQuantity())
                .currency(product.getCurrency())
                .categoryName(product.getCategory().getName())
                .productImages(product.getImages())
                .build();
    }

    private Product updateProductFields(Product product, ProductDto productDto) {
        if(productDto.getName() != null){
            product.setName(productDto.getName());
        }
        if (productDto.getTitle() != null) {
            product.setTitle(productDto.getTitle());
        }
        if (productDto.getDescription() != null) {
            product.setDescription(productDto.getDescription());
        }
        if (productDto.getPrice() != null) {
            product.setPrice(productDto.getPrice());
        }
        return product;
    }
}
