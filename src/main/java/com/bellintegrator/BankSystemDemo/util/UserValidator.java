package com.bellintegrator.BankSystemDemo.util;

import com.bellintegrator.BankSystemDemo.model.User;
import com.bellintegrator.BankSystemDemo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
@AllArgsConstructor
public class UserValidator implements Validator {
    private final UserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User currentUser = (User) target;
        Optional<User> userByEmail = userRepository.findUserByEmail(currentUser.getEmail());
        if(userByEmail.isEmpty()){
            errors.rejectValue("username", "", "User not found");
        }
    }
}
