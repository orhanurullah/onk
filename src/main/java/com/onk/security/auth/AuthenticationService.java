package com.onk.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onk.component.MessageService;
import com.onk.core.exception.ErrorCode;
import com.onk.core.exception.GenericException;
import com.onk.core.results.DataResult;
import com.onk.core.results.ErrorDataResult;
import com.onk.core.results.SuccessDataResult;
import com.onk.repository.UserRepository;
import com.onk.security.jwt.JwtTokenProvider;
import com.onk.security.request.LoginRequest;
import com.onk.security.request.UserRequest;
import com.onk.security.response.AuthenticationResponse;
import com.onk.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;

    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationManager authenticationManager;

    private final MessageService messageService;

    private final UserRepository userRepository;

    public DataResult<?> register(UserRequest userRequest){
        var userDto = userService.createUser(userRequest);
        return new SuccessDataResult<>(userDto,
                messageService.getMessage("user.register.true", null)
                );
    }

    public DataResult<AuthenticationResponse> login(LoginRequest loginRequest){
        var user = userRepository.findByEmailAddress(loginRequest.getEmail()).orElse(null);
        if(user == null){
            return new ErrorDataResult<>(
                    messageService.getMessage("error.not_found", new Object[]{loginRequest.getEmail()})
            );
        }
        if(!user.getIsActive()){
            return new ErrorDataResult<>(
                    messageService.getMessage("user.login.isActive.false", null)
            );
        }
        if(user.getIsDeleted()){
            return new ErrorDataResult<>(
                    messageService.getMessage("user.login.isDeleted.true", null)
            );
        }
       try{
           authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(
                           loginRequest.getEmail(),
                           loginRequest.getPassword()
                   )
           );
           return new SuccessDataResult<>(
                   authenticationResponse(user),
                   messageService.getMessage("user.login.true", new Object[]{user.getEmail()})
           );
       }catch (Exception e){
           return new ErrorDataResult<>(
                   messageService.getMessage("user.login.false", null)
           );
       }
    }

    private AuthenticationResponse authenticationResponse(UserDetails user) {
       try{
           var accessToken = jwtTokenProvider.generateToken(user);
           var refreshToken = jwtTokenProvider.generateRefreshToken(user);
           return AuthenticationResponse.builder()
                   .accessToken(accessToken)
                   .refreshToken(refreshToken)
                   .build();
       }catch (IllegalArgumentException e){
           throw GenericException.builder()
                   .errorMessage(messageService.getMessage("authentication.error", null))
                   .errorCode(ErrorCode.BAD_REQUEST)
                   .httpStatus(HttpStatus.BAD_REQUEST)
                   .build();
       }
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            throw new Exception(messageService.getMessage("refresh_token.error", null));
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtTokenProvider.extractUsername(refreshToken);
        if(userEmail != null){
            var user = this.userService.findUserByUsername(userEmail);
            if(jwtTokenProvider.validateToken(refreshToken, user)){
                var accessToken = jwtTokenProvider.generateToken(user);
                var newRefreshToken = jwtTokenProvider.generateRefreshToken(user);

                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(newRefreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
