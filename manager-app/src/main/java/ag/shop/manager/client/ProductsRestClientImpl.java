package ag.shop.manager.client;

import ag.shop.manager.controller.payload.NewProductPayload;
import ag.shop.manager.controller.payload.UpdateProductPayload;
import ag.shop.manager.entity.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
public class ProductsRestClientImpl implements ProductsRestClient {

    private static final ParameterizedTypeReference<List<Product>> PRODUCTS_TYPE_REFERENCE =
            new ParameterizedTypeReference<>() {
            };

    private final RestClient restClient;

    @Override
    public List<Product> findAllProducts(String filter) {
        return this.restClient
                .get()
                .uri("/catalogue-api/products?filter={filter}", filter)
                .retrieve()
                .body(PRODUCTS_TYPE_REFERENCE);
    }

    @Override
    public Product createProduct(String title, String description) {
        try{
            return this.restClient
                    .post()
                    .uri("/catalogue-api/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new NewProductPayload(title, description))
                    .retrieve()
                    .body(Product.class);
        } catch (HttpClientErrorException exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<FieldError>) problemDetail.getProperties().get("errors"));
        }
    }

    @Override
    public Optional<Product> findProduct(int productId) {
        try{
            return Optional.of(this.restClient
                    .get()
                    .uri("/catalogue-api/products/{productId}", productId)
                    .retrieve()
                    .body(Product.class));
        } catch(HttpClientErrorException.NotFound exception) {
            return Optional.empty();
        }
    }

    @Override
    public void updateProduct(int productId, String title, String description) throws JsonProcessingException {
        try{
            this.restClient
                    .patch()
                    .uri("/catalogue-api/products/{productId}", productId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new UpdateProductPayload(title, description))
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException exception) {
            if (exception.getStatusCode() == HttpStatus.BAD_REQUEST) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(exception.getResponseBodyAsString());

                // Преобразуем JSON-ошибки в список FieldError
                List<FieldError> fieldErrors = new ArrayList<>();
                for (JsonNode errorNode : rootNode.get("errors")) {
                    String field = errorNode.get("field").asText();
                    String message = errorNode.get("message").asText();
                    fieldErrors.add(new FieldError("product", field, message));
                }

                throw new BadRequestException(fieldErrors);
            } else {
                throw exception;
            }
        }
    }

    @Override
    public void deleteProduct(int productId) {
        try{
            this.restClient
                    .delete()
                    .uri("/catalogue-api/products/{productId}", productId)
                    .retrieve()
                    .toBodilessEntity();
        } catch(HttpClientErrorException.NotFound exception) {
            throw new NoSuchElementException(exception);
        }
    }
}
