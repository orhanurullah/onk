package com.onk.api.admin;

import com.onk.core.utils.RouteConstants;
import com.onk.dto.ImageDto;
import com.onk.dto.ProductDto;
import com.onk.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(RouteConstants.adminBaseRoute + RouteConstants.productRoute)
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping(RouteConstants.productFindAllRoute)
    public ResponseEntity<?> getAllProducts(){
        return ResponseEntity.ok(this.productService.getAllProducts());
    }

    @GetMapping(RouteConstants.productFindByIdRoute)
    public ResponseEntity<?> getProductById(@RequestParam("id") Long id){
        return ResponseEntity.ok(this.productService.getProductById(id));
    }

    @GetMapping(RouteConstants.productFindByNameRoute)
    public ResponseEntity<?> getProductByName(@RequestParam("name") String name){
        return ResponseEntity.ok(this.productService.getProductByName(name));
    }

    @GetMapping(RouteConstants.productFindByTitleRoute)
    public ResponseEntity<?> getProductByTitle(@RequestParam("title") String title){
        return ResponseEntity.ok(this.productService.getProductByTitle(title));
    }
    @PostMapping(RouteConstants.productChangePublishRoute)
    public ResponseEntity<?> changeProductPublish(@PathVariable Long id){
        return ResponseEntity.ok(this.productService.changeProductPublish(id));
    }

    @PostMapping(RouteConstants.productCreateRoute)
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDto productDto){
        return ResponseEntity.ok(this.productService.addProduct(productDto));
    }

    @PostMapping(RouteConstants.productUpdateRoute)
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDto productDto){
        return ResponseEntity.ok(this.productService.updateProduct(productDto, id));
    }

    @PostMapping(RouteConstants.productAddImageRoute)
    public ResponseEntity<?> addImagesToProduct(@PathVariable Long id, MultipartFile[] images){
        return ResponseEntity.ok(this.productService.addImagesToProduct(images, id));
    }
    @PostMapping(RouteConstants.productDeleteRoute)
    public ResponseEntity<?> deleteProduct(@PathVariable Long id){
        return ResponseEntity.ok(this.productService.deleteProduct(id));
    }

    @PostMapping(RouteConstants.productDeleteImageRoute)
    public ResponseEntity<?> deleteImageFromProduct(@PathVariable Long id, @Valid @RequestBody List<ImageDto> imageDto){
        return ResponseEntity.ok(this.productService.deleteImagesFromProduct(imageDto, id));
    }

    @GetMapping(RouteConstants.productFindAllIsPublishedRoute)
    public ResponseEntity<?> allPublishedProducts(){
        return ResponseEntity.ok(this.productService.getProductsByIsPublishedTrue());
    }

    @GetMapping(RouteConstants.productFindAllNotPublishedRoute)
    public ResponseEntity<?> allNotPublishedProducts(){
        return ResponseEntity.ok(this.productService.getProductsByIsPublishedFalse());
    }
    @GetMapping(RouteConstants.productFindByCategoryNameRoute)
    public ResponseEntity<?> findProductsByCategory(@PathVariable Long id){
        return ResponseEntity.ok(this.productService.getProductsByCategoryId(id));
    }

}
