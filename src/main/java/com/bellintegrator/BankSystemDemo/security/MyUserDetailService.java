package com.bellintegrator.BankSystemDemo.security;

import com.bellintegrator.BankSystemDemo.model.User;
import com.bellintegrator.BankSystemDemo.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@NoArgsConstructor
@Service
public class MyUserDetailService implements UserDetailsService {

    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User currentUser =  userRepository.findUserByEmail(username).orElseThrow( () -> new UsernameNotFoundException("User not found"));
        return new MyUserDetail(currentUser);
    }
}
