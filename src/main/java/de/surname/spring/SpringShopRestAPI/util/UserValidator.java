package de.surname.spring.SpringShopRestAPI.util;

import de.surname.spring.SpringShopRestAPI.models.User;
import de.surname.spring.SpringShopRestAPI.services.USERDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class UserValidator implements Validator {
    private final USERDetailsService userDetailsService;

    @Autowired
    public UserValidator(USERDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        try {
            userDetailsService.loadUserByUsername(user.getUsername());
        } catch (UsernameNotFoundException ignored){
            return;
        }
        errors.rejectValue("username","","username with this name just exist");
    }
}
