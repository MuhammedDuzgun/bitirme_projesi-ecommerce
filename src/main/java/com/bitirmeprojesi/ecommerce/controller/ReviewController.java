package com.bitirmeprojesi.ecommerce.controller;

import com.bitirmeprojesi.ecommerce.exception.ProductException;
import com.bitirmeprojesi.ecommerce.model.Product;
import com.bitirmeprojesi.ecommerce.model.Review;
import com.bitirmeprojesi.ecommerce.model.User;
import com.bitirmeprojesi.ecommerce.request.CreateReviewRequest;
import com.bitirmeprojesi.ecommerce.response.ApiResponse;
import com.bitirmeprojesi.ecommerce.service.IProductService;
import com.bitirmeprojesi.ecommerce.service.IReviewService;
import com.bitirmeprojesi.ecommerce.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ReviewController {

    private final IReviewService reviewService;
    private final IUserService userService;
    private final IProductService productService;

    @GetMapping("/{productId}/reviews")
    public ResponseEntity<List<Review>> getReviewsByProductId(@PathVariable("productId") Long productId) {
        List<Review> reviews = reviewService.getReviewByProductId(productId);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/{productId}/reviews")
    public ResponseEntity<Review> writeReview(@RequestHeader("Authorization") String jwt,
                                              @RequestBody CreateReviewRequest request,
                                              @PathVariable Long productId) throws ProductException {
        User user = userService.findUserByJwtToken(jwt);
        Product product = productService.getProductById(productId);
        Review review = reviewService.createReview(request, user, product);
        return ResponseEntity.ok(review);
    }

    @PatchMapping("/reviews/{reviewId}")
    public ResponseEntity<Review> updateReview(@RequestHeader("Authorization") String jwt,
                                               @RequestBody CreateReviewRequest request,
                                               @PathVariable("reviewId") Long reviewId) throws ProductException {
        User user = userService.findUserByJwtToken(jwt);
        Review review = reviewService.updateReview(reviewId,
                request.getReviewText(),
                request.getReviewRating(),
                user.getId());
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResponse> deleteReview(@RequestHeader("Authorization") String jwt,
                                                    @PathVariable("reviewId") Long reviewId) {
        User user = userService.findUserByJwtToken(jwt);
        reviewService.deleteReview(reviewId, user.getId());

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Review deleted");

        return ResponseEntity.ok(apiResponse);
    }

}
