package ag.shop.manager.repository;

import ag.shop.manager.entity.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final List<Product> products = Collections.synchronizedList(new ArrayList<>());
}
