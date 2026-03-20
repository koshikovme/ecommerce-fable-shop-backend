package advanceddatabases.assignment.fableshopbackend.service;

import advanceddatabases.assignment.fableshopbackend.model.Product;
import advanceddatabases.assignment.fableshopbackend.model.Purchase;
import advanceddatabases.assignment.fableshopbackend.model.User;
import advanceddatabases.assignment.fableshopbackend.repository.ProductRepository;
import advanceddatabases.assignment.fableshopbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final UserRepository userRepo;
    private final ProductRepository productRepo;

    public List<Product> recommendForUser(String userId) {

        User current = userRepo.findById(userId).orElse(null);

        if (current == null) {
            return List.of();
        }

        List<String> currentLikes =
                current.getLikedProductIds() != null
                        ? current.getLikedProductIds()
                        : List.of();

        List<User> others = userRepo.findAll().stream()
                .filter(u -> !u.getId().equals(userId))
                .collect(Collectors.toList());

        Map<String, Integer> similarityCount = new HashMap<>();

        for (User other : others) {

            List<String> otherLikes =
                    other.getLikedProductIds() != null
                            ? other.getLikedProductIds()
                            : List.of();

            List<String> overlap = new ArrayList<>(otherLikes);
            overlap.retainAll(currentLikes);

            if (!overlap.isEmpty()) {
                for (String pid : otherLikes) {
                    if (!currentLikes.contains(pid)) {
                        similarityCount.merge(pid, 1, Integer::sum);
                    }
                }
            }
        }

        List<String> recommendedIds = similarityCount.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        if (recommendedIds.isEmpty() && !currentLikes.isEmpty()) {

            List<Product> liked = productRepo.findAllById(currentLikes);

            Set<String> likedCats = liked.stream()
                    .map(Product::getCategory)
                    .collect(Collectors.toSet());

            recommendedIds = productRepo.findAll().stream()
                    .filter(p -> likedCats.contains(p.getCategory()))
                    .filter(p -> !currentLikes.contains(p.getId()))
                    .limit(3)
                    .map(Product::getId)
                    .collect(Collectors.toList());
        }

        return recommendedIds.isEmpty()
                ? List.of()
                : productRepo.findAllById(recommendedIds);
    }

}
