package com.onk.security.auth;

import com.onk.core.utils.RouteConstants;
import com.onk.security.request.LoginRequest;
import com.onk.security.request.UserRequest;
import com.onk.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(RouteConstants.authBaseRoute)
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    private final UserService userService;

    @PostMapping(RouteConstants.authRegisterRoute)
    public ResponseEntity<?> register(
           @Valid @RequestBody UserRequest request
    ) throws IOException {
        return ResponseEntity.ok(service.register(request));
    }
    @PostMapping(RouteConstants.authLoginRoute)
    public ResponseEntity<?> authenticate(
            @Valid @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(service.login(request));
    }
    @GetMapping(RouteConstants.authActivationRoute)
    public ResponseEntity<?> emailActivation(@Valid @PathVariable Long id,
                                             @RequestParam("token") String token){
        return ResponseEntity.ok(userService.activation(token, id));
    }
    @PostMapping(RouteConstants.authRefreshTokenRoute)
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {
        service.refreshToken(request, response);
    }
}
