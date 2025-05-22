package com.equipshare.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.equipshare.security.CustomUserDetailsService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/login", "/signup", "/user-login", "/payment",
                                "/borrowerDashboard", "/ownerDashboard", "/cart",
                                "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/owner/**").hasRole("OWNER")
                        .requestMatchers("/borrower/**").hasRole("BORROWER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/user-login")
                        .usernameParameter("username")  // Map to your form field
                        .passwordParameter("password")  // Map to your form field
                        .successHandler((request, response, authentication) -> {
                            String selectedRole = request.getParameter("selectedRole");
                            boolean isOwnerRequest = "owner".equalsIgnoreCase(selectedRole);

                            boolean hasRequiredRole = authentication.getAuthorities().stream()
                                    .anyMatch(a -> isOwnerRequest ?
                                            a.getAuthority().equals("ROLE_OWNER") :
                                            a.getAuthority().equals("ROLE_BORROWER"));

                            if (hasRequiredRole) {
                                response.sendRedirect(isOwnerRequest ? "/ownerDashboard" : "/borrowerDashboard");
                            } else {
                                response.sendRedirect("/login?error=role_mismatch");
                            }
                        })
                        .failureUrl("/login?error=auth_failed")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable()); // Temporarily disable CSRF for development

        return http.build();
    }

   

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }
}