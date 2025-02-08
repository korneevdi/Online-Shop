package ag.shop.manager.controller;

import ag.shop.manager.client.BadRequestException;
import ag.shop.manager.client.ProductsRestClient;
import ag.shop.manager.controller.payload.UpdateProductPayload;
import ag.shop.manager.entity.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.FieldError;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("catalogue/products/{productId:\\d+}")
@RequiredArgsConstructor
public class ProductController {

    private final ProductsRestClient productsRestClient;

    private final MessageSource messageSource;

    @ModelAttribute("product")
    public Product product(@PathVariable("productId") int productId) {
        return this.productsRestClient.findProduct(productId)
                .orElseThrow(() -> new NoSuchElementException("catalogue.errors.product.not_found"));
    }

    @GetMapping
    public String getProduct() {
        return "catalogue/products/product";
    }

    @GetMapping("edit")
    public String getProductEditPage() {
        return "catalogue/products/edit";
    }

    @PostMapping("edit")
    public String updateProduct(@ModelAttribute(name = "product", binding = false) Product product,
                                UpdateProductPayload payload,
                                Model model) {
        try {
            System.out.println("###### ManagerApp controller: Updating product ######");
            System.out.println("Product ID: " + product.id());

            List<String> oldImageUrls = product.imageUrls();
            List<String> newImageUrls = payload.imageUrls();

            System.out.println("Old Images:");
            for (String oldImage : oldImageUrls) {
                System.out.println(" - " + oldImage);
            }

            System.out.println("New Images from payload:");
            if (newImageUrls != null) {
                for (String newImage : newImageUrls) {
                    System.out.println(" - " + newImage);
                }
            } else {
                System.out.println("No new images received.");
            }

            if (newImageUrls == null || newImageUrls.equals(oldImageUrls)) {
                newImageUrls = null;
                System.out.println("No image changes detected, sending null.");
            }

            this.productsRestClient.updateProduct(product.id(), payload.title(), payload.description(), newImageUrls);
            return "redirect:/catalogue/products/%d".formatted(product.id());
        } catch (BadRequestException exception) {
            model.addAttribute("payload", payload);
            model.addAttribute("errors", exception.getErrors());
            return "catalogue/products/edit";
        }
    }

    @PostMapping("delete")
    public String deleteProduct(@ModelAttribute("product") Product product) {
        this.productsRestClient.deleteProduct(product.id());
        return "redirect:/catalogue/products/list";
    }

    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(NoSuchElementException exception, Model model,
                                               HttpServletResponse response, Locale locale) {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        model.addAttribute("error",
                this.messageSource.getMessage(exception.getMessage(), new Object[0],
                        exception.getMessage(), locale));
        return "errors/404";
    }
}
