package com.athena.database;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_accounts")
@Getter
@Setter
public class Account
{
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String role;

    @Column
    private String comment;

    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "last_login")
    private Date lastLogin;
}
