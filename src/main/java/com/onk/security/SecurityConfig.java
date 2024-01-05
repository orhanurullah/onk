package com.onk.security;

import com.onk.core.utils.EntityConstants;
import com.onk.core.utils.RouteConstants;
import com.onk.security.auth.CustomAuthenticationEntryPoint;
import com.onk.security.auth.LogoutService;
import com.onk.security.jwt.JwtAccessDeniedHandler;
import com.onk.security.jwt.JwtFilter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    private final AuthenticationProvider authenticationProvider;

    private final LogoutService logoutService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> configure())
                .authorizeHttpRequests((auth) -> {
                    auth.requestMatchers(RouteConstants.primaryRoleBaseRoute + "/**")
                            .hasAuthority( "ROLE_" + EntityConstants.primaryRoleName);
                    auth.requestMatchers(RouteConstants.secondaryRoleBaseRoute + "/**")
                            .hasAuthority("ROLE_" + EntityConstants.secondaryRoleName);
                    auth.requestMatchers(RouteConstants.userBaseRoute + "/**")
                            .hasAuthority("ROLE_" + EntityConstants.baseRoleName);
                    auth.anyRequest().permitAll();
                })
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authenticationProvider(authenticationProvider)
                .exceptionHandling((exception) -> exception
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                        .authenticationEntryPoint(customAuthenticationEntryPoint))
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .logout((logout) ->
                        logout.logoutUrl(RouteConstants.authLogoutRoute))
                .logout((logout) -> logout.addLogoutHandler(logoutService))
                .logout((logout) ->
                        logout.logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()))
                .build();

    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web -> web.ignoring().requestMatchers(
                    RouteConstants.authBaseRoute + "/**"
                )
        );
    }

    @Bean
    public WebMvcConfigurer configure(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("*");
            }
        };
    }

}
