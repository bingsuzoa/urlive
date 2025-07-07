package com.urlive.domain.user;

import com.urlive.domain.BaseEntity;
import com.urlive.domain.user.option.Gender;
import com.urlive.domain.user.option.country.Country;
import com.urlive.domain.user.passwordHistory.PasswordHistory;
import com.urlive.domain.userUrl.UserUrl;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@AttributeOverride(name = "id", column = @Column(name = "user_id"))
public class User extends BaseEntity {

    protected User() {

    }

    public User(
            String name,
            String phoneNumber,
            String password,
            int age,
            Gender gender,
            Country country
    ) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.age = age;
        this.gender = gender;
        this.country = country;
        this.urls = new HashSet<>();
        PasswordHistory passwordHistory = new PasswordHistory(this, password);
        passwordHistories.add(passwordHistory);
    }

    public static final String NOT_EXIST_USER_ID = "존재하지 않는 회원입니다.";
    public static final String NOT_MATCH_PASSWORD = "비밀번호가 일치하지 않습니다.";

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false, length = 11)
    private String phoneNumber;

    @Column(nullable = false, length = 60)
    private String password;

    @Column(nullable = false, length = 4)
    private int age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(unique = true, name = "country_id", nullable = false)
    private Country country;

    @OneToMany(mappedBy = "user")
    private Set<UserUrl> urls = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private Set<PasswordHistory> passwordHistories = new HashSet<>();

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
        PasswordHistory passwordHistory = new PasswordHistory(this, newPassword);
        passwordHistories.add(passwordHistory);
    }

    public int getAge() {
        return age;
    }

    public Gender getGender() {
        return gender;
    }

    public Country getCountry() {
        return country;
    }

    public Set<UserUrl> getUrls() {
        return urls;
    }

    public Set<PasswordHistory> getPasswordHistories() {
        return passwordHistories;
    }
}
