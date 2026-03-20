package advanceddatabases.assignment.fableshopbackend.service;

import advanceddatabases.assignment.fableshopbackend.model.Product;
import advanceddatabases.assignment.fableshopbackend.model.User;
import advanceddatabases.assignment.fableshopbackend.repository.ProductRepository;
import advanceddatabases.assignment.fableshopbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepo;
    private final UserRepository userRepo;

    public List<Product> findByNameContainingIgnoreCase(String query) {
        return productRepo.findByNameContainingIgnoreCase(query);
    }

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public List<Product> getByCategory(String category) {
        return productRepo.findByCategoryIgnoreCase(category);
    }

    public List<Product> getByCollection(String collection) {
        return productRepo.findByCollectionIgnoreCase(collection);
    }

    public Product getById(String id) {
        return productRepo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
    }

    public Product save(Product product) {
        return productRepo.save(product);
    }


    public void deleteProductById(String productId) {
        // 1️⃣ Удаляем товар
        productRepo.deleteById(productId);

        // 2️⃣ Удаляем ссылки из лайков
        List<User> likedUsers = userRepo.findByLikedProductIdsContains(productId);
        for (User u : likedUsers) {
            u.getLikedProductIds().remove(productId);
            userRepo.save(u);
        }

        // 3️⃣ Удаляем из истории покупок
        List<User> purchasedUsers = userRepo.findByPurchasedProductsProductId(productId);
        for (User u : purchasedUsers) {
            if (u.getPurchasedProducts() != null) {
                u.getPurchasedProducts().removeIf(p -> productId.equals(p.getProductId()));
                userRepo.save(u);
            }
        }
    }
}
