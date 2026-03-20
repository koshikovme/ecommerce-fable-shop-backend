package advanceddatabases.assignment.fableshopbackend.repository;

import advanceddatabases.assignment.fableshopbackend.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByCategoryIgnoreCase(String category);
    List<Product> findByCollectionIgnoreCase(String collection);
    List<Product> findByNameContainingIgnoreCase(String name);
    Optional<Product> findById(String id);
}