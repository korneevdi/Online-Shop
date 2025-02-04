package ag.shop.manager.controller.payload;

import java.util.List;

public record NewProductPayload(String title, String description, List<String> imageUrls) {
}
