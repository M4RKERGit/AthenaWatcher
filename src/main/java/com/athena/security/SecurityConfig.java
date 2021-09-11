package com.athena.security;

import com.athena.linuxtools.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    private static final Logger logger = new Logger("[SEC]");
    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.csrf().disable().authorizeRequests()
                .antMatchers("/terminal/").hasRole(Role.ADMIN.name())
                .antMatchers("/api/").hasRole(Role.ADMIN.name())
                .antMatchers("/upload/").hasAnyRole(Role.ADMIN.name(), Role.CLOUDER.name())
                .anyRequest().authenticated().and().httpBasic();
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService()
    {
        String adminPass = "admin", clouderPass = "clouder";
        try
        {
            List<String> buf = Files.readAllLines(Path.of("roles.txt"));
            adminPass = buf.get(0).split(":")[1];
            clouderPass = buf.get(1).split(":")[1];
            logger.createLog(String.format("Admin password: %s", adminPass));
            logger.createLog(String.format("Clouder password: %s", clouderPass));
        }
        catch (IOException e) {logger.createLog("Error reading passwords from roles.txt, passwords will be default");}

        return new InMemoryUserDetailsManager(
                User.builder()
                        .username("admin")
                        .password(passwordEncoder().encode(adminPass))
                        .roles(Role.ADMIN.name())
                        .build(),
                User.builder()
                        .username("clouder")
                        .password(passwordEncoder().encode(clouderPass))
                        .roles(Role.CLOUDER.name())
                        .build());
    }

    @Bean
    protected PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}
