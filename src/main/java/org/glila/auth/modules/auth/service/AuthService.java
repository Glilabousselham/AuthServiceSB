package org.glila.auth.modules.auth.service;


import org.glila.auth.modules.auth.dto.LoginRequestDto;
import org.glila.auth.modules.user.entity.User;
import org.glila.auth.modules.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;



    public String login(LoginRequestDto loginRequestDto) throws Exception {

        // fetch the user from database
        final Optional<User> optionalUser = Optional.of(userRepository.findByUsername(loginRequestDto.getUsername()));

        // throw exception if the user not found
        User user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        // check the password
        boolean passwordsMatched =  bCryptPasswordEncoder.matches(loginRequestDto.getPassword(), user.getPassword());

        // throw password not matched exception
        if(!passwordsMatched) throw new Exception("Password wrong!");

        return JWTUtilService.generateToken(user.getUsername());
    }




    public String check(String token) throws Exception{




        return JWTUtilService.extractUsername(token);
    }


}
