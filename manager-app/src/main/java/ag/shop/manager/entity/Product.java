package ag.shop.manager.entity;

import java.util.List;

public record Product(int id, String title, String description, List<String> imageUrls) {
}
