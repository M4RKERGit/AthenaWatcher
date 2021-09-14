package com.athena.database;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "accounts")
@Getter
@Setter
public class Account
{
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer user_id;

    @Column
    private String name;

    @Column
    private String role;

    @Column
    private String password;

    @Column
    private String creationDate;
}
