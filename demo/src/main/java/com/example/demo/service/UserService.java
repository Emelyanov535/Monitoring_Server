package com.example.demo.service;

import com.example.demo.model.Channel;
import com.example.demo.model.CustomUserDetails;
import com.example.demo.repository.ChannelRepository;
import com.example.demo.utils.JwtTokenUtils;
import com.example.demo.controller.dto.JwtRequest;
import com.example.demo.controller.dto.JwtResponse;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtils jwtTokenUtils;
    private final ChannelRepository channelRepository;

    @Transactional
    public User createUser(String username, String password){
        final User user = new User(username, passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        return new CustomUserDetails(user);
    }

    @Transactional
    public JwtResponse getAuthToken(JwtRequest authRequest){
        try {
            UsernamePasswordAuthenticationToken qwe = new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());
            authenticationManager.authenticate(qwe);
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException("Неккоректные данные");
        }
        CustomUserDetails userDetails = new CustomUserDetails(userRepository.findByUsername(authRequest.getUsername()));
        String token = jwtTokenUtils.generateToken(userDetails);
        return new JwtResponse(token);
    }

    @Transactional
    public Long addChannel(Long chatId){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User realUser = userRepository.getReferenceById(user.getId());
        Channel channel = new Channel(chatId);
        channelRepository.save(channel);
        realUser.setChannel(channel);
        return channel.getChatId();
    }
}
