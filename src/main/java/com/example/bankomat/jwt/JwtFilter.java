package com.example.bankomat.jwt;

import com.example.bankomat.entity.Card;
import com.example.bankomat.repository.CardRepository;
import com.example.bankomat.service.AuthService;

import com.example.bankomat.service.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    JwtProwider jwtProwider;
    @Autowired
    AuthService authService;
    @Autowired
    ExchangeService exchangeService;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token=httpServletRequest.getHeader("Authorization");
        if (token!=null&&token.startsWith("Bearer")){
            token=token.substring(7);
            String fromToken = jwtProwider.usernameFromToken(token);
            if (fromToken!=null){
                UserDetails userDetails = authService.loadUserByUsername(fromToken);
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        if (token!=null&&token.startsWith("Basic")){
            byte[] base64Token = token.substring(6).getBytes("UTF-8");
            byte[] decoded;
            try {
                decoded = Base64.decode(base64Token);
               String usernamepassword=new String(decoded);
                UserDetails userDetails = exchangeService.loadUserByUsername(usernamepassword.substring(0, usernamepassword.indexOf(":")));
                if (passwordEncoder.matches(usernamepassword.substring(usernamepassword.indexOf(":") + 1),userDetails.getPassword())){
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
             SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
           }
            else {
                Card card= (Card) userDetails;
                card.setBlockedCount(card.getBlockedCount()+1);
                if (card.getBlockedCount()>2){
                    card.setEnabled(false);
                    cardRepository.save(card);
                }
                cardRepository.save(card);
            }
                System.out.println(usernamepassword.substring(0, usernamepassword.indexOf(":")));
            } catch (IllegalArgumentException var7) {
                throw new BadCredentialsException(" specialnumber yoki password xato");
            }

        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
