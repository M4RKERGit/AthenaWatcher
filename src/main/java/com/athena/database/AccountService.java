package com.athena.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService
{
    @Autowired
    private UserRepository userRepository;

    public AccountService (UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    public List<Account> findAll() {return userRepository.findAll();}
}
