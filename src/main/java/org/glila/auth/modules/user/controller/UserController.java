package org.glila.auth.modules.user.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.glila.auth.config.exception.handler.ErrorMessageException;
import org.glila.auth.modules.user.dto.UserDto;
import org.glila.auth.modules.user.dto.UserResponseDto;
import org.glila.auth.modules.user.entity.User;
import org.glila.auth.modules.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("")
    public ResponseEntity<List<UserResponseDto>> index() {

        List<UserResponseDto> users = this.userRepository.findAll().stream().map(user -> new UserResponseDto(
                user.getId(),
                user.getUsername()
        )).toList();

        return ResponseEntity.ok(users);
    }

    @PostMapping("")
    public ResponseEntity<UserResponseDto> createNewUser(@RequestBody UserDto userDto) {

        // check the username if exists
        User exists = userRepository.findByUsername(userDto.getUsername());
        System.out.println(exists.getUsername());
        if (exists != null) throw new ErrorMessageException("username already in use", HttpStatus.BAD_REQUEST);


        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEnabled(true);

        userRepository.save(user);

        UserResponseDto response = new UserResponseDto(
                user.getId(),
                user.getUsername()
        );

        return ResponseEntity.ok(response);
    }

}
