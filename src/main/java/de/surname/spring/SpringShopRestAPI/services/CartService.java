package de.surname.spring.SpringShopRestAPI.services;

import de.surname.spring.SpringShopRestAPI.models.Cart;
import de.surname.spring.SpringShopRestAPI.models.CartItem;
import de.surname.spring.SpringShopRestAPI.models.User;
import de.surname.spring.SpringShopRestAPI.repository.CartRepository;
import de.surname.spring.SpringShopRestAPI.util.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final UserService userService;

    @Autowired
    public CartService(CartRepository cartRepository, UserService userService) {
        this.cartRepository = cartRepository;
        this.userService = userService;
    }

    /** GET cart or CREATE if not exist*/
    public Cart getOrCreateCurrentUserCart() {
        User currentUser = userService.getCurrentUser();
        return cartRepository.findByUserId(currentUser.getId()).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(currentUser);
            newCart.setCreatedAt(LocalDateTime.now());
            return cartRepository.save(newCart);
        });
    }

    /**ADD item in cart*/
    public Cart addItemToCurrentUserCart(CartItem cartItem) {
        Cart cart = getOrCreateCurrentUserCart();
        cartItem.setCart(cart);
        cart.getItems().add(cartItem);
        cart.setUpdatedAt(LocalDateTime.now());
        return cartRepository.save(cart);
    }

    /** GET cart current user*/
    public Cart findCurrentUserCart() {
        User currentUser = userService.getCurrentUser();
        System.out.println(currentUser.getUsername());
        return cartRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for current user"));
    }

    /** SAVE or Update cart current user*/
    public Cart saveCurrentUserCart(Cart cart) {
        User currentUser = userService.getCurrentUser();
        cart.setUser(currentUser);
        cart.setCreatedAt(LocalDateTime.now());
        return cartRepository.save(cart);
    }

    /** DEll cart  current user*/
    public void deleteCurrentUserCart() {
        Cart cart = findCurrentUserCart();
        cartRepository.delete(cart);
    }

    /**GET ALL carts for ADMIN*/
    @PreAuthorize("hasRole('ADMIN')")
    public List<Cart> findAllCarts() {
        return cartRepository.findAll();
    }

    /**GET cart by id for ADMIN*/
    @PreAuthorize("hasRole('ADMIN')")
    public Cart findCartById(int id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
    }

    /**DELL cart by id for ADMIN*/
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCartById(int id) {
        Cart cart = findCartById(id);
        cartRepository.delete(cart);
    }

}
