package de.surname.spring.SpringShopRestAPI.controllers;

import de.surname.spring.SpringShopRestAPI.models.Cart;
import de.surname.spring.SpringShopRestAPI.models.CartItem;
import de.surname.spring.SpringShopRestAPI.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class CartController {
    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }


    /** GET cart current user*/
    @GetMapping("/my-cart")
    public ResponseEntity<Cart> getCurrentUserCart() {
        return ResponseEntity.ok(cartService.findCurrentUserCart());
    }

    /** CREATE or UPDATE current user */
    @PostMapping
    public ResponseEntity<Cart> saveCurrentUserCart(@RequestBody Cart cart) {
        return ResponseEntity.ok(cartService.saveCurrentUserCart(cart));
    }

    /** DELL cart current user*/
    @DeleteMapping
    public ResponseEntity<Void> deleteCurrentUserCart() {
        cartService.deleteCurrentUserCart();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/items")
    public ResponseEntity<Cart> addItemToCart(@RequestBody CartItem cartItem) {
        Cart updatedCart = cartService.addItemToCurrentUserCart(cartItem);
        return ResponseEntity.ok(updatedCart);
    }

    /** GET ALL carts for ADMIN*/
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Cart>> getAllCarts() {
        return ResponseEntity.ok(cartService.findAllCarts());
    }

    /** GET cart by Id for ADMIN*/
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCartById(@PathVariable int id) {
        return ResponseEntity.ok(cartService.findCartById(id));
    }

    /** DELL cart by id for ADMIN*/
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCartById(@PathVariable int id) {
        cartService.deleteCartById(id);
        return ResponseEntity.noContent().build();
    }
}
