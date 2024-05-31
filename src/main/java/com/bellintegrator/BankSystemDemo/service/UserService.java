package com.bellintegrator.BankSystemDemo.service;

import com.bellintegrator.BankSystemDemo.model.User;
import com.bellintegrator.BankSystemDemo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    public List<User> findAllUser(){
        return userRepository.findAll();
    }

    public void saveUser(User user){
        userRepository.save(user);
    }
}
