package br.com.myfreelas.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.myfreelas.config.filter.FilterJwt;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    private FilterJwt filterJwt;
    private AccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(FilterJwt filterJwt, AccessDeniedHandler accessDeniedHandler) {
        this.filterJwt = filterJwt;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/get-token").permitAll()
                        .requestMatchers(HttpMethod.POST, "/user/save-user").permitAll()
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**","/v3/api-docs/**").permitAll()
                        .requestMatchers("/skill/**").hasAuthority("ADMIN")
                        .requestMatchers("/freelancers/**").hasAnyAuthority("ADMIN", "FREELANCER")
                        .requestMatchers("/customer/**").hasAnyAuthority("ADMIN", "CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/project/my-projectsBy").authenticated()
                        .requestMatchers(HttpMethod.GET, "/project/my-projects").authenticated()
                        .requestMatchers("/project/**").hasAnyAuthority("ADMIN", "CUSTOMER")
                        .anyRequest().authenticated())
                .exceptionHandling(exception -> exception.accessDeniedHandler(accessDeniedHandler))
                .addFilterBefore(filterJwt, UsernamePasswordAuthenticationFilter.class)
                .build();
    }          
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) 
                    throws Exception{

        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
