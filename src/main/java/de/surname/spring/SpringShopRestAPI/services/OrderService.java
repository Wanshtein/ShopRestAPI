package de.surname.spring.SpringShopRestAPI.services;

import de.surname.spring.SpringShopRestAPI.models.Order;
import de.surname.spring.SpringShopRestAPI.models.OrderItem;
import de.surname.spring.SpringShopRestAPI.models.User;
import de.surname.spring.SpringShopRestAPI.repository.OrderRepository;
import de.surname.spring.SpringShopRestAPI.util.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserService userService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
    }


    /**CREATE order for current user*/
    public Order createOrder(Order order) {
        User currentUser = userService.getCurrentUser();
        order.setUser(currentUser);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalPrice(calculateTotalPrice(order.getOrderItems()));

        for (OrderItem item : order.getOrderItems()) {
            item.setOrder(order);
        }
        return orderRepository.save(order);
    }
    private Double calculateTotalPrice(List<OrderItem> items) {
        return items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    /**GET ALL orders of current users */
    public List<Order> findCurrentUserOrders() {
        User currentUser = userService.getCurrentUser();
        return orderRepository.findByUserId(currentUser.getId());
    }

    /**GET order by id for CU*/
    public Order findCurrentUserOrderById(int id) {
        User currentUser = userService.getCurrentUser();
        return orderRepository.findByIdAndUserId(id, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found for current user"));
    }


    /**DELL order by id CU*/
    public void deleteCurrentUserOrder(int id) {
        Order order = findCurrentUserOrderById(id);
        orderRepository.delete(order);
    }


    /** GET all orders for ADMIN*/
    @PreAuthorize("hasRole('ADMIN')")
    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    /** get order by id for ADMIN*/
    @PreAuthorize("hasRole('ADMIN')")
    public Order findOrderById(int id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    /**dell order by id for ADMIN*/
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteOrderById(int id) {
        Order order = findOrderById(id);
        orderRepository.delete(order);
    }

}
