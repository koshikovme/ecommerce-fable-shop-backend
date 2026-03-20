package advanceddatabases.assignment.fableshopbackend.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String username;
    private String email;
    private String password;
    private List<String> likedProductIds;
    private List<Purchase> purchasedProducts;
}
