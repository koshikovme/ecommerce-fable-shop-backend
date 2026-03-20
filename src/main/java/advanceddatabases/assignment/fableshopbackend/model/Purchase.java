package advanceddatabases.assignment.fableshopbackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Purchase {
    private String productId;
    private String color;
    private String size;
}
