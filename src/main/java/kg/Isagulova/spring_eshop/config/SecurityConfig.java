package kg.Isagulova.spring_eshop.config;

import kg.Isagulova.spring_eshop.domain.Role;
import kg.Isagulova.spring_eshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled  = true)
public class SecurityConfig {
    private final UserService userService;
    private final PasswordEncoderConfig passwordEncoder;

    @Autowired
    public SecurityConfig(UserService userService, PasswordEncoderConfig passwordEncoderConfig) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoderConfig;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder.passwordEncoder());
        return auth;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, DaoAuthenticationProvider provider) throws Exception {

        http
                .csrf(customizer -> customizer.disable())
                .authenticationProvider(provider)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/auth", "/login-error").permitAll()
                        .requestMatchers("/users").hasRole("ADMIN")
                        .requestMatchers("/users").hasRole("MANAGER")
                        .requestMatchers("/users/new").hasRole("ADMIN")
                        .anyRequest().authenticated()

                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .loginProcessingUrl("/auth")
                        .failureUrl("/login-error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                );

        return http.build();
    }
}