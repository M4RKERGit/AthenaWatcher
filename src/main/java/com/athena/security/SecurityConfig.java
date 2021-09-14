package com.athena.security;

import com.athena.AthenaSettings;
import com.athena.database.Account;
import com.athena.database.AccountService;
import com.athena.linuxtools.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    private static final Logger logger = new Logger("[SEC]");

    @Autowired
    private AccountService accountService;

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.csrf().disable().authorizeRequests()
                .antMatchers("/terminal/").hasRole("ADMIN")
                .antMatchers("/api/").hasRole("ADMIN")
                .antMatchers("/upload/").hasAnyRole("ADMIN", "CLOUDER")
                .anyRequest().authenticated().and().httpBasic();
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService()
    {
        List<Account> gotFromInterface = accountService.findAll();
        ArrayList<UserDetails> details = new ArrayList<>();
        for (Account account : gotFromInterface)
        {
            details.add(User.builder()
                    .username(account.getName())
                    .password(account.getPassword())
                    .roles(account.getRole())
                    .build());
        }
        return new InMemoryUserDetailsManager(
                //details.toArray(new UserDetails[0])
                User.builder()
                        .username("admin")
                        .password(passwordEncoder().encode(AthenaSettings.Roles.adminPassword))
                        .roles(Role.ADMIN.name())
                        .build(),
                User.builder()
                        .username("clouder")
                        .password(passwordEncoder().encode(AthenaSettings.Roles.clouderPassword))
                        .roles(Role.CLOUDER.name())
                        .build()
        );
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {return new BCryptPasswordEncoder();}
}
