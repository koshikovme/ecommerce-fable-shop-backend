package advanceddatabases.assignment.fableshopbackend.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "interactions")
public class Interaction {
    @Id
    private String id;
    private String userId;
    private String productId;
    private String type; // view, like, purchase
    private long timestamp;
}
