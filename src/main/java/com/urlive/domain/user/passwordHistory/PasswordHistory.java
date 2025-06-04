package com.urlive.domain.user.passwordHistory;


import com.urlive.domain.BaseEntity;
import com.urlive.domain.user.User;
import jakarta.persistence.*;

@Entity
@AttributeOverride(name = "id", column = @Column(name = "password_history_id"))
public class PasswordHistory extends BaseEntity {

    protected PasswordHistory() {

    }

    public PasswordHistory(User user, String password) {
        this.user = user;
        this.password = password;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, length = 60)
    private String password;

    public User getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
