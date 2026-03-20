package advanceddatabases.assignment.fableshopbackend.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "products")
public class Product {
    @Id
    private String id;
    private String name;
    private String description;
    private String category;
    private String collection;
    private double price;
    private String[] imageUrls;
}
