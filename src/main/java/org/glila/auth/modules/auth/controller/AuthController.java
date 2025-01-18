package org.glila.auth.modules.auth.controller;


import org.glila.auth.modules.auth.dto.CheckTokenRequestDto;
import org.glila.auth.modules.auth.dto.LoginRequestDto;
import org.glila.auth.modules.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("login")
    public String login(@RequestBody LoginRequestDto loginRequestDto) throws Exception {
        return authService.login(loginRequestDto);
    }


    @PostMapping("check")
    public String check(@RequestBody CheckTokenRequestDto checkTokenRequestDto) {
        try {
            return authService.check(checkTokenRequestDto.getToken());
        } catch (Exception e) {
            return e.getMessage();
        }
    }


}
