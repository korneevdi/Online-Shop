package ag.shop.manager.service;

import ag.shop.manager.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> findAllProducts();

    Product createProduct(String title, String description);

    Optional<Product> findProduct(int productId);
}
