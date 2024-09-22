package de.surname.spring.SpringShopRestAPI.services;

import de.surname.spring.SpringShopRestAPI.models.User;
import de.surname.spring.SpringShopRestAPI.repository.UserRepository;
import de.surname.spring.SpringShopRestAPI.security.USERDetails;
import de.surname.spring.SpringShopRestAPI.util.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /*** GET user with by id*/
    public User findById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    /**GET ALL USER for ADMIN*/
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**SAVE CU*/
    public User save(User user) {
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    /**DELL USER for ADMIN*/
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(int id) {
        userRepository.deleteById(id);
    }

    /**RETURN CURRENT AUTHED USER*/
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        USERDetails userDetails = (USERDetails) authentication.getPrincipal();
        return userDetails.getUser();
    }
}
