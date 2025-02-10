package ag.shop.manager.client;

import ag.shop.manager.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductsRestClient {

    List<Product> findAllProducts(String filter);

    Product createProduct(String title, String description, List<String> imageUrls);

    Optional<Product> findProduct(int productId);

    void updateProduct(int productId, String title, String description, List<String> imageUrls);

    void deleteProduct(int productId);
}
