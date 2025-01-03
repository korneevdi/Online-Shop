package ag.shop.manager.repository;

import ag.shop.manager.entity.Product;

import java.util.List;

public interface ProductRepository {

    List<Product> findAll();
}
