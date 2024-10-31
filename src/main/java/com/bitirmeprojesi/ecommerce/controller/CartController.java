package com.bitirmeprojesi.ecommerce.controller;

import com.bitirmeprojesi.ecommerce.exception.ProductException;
import com.bitirmeprojesi.ecommerce.model.Cart;
import com.bitirmeprojesi.ecommerce.model.CartItem;
import com.bitirmeprojesi.ecommerce.model.Product;
import com.bitirmeprojesi.ecommerce.model.User;
import com.bitirmeprojesi.ecommerce.request.AddItemRequst;
import com.bitirmeprojesi.ecommerce.response.ApiResponse;
import com.bitirmeprojesi.ecommerce.service.ICartItemService;
import com.bitirmeprojesi.ecommerce.service.ICartService;
import com.bitirmeprojesi.ecommerce.service.impl.ProductService;
import com.bitirmeprojesi.ecommerce.service.impl.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final ICartService cartService;
    private final ICartItemService cartItemService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Cart> findUserCart(@RequestHeader("Authorization") String jwt) {
        User user = userService.findUserByJwtToken(jwt);
        Cart cart = cartService.findUserCart(user);
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/add")
    public ResponseEntity<CartItem> addItemToCart(@RequestHeader("Authorization") String jwt,
                                                  @RequestBody AddItemRequst requst) throws ProductException {

        User user = userService.findUserByJwtToken(jwt);
        Product product = productService.getProductById(requst.getProductId());

        CartItem item = cartService.addCartItem(user, product, requst.getSize(), requst.getQuantity());

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Item added to cart successfuly");

        return new ResponseEntity<>(item, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/item/{id}")
    public ResponseEntity<ApiResponse> deleteCartItem(@RequestHeader("Authorization") String jwt,
                                                      @PathVariable("id") Long cartItemId) {

        User user = userService.findUserByJwtToken(jwt);
        cartItemService.deleteCartItem(user.getId(), cartItemId);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Item deleted successfully");

        return new ResponseEntity<>(apiResponse, HttpStatus.ACCEPTED);
    }

    @PutMapping("/item/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItem(@RequestHeader("Authorization") String jwt,
                                                   @PathVariable("cartItemId") Long cartItemId,
                                                   @RequestBody CartItem cartItem) {
        User user = userService.findUserByJwtToken(jwt);

        CartItem updatedCartItem = null;
        if (cartItem.getQuantity()>0) {
            updatedCartItem = cartItemService.updateCartItem(user.getId(), cartItemId, cartItem);
        }

        return new ResponseEntity<>(updatedCartItem, HttpStatus.ACCEPTED);
    }

}
