package advanceddatabases.assignment.fableshopbackend.controller;

import advanceddatabases.assignment.fableshopbackend.model.Product;
import advanceddatabases.assignment.fableshopbackend.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/recommendations")
@RequiredArgsConstructor
public class RecommendationController {
    private final RecommendationService recommendationService;

    @GetMapping("/{userId}")
    public List<Product> getRecommendations(@PathVariable String userId) {
        return recommendationService.recommendForUser(userId);
    }


    @GetMapping("/{userId}/benchmark")
    public Map<String, Object> benchmark(@PathVariable String userId) {
        long start = System.currentTimeMillis();
        List<Product> recs = recommendationService.recommendForUser(userId);
        long end = System.currentTimeMillis();

        return Map.of(
                "executionTimeMs", (end - start),
                "recommendationsCount", recs.size()
        );
    }

}
