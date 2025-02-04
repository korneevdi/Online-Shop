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
        Product product = new Product();
        product.setTitle(title);
        product.setDescription(description);

        // Преобразуем ссылки в объекты ProductImage
        if (imageUrls != null && !imageUrls.isEmpty()) {
            List<ProductImage> images = imageUrls.stream()
                    .map(url -> new ProductImage(null, product, url))
                    .toList();
            product.setImageUrls(images);
        }

        return this.productRepository.save(product);
    }

    @Override
    public Optional<Product> findProduct(int productId) {
        return this.productRepository.findById(productId);
    }

    @Override
    @Transactional
    public void updateProduct(Integer id, String title, String description) {
        this.productRepository.findById(id)
                .ifPresentOrElse(product -> {
                    product.setTitle(title);
                    product.setDescription(description);
                }, () -> {throw new NoSuchElementException();
                });
    }

    @Override
    @Transactional
    public void deleteProduct(Integer id) {
        this.productRepository.deleteById(id);
    }
}
