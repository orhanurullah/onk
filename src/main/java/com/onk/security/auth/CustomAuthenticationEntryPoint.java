package com.onk.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
                        throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        Map<String, String> unAuthenticationMessage = new HashMap<>();
        unAuthenticationMessage.put("success", String.valueOf(false));
        unAuthenticationMessage.put("message", "Ops! You are not authenticate! Please log in or register");

        ObjectMapper mapper = new ObjectMapper();
        String unAuthorizationJson = mapper.writeValueAsString(unAuthenticationMessage);
        response.getWriter().write(unAuthorizationJson);
        response.getWriter().flush();
    }

}

