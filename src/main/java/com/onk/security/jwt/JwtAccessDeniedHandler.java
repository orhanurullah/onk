package com.onk.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        Map<String, String> forbiddenResponse = new HashMap<>();
        forbiddenResponse.put("success", String.valueOf(false));
        forbiddenResponse.put("message", "Ops! Forbidden Error!");

        ObjectMapper mapper = new ObjectMapper();
        String forbiddenJson = mapper.writeValueAsString(forbiddenResponse);
        response.getWriter().write(forbiddenJson);
        response.getWriter().flush();
    }
}
