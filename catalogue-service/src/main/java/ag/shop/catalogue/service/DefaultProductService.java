package ag.shop.catalogue.service;

import ag.shop.catalogue.entity.Product;
import ag.shop.catalogue.entity.ProductImage;
import ag.shop.catalogue.repository.ProductImageRepository;
import ag.shop.catalogue.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DefaultProductService implements ProductService {

    private final ProductRepository productRepository;

    private final ProductImageRepository productImageRepository;

    @Override
    public Iterable<Product> findAllProducts(String filter) {
        if (filter != null && !filter.isBlank()) {
            return this.productRepository.findAllByTitleLikeIgnoreCase("%" + filter + "%");
        } else {
            return this.productRepository.findAll();
        }
    }

    @Override
    @Transactional
    public Product createProduct(String title, String description, List<String> imageUrls) {

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
        return this.productRepository.findById(productId);
    }

    @Override
    @Transactional
    public void updateProduct(Integer id, String title, String description, List<String> imageUrls) {
        this.productRepository.findById(id).ifPresentOrElse(product -> {

            List<ProductImage> oldImages = new ArrayList<>(product.getProductImages());
            List<String> oldImageUrls = oldImages.stream()
                    .map(ProductImage::getImageUrl)
                    .toList();

            product.setTitle(title);
            product.setDescription(description);

            // Check whether the link list changed
            if (!new HashSet<>(oldImageUrls).equals(new HashSet<>(imageUrls))) {

                // Remove only the images that absent in new list
                oldImages.stream()
                        .filter(img -> !imageUrls.contains(img.getImageUrl()))
                        .forEach(productImageRepository::delete);

                product.getProductImages().removeIf(img -> !imageUrls.contains(img.getImageUrl()));

                // Add new images (only those that were not there before)
                List<ProductImage> newImages = imageUrls.stream()
                        .filter(url -> !oldImageUrls.contains(url))
                        .map(url -> new ProductImage(null, product, url))
                        .toList();

                product.getProductImages().addAll(newImages);
            }

            productRepository.save(product);
        }, () -> {
            throw new NoSuchElementException();
        });
    }

    @Override
    @Transactional
    public void deleteProduct(Integer id) {
        this.productRepository.deleteById(id);
    }
}
