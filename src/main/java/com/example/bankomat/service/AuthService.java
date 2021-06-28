package com.example.bankomat.service;

import com.example.bankomat.dto.ExchangeDto;
import com.example.bankomat.entity.Bankomat;
import com.example.bankomat.entity.ExchangeHistory;
import com.example.bankomat.entity.Card;
import com.example.bankomat.entity.User;
import com.example.bankomat.entity.enums.Rolename;
import com.example.bankomat.repository.*;
import com.example.bankomat.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class AuthService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userbyEmail = userRepository.findByEmail(email);
        return userbyEmail.orElseThrow(() -> new UsernameNotFoundException("Bunday email topilmadi !!!"));
    }


}

