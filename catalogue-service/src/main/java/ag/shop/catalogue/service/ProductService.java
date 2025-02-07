package ag.shop.catalogue.service;

import ag.shop.catalogue.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Iterable<Product> findAllProducts(String filter);

    Product createProduct(String title, String description, List<String> imageUrls);

    Optional<Product> findProduct(int productId);

    void updateProduct(Integer id, String title, String description, List<String> imageUrls);

    void deleteProduct(Integer id);
}
