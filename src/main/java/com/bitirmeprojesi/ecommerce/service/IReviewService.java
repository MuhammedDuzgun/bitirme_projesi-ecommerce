package com.bitirmeprojesi.ecommerce.service;

import com.bitirmeprojesi.ecommerce.model.Product;
import com.bitirmeprojesi.ecommerce.model.Review;
import com.bitirmeprojesi.ecommerce.model.User;
import com.bitirmeprojesi.ecommerce.request.CreateReviewRequest;

import java.util.List;

public interface IReviewService {

    Review createReview(CreateReviewRequest request, User user, Product product);
    Review getReviewById(Long id);
    List<Review> getReviewByProductId(Long productId);
    Review updateReview(Long reviewId, String reviewText, double rating, Long userId);
    void deleteReview(Long reviewId, Long userId);

}
