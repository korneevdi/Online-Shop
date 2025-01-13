package ag.shop.catalogue.service;

import ag.shop.catalogue.entity.Product;
import ag.shop.catalogue.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultProductService implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Iterable<Product> findAllProducts() {
        return this.productRepository.findAll();
    }

    @Override
    @Transactional
    public Product createProduct(String title, String description) {
        return this.productRepository.save(new Product(null, title, description));
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
