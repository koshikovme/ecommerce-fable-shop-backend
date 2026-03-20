package advanceddatabases.assignment.fableshopbackend.controller;

import advanceddatabases.assignment.fableshopbackend.model.Product;
import advanceddatabases.assignment.fableshopbackend.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam String query) {
        return productService.findByNameContainingIgnoreCase(query);
    }


    @GetMapping
    public List<Product> getAll() {
        return productService.getAllProducts();
    }

    @GetMapping("/category/{category}")
    public List<Product> getByCategory(@PathVariable String category) {
        return productService.getByCategory(category);
    }

    @GetMapping("/collection/{collection}")
    public List<Product> getByCollection(@PathVariable String collection) {
        return productService.getByCollection(collection);
    }

    @PostMapping
    public Product create(@RequestBody Product product) {
        return productService.save(product);
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable String id) {
        return productService.getById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteProductById(@PathVariable String id) {
        productService.deleteProductById(id);
    }
}
