package de.surname.spring.SpringShopRestAPI.controllers;

import de.surname.spring.SpringShopRestAPI.models.Order;
import de.surname.spring.SpringShopRestAPI.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**CREATE order for CURRENT USER*/
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Order createdOrder = orderService.createOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }


    /**GET all orders CURRENT USER*/
    @GetMapping("/my-orders")
    public ResponseEntity<List<Order>> getCurrentUserOrders() {
        return ResponseEntity.ok(orderService.findCurrentUserOrders());
    }


    /**GET Order by id CURRENT USER*/
    @GetMapping("/{id}")
    public ResponseEntity<Order> getCurrentUserOrderById(@PathVariable int id) {
        return ResponseEntity.ok(orderService.findCurrentUserOrderById(id));
    }


    /**DELL order by Id CURRENT USER*/
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCurrentUserOrder(@PathVariable int id) {
        orderService.deleteCurrentUserOrder(id);
        return ResponseEntity.noContent().build();
    }

    /** GET all orders for ADMIN*/
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.findAllOrders());
    }

    /**GET order by Id for ADMIN*/
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable int id) {
        return ResponseEntity.ok(orderService.findOrderById(id));
    }

    /** DELL order by id for ADMIN*/
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deleteOrderById(@PathVariable int id) {
        orderService.deleteOrderById(id);
        return ResponseEntity.noContent().build();
    }
}
