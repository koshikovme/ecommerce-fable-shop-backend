package advanceddatabases.assignment.fableshopbackend.repository;

import advanceddatabases.assignment.fableshopbackend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);

    // UserRepository.java
    List<User> findByLikedProductIdsContains(String productId);
    List<User> findByPurchasedProductsProductId(String productId);

}
