package de.surname.spring.SpringShopRestAPI.services;

import de.surname.spring.SpringShopRestAPI.models.User;
import de.surname.spring.SpringShopRestAPI.repository.UserRepository;
import de.surname.spring.SpringShopRestAPI.security.USERDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class USERDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public USERDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user= userRepository.findByUsername(username);

        if (user.isEmpty())
            throw new UsernameNotFoundException("User not found");
        return new USERDetails(user.get());
    }
}
