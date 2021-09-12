package com.athena.security;

import com.athena.AthenaSettings;
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
        return new InMemoryUserDetailsManager(
                User.builder()
                        .username("admin")
                        .password(passwordEncoder().encode(AthenaSettings.Roles.adminPassword))
                        .roles(Role.ADMIN.name())
                        .build(),
                User.builder()
                        .username("clouder")
                        .password(passwordEncoder().encode(AthenaSettings.Roles.clouderPassword))
                        .roles(Role.CLOUDER.name())
                        .build());
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {return new BCryptPasswordEncoder();}
}
