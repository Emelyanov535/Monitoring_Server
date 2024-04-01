package com.example.demo.controller.rest;

import com.example.demo.controller.dto.ChatIdDto;
import com.example.demo.service.UserService;
import com.example.demo.controller.dto.JwtRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest jwtRequest){
        return ResponseEntity.ok(userService.getAuthToken(jwtRequest));
    }

    @PostMapping("/registration")
    public void registration(@RequestBody JwtRequest jwtRequest){
        userService.createUser(jwtRequest.getUsername(), jwtRequest.getPassword());
    }

    @PostMapping("/addChannel")
    public ResponseEntity<?> addChannel(@RequestBody ChatIdDto chatIdDto){
        return ResponseEntity.ok(userService.addChannel(chatIdDto.getChatId()));
    }
}
