package com.example.bankomat.config;

import com.example.bankomat.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

@Configuration
public class KimYozganiniBilish implements AuditorAware<UUID> {
    @Bean
    @Override
    public Optional getCurrentAuditor() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if (authentication!=null&&authentication.isAuthenticated()&&!authentication.getPrincipal().equals("anonymouse")){
            User user=(User) authentication.getPrincipal();
            return Optional.of(user.getUuid());
        }else {
            return Optional.empty();
        }
    }
}
