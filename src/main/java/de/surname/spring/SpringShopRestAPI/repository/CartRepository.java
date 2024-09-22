package de.surname.spring.SpringShopRestAPI.repository;

import de.surname.spring.SpringShopRestAPI.models.Cart;
import de.surname.spring.SpringShopRestAPI.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findByUserId(Integer userId);

    Optional<Cart> findByUser(User user);
}
