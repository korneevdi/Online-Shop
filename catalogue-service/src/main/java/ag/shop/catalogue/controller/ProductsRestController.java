package ag.shop.catalogue.controller;

import ag.shop.catalogue.controller.payload.NewProductPayload;
import ag.shop.catalogue.entity.Product;
import ag.shop.catalogue.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("catalogue-api/products")
public class ProductsRestController {

    private final ProductService productService;

    @GetMapping
    public Iterable<Product> findProducts(@RequestParam(name = "filter", required = false) String filter) {
        return this.productService.findAllProducts(filter);
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody NewProductPayload payload,
                                           BindingResult bindingResult,
                                           UriComponentsBuilder uriComponentsBuilder)
            throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        }

        // Create a product with an image
        Product product = this.productService
                .createProduct(payload.title(), payload.description(), payload.imageUrls());

        return ResponseEntity
                .created(uriComponentsBuilder
                        .replacePath("/catalogue-api/products/{productId}")
                        .build(Map.of("productId", product.getId())))
                .body(product);
    }
}
