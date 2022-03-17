package com.athena.security;

import com.athena.database.Account;
import com.athena.database.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    private final UserRepository userRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.csrf().disable().authorizeRequests()
                .antMatchers("/terminal/").hasRole("admin")
                .antMatchers("/api/").hasRole("admin")
                .anyRequest().permitAll().and().httpBasic();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService());
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService()
    {
        List<UserDetails> details = new ArrayList<>();
        for (Account account : userRepository.findAll())
        {
            details.add(User.builder()
                    .username(account.getUsername())
                    .password(passwordEncoder().encode(account.getPassword()))
                    .roles(account.getRole())
                    .build());
        }
        return new InMemoryUserDetailsManager(details);
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {return new BCryptPasswordEncoder();}
}
