package com.onk.security.auth;
import com.onk.core.exception.GenericException;
import com.onk.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SecurityManager {

    public static User getCurrentUser() throws GenericException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null
                && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        return null;

    }
}
