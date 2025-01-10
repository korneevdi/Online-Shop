package ag.shop.manager.config;

import ag.shop.manager.client.ProductsRestClientImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ClientBean {

    @Bean
    public ProductsRestClientImpl productsRestClient(
            @Value("${shop.services.catalogue.uri:http://localhost:8081}") String catalogueBaseUri) {
        return new ProductsRestClientImpl(RestClient.builder()
                .baseUrl(catalogueBaseUri)
                .build());
    }
}
