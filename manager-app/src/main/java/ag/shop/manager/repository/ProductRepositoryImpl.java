package ag.shop.manager.repository;

import ag.shop.manager.entity.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final List<Product> products = Collections.synchronizedList(new ArrayList<>());

    private ProductRepositoryImpl() {
        for(int i = 1; i < 4; i++) {
            this.products.add(new Product(i, "Good №%d".formatted(i), "Description od the good №%d".formatted(i)));
        }
    }

    @Override
    public List<Product> findAll() {
        return Collections.unmodifiableList(this.products);
    }
}
