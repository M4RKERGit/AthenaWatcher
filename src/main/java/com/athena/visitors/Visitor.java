package com.athena.visitors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Visitor
{
    @Id
    @GeneratedValue
    public Long id;

    public String description;
}
