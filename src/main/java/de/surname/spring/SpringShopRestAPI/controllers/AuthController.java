package de.surname.spring.SpringShopRestAPI.controllers;

import de.surname.spring.SpringShopRestAPI.dto.AuthenticationDTO;
import de.surname.spring.SpringShopRestAPI.models.User;
import de.surname.spring.SpringShopRestAPI.security.JWTUtil;
import de.surname.spring.SpringShopRestAPI.security.USERDetails;
import de.surname.spring.SpringShopRestAPI.services.RegistrationService;
import de.surname.spring.SpringShopRestAPI.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final RegistrationService registrationService;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Autowired
    public AuthController( RegistrationService registrationService, JWTUtil jwtUtil, AuthenticationManager authenticationManager, UserService userService) {
        this.registrationService = registrationService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    /**REG USER**/
    @PostMapping("/registration")
    public ResponseEntity<Map<String, String>> performRegistration(@RequestBody @Valid User user) {
        try {
            registrationService.register(user);
            String token = jwtUtil. generateToken(user.getUsername(), user.getId());
            return ResponseEntity.ok(Map.of("jwt-token", token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Registration failed: " + e.getMessage()));
        }
    }

    /**LOGIN USER**/
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> performLogin(@RequestBody AuthenticationDTO authenticationDTO) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    authenticationDTO.getUsername(),
                    authenticationDTO.getPassword());
            authenticationManager.authenticate(authenticationToken);
            String token = jwtUtil.generateToken(authenticationDTO.getUsername(), authenticationDTO.getId());
            return ResponseEntity.ok(Map.of("jwt-token", token));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Incorrect credentials"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Login failed: " + e.getMessage()));
        }
    }

    /**EDIT USER**/
    @PutMapping("/edit")
    public ResponseEntity<?> editUserData(
            @RequestBody Map<String, String> userData) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        USERDetails userDetails = (USERDetails) authentication.getPrincipal();
        User currentUser = userDetails.getUser();

        // UPDATE fields if nn
        String newUsername = userData.get("username");
        if (newUsername != null) {
            currentUser.setUsername(newUsername);
        }

        String newEmail = userData.get("email");
        if (newEmail != null) {
            currentUser.setEmail(newEmail);
        }

        String newPassword = userData.get("password");
        if (newPassword != null) {
            currentUser.setPassword(newPassword);
        }

        currentUser.setUpdatedAt(LocalDateTime.now());
        userService.save(currentUser);
        String token = jwtUtil.generateToken(currentUser.getUsername(), currentUser.getId());

        return ResponseEntity.ok(Map.of("message", "Successfully updated", "jwt-token", token));
    }


    /** LOGOUT **/
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> performLogout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(Map.of("message", "Successfully logged out "));
    }
}

