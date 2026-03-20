package advanceddatabases.assignment.fableshopbackend;


import advanceddatabases.assignment.fableshopbackend.model.Product;
import advanceddatabases.assignment.fableshopbackend.model.User;
import advanceddatabases.assignment.fableshopbackend.repository.ProductRepository;
import advanceddatabases.assignment.fableshopbackend.repository.UserRepository;
import advanceddatabases.assignment.fableshopbackend.service.RecommendationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class RecommendationQualityTest {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private RecommendationService recommendationService;

    private User targetUser;

    @BeforeEach
    void setup() {
        userRepo.deleteAll();
        productRepo.deleteAll();

        // --- создаем продукты и сохраняем ---
        List<Product> products = productRepo.saveAll(List.of(
                new Product(null, "Classic Jacket", "warm", "jackets", "klassik", 100.0, null),
                new Product(null, "Sport Shorts", "light", "shorts", "supersport", 70.0, null),
                new Product(null, "Elegant Shirt", "white", "shirts", "klassik", 80.0, null),
                new Product(null, "Denim Jeans", "blue", "jeans", "neaven", 120.0, null),
                new Product(null, "Leather Jacket", "brown", "jackets", "design", 150.0, null)
        ));

        // --- создаем пользователей с реальными id продуктов ---
        User userA = User.builder()
                .username("Alice")
                .email("a@test.com")
                .password("123")
                .likedProductIds(List.of(products.get(0).getId(), products.get(1).getId()))
                .build();

        User userB = User.builder()
                .username("Bob")
                .email("b@test.com")
                .password("123")
                .likedProductIds(List.of(
                        products.get(0).getId(), // Classic Jacket
                        products.get(1).getId(), // Sport Shorts
                        products.get(2).getId()  // Elegant Shirt
                ))
                .build();

        User userC = User.builder()
                .username("Charlie")
                .email("c@test.com")
                .password("123")
                .likedProductIds(List.of(
                        products.get(0).getId(), // Classic Jacket
                        products.get(3).getId(), // Denim Jeans
                        products.get(4).getId()  // Leather Jacket
                ))
                .build();


        targetUser = userRepo.save(userA);
        userRepo.saveAll(List.of(userB, userC));
    }


    @Test
    public void testRecommendationPrecisionRecallF1() {
        // Эмулируем, что пользователь купил Shirt и Jeans (которые могли быть рекомендованы)
        List<String> groundTruth = List.of(
                productRepo.findAll().get(2).getId(), // Shirt
                productRepo.findAll().get(3).getId()  // Jeans
        );

        List<Product> recs = recommendationService.recommendForUser(targetUser.getId());

        Set<String> recIds = recs.stream().map(Product::getId).collect(Collectors.toSet());
        Set<String> truthSet = new HashSet<>(groundTruth);

        int tp = (int) truthSet.stream().filter(recIds::contains).count();
        int fp = recIds.size() - tp;
        int fn = truthSet.size() - tp;

        double precision = tp / (double) (tp + fp + 1e-9);
        double recall = tp / (double) (tp + fn + 1e-9);
        double f1 = 2 * precision * recall / (precision + recall + 1e-9);

        System.out.printf("""
            === Recommendation Quality ===
            TP=%d, FP=%d, FN=%d
            Precision=%.3f, Recall=%.3f, F1=%.3f
            ==============================
            """, tp, fp, fn, precision, recall, f1);

        if (!recs.isEmpty()) {
            assertTrue(precision >= 0.3 || recall >= 0.3,
                    "Precision/Recall должны быть не ниже 0.3 при наличии данных");
        }
    }

}
