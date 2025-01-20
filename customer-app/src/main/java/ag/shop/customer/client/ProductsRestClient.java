package ag.shop.customer.client;

import ag.shop.customer.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductsRestClient {

    List<Product> findAllProducts(String filter);

    Optional<Product> findProduct(int productId);
}
