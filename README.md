# Fable Shop

**Full-Scale Advanced Frontend SPA (Final Project)**

A feature-rich, architecturally structured Single Page Application built with **Vue 3 + TypeScript**, implementing centralized state management, nested & protected routing, lazy loading, async form validation, performance optimizations, and comprehensive testing.

---

## Project Purpose

This project was developed as part of the *Full-Scale Advanced Frontend Application Development* assignment , which requires:

* Complex SPA architecture
* Centralized state management
* Nested & protected routing
* Lazy loading
* Performance optimization
* Complex form with async validation
* Unit testing coverage

Fable Shop implements all mandatory technical requirements.

---

# Tech Stack

| Layer            | Technology                       |
| ---------------- |----------------------------------|
| Framework        | Vue 3 (Composition API)          |
| Language         | TypeScript                       |
| State Management | Pinia                            |
| Routing          | Vue Router 4                     |
| HTTP             | Axios                            |
| Testing          | Vitest + Cypress                 |
| Build Tool       | Vite                             |
| Backend          | REST API (Spring Boot + MongoDB) |

---

# Architecture Overview

The application follows a modular layered SPA architecture:

```
UI Layer
 ├── Views
 ├── Components
 ├── Layouts

State Layer
 ├── authStore
 ├── clothingStore

Routing Layer
 ├── Public routes
 ├── Protected routes
 ├── Nested routes

API Layer
 ├── Axios services
```

### Data Flow

User Action
→ Component
→ Pinia Store
→ API
→ Store Update
→ Reactive UI Update

This ensures predictable state transitions and centralized business logic.

---

# State Management (Centralized)

Implemented using **Pinia**:

* Global `authStore`
* Global `clothingStore`
* Async actions for:

    * Login / Register
    * Fetch products
    * Like / Purchase
* State persistence via localStorage

This satisfies the centralized state requirement .

---

# Routing

### Nested Routing

```
/account
 ├── /account/overview
 ├── /account/checkout
```

Implemented using layout-based routing.

### Protected Routing

* `meta.requiresAuth`
* Global navigation guard
* Redirect to `/login?redirect=...`

### Lazy Loading

All pages are dynamically imported:

```ts
component: () => import("../views/HomePage.vue")
```

This reduces initial bundle size and improves performance .

---

# Performance Optimizations

* Lazy route loading
* Computed-based filtering
* Debounced async validation
* Centralized state prevents redundant API calls
* Scroll behavior management
* Avoidance of unnecessary re-renders

Optimizations are applied strategically as required .

---

# Complex Form with Async Validation

The Checkout page includes:

* 8+ input fields
* Select dropdowns
* Checkbox agreement
* Synchronous validation
* Asynchronous promo code validation
* Debounce logic
* Disabled submit during validation

Promo code validation:

* API-based
* Fallback mock validation
* Prevents submission during async state

This fulfills the async validation requirement .

---

# Testing

The project includes:

* Unit tests for stores
* Component logic tests
* Integration tests
* End-to-end tests (authentication + checkout flow)

Coverage focuses on:

* Business logic
* Route guards
* Async validation
* State transitions

All tests pass successfully.

---

# Project Structure

```
src/
 ├── assets/
 ├── components/
 ├── layouts/
 ├── router/
 ├── stores/
 ├── views/
 ├── main.ts
public/
 ├── images/
```

Images are served from `/public/images`.

---

# Backend (Spring + Mongo)

The backend runs on Spring Boot with MongoDB on Docker Container.

API:
```java
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
```

To update image paths:
```bash
docker exec -it <mongo_container> mongosh
```

Then:

```js
db.products.updateMany(
  {},
  [
    {
      $set: {
        imageUrls: {
          $map: {
            input: "$imageUrls",
            as: "img",
            in: {
              $replaceOne: {
                input: "$$img",
                find: "/assets/images/",
                replacement: "/images/"
              }
            }
          }
        }
      }
    }
  ]
)
```

---

# Installation & Run

```bash
npm install
npm run dev
```

Production build:

```bash
npm run build
```

---

# CI/CD (Potential)

Example GitHub Actions workflow:

1. Install dependencies
2. Run unit tests
3. Run coverage
4. Build production bundle
5. Deploy

---