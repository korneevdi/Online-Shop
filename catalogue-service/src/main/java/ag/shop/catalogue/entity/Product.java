package ag.shop.catalogue.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "catalogue", name = "t_product")
@ToString(exclude = "productImages")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "c_title")
    @NotNull
    @Size(min = 2, max = 50)
    private String title;

    @Column(name = "c_description")
    @Size(max = 1000)
    private String description;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<ProductImage> productImages; // Храним связи с изображениями

    @Transient
    @JsonProperty("imageUrls")
    public List<String> getImageUrls() {
        if (productImages == null) return Collections.emptyList();
        return productImages.stream()
                .map(ProductImage::getImageUrl) // Достаем imageUrl из ProductImage
                .collect(Collectors.toList());
    }
}
