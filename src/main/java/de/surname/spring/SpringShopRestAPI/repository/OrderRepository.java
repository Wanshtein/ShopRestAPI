package de.surname.spring.SpringShopRestAPI.repository;

import de.surname.spring.SpringShopRestAPI.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUserId(int userId);

    Optional<Order> findByIdAndUserId(int id, int userId);
}
