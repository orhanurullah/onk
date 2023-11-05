package com.onk.security.auth;

import com.onk.component.MessageService;
import com.onk.core.exception.ErrorCode;
import com.onk.core.exception.GenericException;
import com.onk.security.jwt.JwtTokenBlackList;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final JwtTokenBlackList jwtTokenBlackList;

    private final MessageService messageService;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);
        if(jwt.isBlank()){
            throw GenericException.builder()
                    .errorMessage(messageService.getMessage("user.logout.null.token", null))
                    .errorCode(ErrorCode.BAD_REQUEST)
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
        jwtTokenBlackList.addBlackListToken(jwt);
        SecurityContextHolder.clearContext();
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        try {
            response.getWriter().write("{\"message\": \"Logout successful\",");
            response.getWriter().write("\"state\" : true }");
            response.getWriter().flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
