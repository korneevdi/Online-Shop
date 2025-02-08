package ag.shop.catalogue.service;

import ag.shop.catalogue.entity.Product;
import ag.shop.catalogue.entity.ProductImage;
import ag.shop.catalogue.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultProductService implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Iterable<Product> findAllProducts(String filter) {
        if(filter != null && !filter.isBlank()) {
            return this.productRepository.findAllByTitleLikeIgnoreCase("%" + filter + "%");
        } else {
            return this.productRepository.findAll();
        }
    }

    @Override
    @Transactional
    public Product createProduct(String title, String description, List<String> imageUrls) {
        System.out.println("###### DefaultProductService module catalogue-service ######");
        System.out.println("Received title: " + title);
        System.out.println("Received description: " + description);
        System.out.println("Received imageUrls:");
        if(imageUrls != null) {
            for (String url : imageUrls) {
                System.out.println(url);
            }
        } else {
            System.out.println("null");
        }

        Product product = new Product();
        product.setTitle(title);
        product.setDescription(description);

        // Transform links into ProductImage objects
        if (imageUrls != null && !imageUrls.isEmpty()) {
            List<ProductImage> images = imageUrls.stream()
                    .map(url -> new ProductImage(null, product, url))
                    .toList();
            product.setProductImages(images);
        }

        return this.productRepository.save(product);
    }

    @Override
    public Optional<Product> findProduct(int productId) {
        Optional<Product> product = this.productRepository.findById(productId);
        System.out.println("###### DefaultProductService: Retrieved product ######");
        System.out.println(product);
        return product;
    }

    @Override
    @Transactional
    public void updateProduct(Integer id, String title, String description, List<String> imageUrls) {
        this.productRepository.findById(id)
                .ifPresentOrElse(product -> {
                    System.out.println("###### DefaultProductService: Updating product ######");
                    System.out.println("Product ID: " + id);
                    System.out.println("Old Title: " + product.getTitle());
                    System.out.println("New Title: " + title);
                    System.out.println("Old Description: " + product.getDescription());
                    System.out.println("New Description: " + description);

                    // Log old images before clearing
                    System.out.println("Old Images:");
                    for (ProductImage image : product.getProductImages()) {
                        System.out.println(" - " + image.getImageUrl());
                    }

                    product.setTitle(title);
                    product.setDescription(description);

                    // Clear old images
                    product.getProductImages().clear();

                    // Check if the list of images is empty
                    if(product.getProductImages().isEmpty()) {
                        System.out.println("Images cleared.");
                    } else {
                        for(ProductImage p : product.getProductImages()) {
                            System.out.println(p.getImageUrl());
                        }
                    }

                    if (imageUrls != null && !imageUrls.isEmpty()) {
                        System.out.println("New Images:");
                        List<ProductImage> newImages = imageUrls.stream()
                                .distinct()
                                .map(url -> new ProductImage(null, product, url))
                                .toList();
                        for (ProductImage image : newImages) {
                            System.out.println(" - " + image.getImageUrl());
                        }

                        product.getProductImages().addAll(newImages);
                    } else {
                        System.out.println("No new images received.");
                    }
                }, () -> {throw new NoSuchElementException();
                });
    }

    @Override
    @Transactional
    public void deleteProduct(Integer id) {
        this.productRepository.deleteById(id);
    }
}
