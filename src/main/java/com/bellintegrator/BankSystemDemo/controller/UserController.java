package com.bellintegrator.BankSystemDemo.controller;

import com.bellintegrator.BankSystemDemo.model.User;
import com.bellintegrator.BankSystemDemo.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class UserController {
    private UserService userService;

    public void addUser(@Valid User user){
        userService.saveUser(user);
    }
}
