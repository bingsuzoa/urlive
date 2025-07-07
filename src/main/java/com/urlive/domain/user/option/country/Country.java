package com.urlive.domain.user.option.country;


import com.urlive.domain.user.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Country {

    protected Country() {

    }

    public Country(String isoCode,
                   String name
    ) {
        this.isoCode = isoCode;
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String isoCode;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "country", fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public String getName() {
        return name;
    }

    public List<User> getUsers() {
        return users;
    }
}
