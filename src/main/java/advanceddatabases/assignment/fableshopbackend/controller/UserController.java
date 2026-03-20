package advanceddatabases.assignment.fableshopbackend.controller;

import advanceddatabases.assignment.fableshopbackend.model.Purchase;
import advanceddatabases.assignment.fableshopbackend.model.User;
import advanceddatabases.assignment.fableshopbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepo;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userRepo.save(user);
    }


    @PostMapping("/login")
    public User login(@RequestBody User credentials) {
        return userRepo.findAll().stream()
                .filter(u -> u.getEmail().equals(credentials.getEmail())
                        && u.getPassword().equals(credentials.getPassword()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable String id) {
        return userRepo.findById(id).orElseThrow();
    }



    @PutMapping("/{userId}/like/{productId}")
    public User toggleLike(@PathVariable String userId, @PathVariable String productId) {
        User user = userRepo.findById(userId).orElseThrow();

        List<String> likes = user.getLikedProductIds();
        if (likes == null) {
            likes = new ArrayList<>();
        }

        // Idempotency
        likes = new ArrayList<>(new HashSet<>(likes));

        if (likes.contains(productId)) {
            likes.remove(productId);
        } else {
            likes.add(productId);
        }

        user.setLikedProductIds(likes);
        return userRepo.save(user);
    }

    @PostMapping("/{userId}/purchase")
    public User purchaseProduct(@PathVariable String userId, @RequestBody Purchase purchase) {
        User user = userRepo.findById(userId).orElseThrow();

        List<Purchase> purchases = user.getPurchasedProducts() != null ? user.getPurchasedProducts() : new ArrayList<>();
        purchases.add(purchase);

        user.setPurchasedProducts(purchases);
        return userRepo.save(user);
    }

}
