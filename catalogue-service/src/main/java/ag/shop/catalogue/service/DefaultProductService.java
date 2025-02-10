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
        if(filter != null && !filter.isBlank()) {
            return this.productRepository.findAllByTitleLikeIgnoreCase("%" + filter + "%");
        } else {
            return this.productRepository.findAll();
        }
    }

    @Override
    @Transactional
    public Product createProduct(String title, String description, List<String> imageUrls) {
        System.out.println("###### DefaultProductService module catalogue-service ######");
        System.out.println("Received title: " + title);
        System.out.println("Received description: " + description);
        System.out.println("Received imageUrls:");
        if(imageUrls != null) {
            for (String url : imageUrls) {
                System.out.println(url);
            }
        } else {
            System.out.println("null");
        }

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
        Optional<Product> product = this.productRepository.findById(productId);
        System.out.println("###### DefaultProductService: Retrieved product ######");
        System.out.println(product);
        return product;
    }

    @Override
    @Transactional
    public void updateProduct(Integer id, String title, String description, List<String> imageUrls) {
        this.productRepository.findById(id).ifPresentOrElse(product -> {
            System.out.println("###### DefaultProductService: Updating product ######");
            System.out.println("Product ID: " + id);

            List<ProductImage> oldImages = new ArrayList<>(product.getProductImages());
            List<String> oldImageUrls = oldImages.stream()
                    .map(ProductImage::getImageUrl)
                    .toList();

            System.out.println("Old Images: " + oldImageUrls);
            System.out.println("New Images from payload: " + imageUrls);

            product.setTitle(title);
            product.setDescription(description);

            // ✅ Проверяем, изменился ли список изображений
            if (!new HashSet<>(oldImageUrls).equals(new HashSet<>(imageUrls))) {
                System.out.println("❗ Changes detected. Updating images...");

                // Удаляем только те изображения, которых нет в новом списке
                oldImages.stream()
                        .filter(img -> !imageUrls.contains(img.getImageUrl())) // Если URL отсутствует в новом списке
                        .forEach(productImageRepository::delete); // Удаляем из БД

                // Оставляем только те изображения, которые есть в новом списке
                product.getProductImages().removeIf(img -> !imageUrls.contains(img.getImageUrl()));

                // Добавляем новые изображения (только те, которых не было ранее)
                List<ProductImage> newImages = imageUrls.stream()
                        .filter(url -> !oldImageUrls.contains(url)) // Фильтруем только новые ссылки
                        .map(url -> new ProductImage(null, product, url))
                        .toList();

                product.getProductImages().addAll(newImages);
            } else {
                System.out.println("✅ No image changes detected. Skipping update.");
            }

            productRepository.save(product); // Сохраняем изменения
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
