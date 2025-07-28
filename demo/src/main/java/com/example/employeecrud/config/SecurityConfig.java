package com.example.employeecrud.config;

import com.example.employeecrud.security.JwtFilter;
import com.example.employeecrud.service.ServiceImpl.CustomUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(authz ->
            authz
                    .requestMatchers(HttpMethod.POST,"/api/employees").permitAll()
                    .requestMatchers("/api/employees/hashPassword").permitAll()
                    .requestMatchers("api/employees/**").authenticated()
                    .anyRequest().permitAll()
        )
       // .formLogin(form ->form.permitAll().defaultSuccessUrl("/api/addresses/all"))
                .httpBasic(Customizer->{})
                .csrf(csrf ->csrf.disable())
                .sessionManagement( sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS) )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        ;
        return http.build();
    }
    @Bean
    public UserDetailsService userDetailService(){
    //    UserDetails user= User.withUsername("alice")
    //            .password(passwordEncoder.encode("user123"))
    //            .roles("USER")
    //            .build();
    //    UserDetails admin= User.withUsername("john")
    //            .password(passwordEncoder.encode("admin123"))
    //            .roles("ADMIN")
    //            .build();
    //    return new InMemoryUserDetailsManager(user, admin);
          return new CustomUserDetailsServiceImpl();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider= new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager AuthenticationManager(){
        return new ProviderManager(List.of(authenticationProvider()));
    }
}
