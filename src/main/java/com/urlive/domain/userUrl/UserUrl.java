package com.urlive.domain.userUrl;


import com.urlive.domain.BaseEntity;
import com.urlive.domain.url.Url;
import com.urlive.domain.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@AttributeOverride(name = "id", column = @Column(name = "user_url_id"))
public class UserUrl extends BaseEntity {

    protected UserUrl() {

    }

    public UserUrl(User user, Url url) {
        this.user = user;
        this.url = url;
        user.getUrls().add(this);
        url.getUsers().add(this);
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "url_id")
    private Url url;

    @Column(nullable = false, columnDefinition = "varchar(255) default ''")
    private String title = "";

    public static final String INVALID_USER_URL = "현재 사용자님이 가지고 있지 않는 url입니다.";

    public User getUser() {
        return user;
    }

    public Url getUrl() {
        return url;
    }

    public String getName() {
        return user.getName();
    }

    public String getOriginalUrl() {
        return url.getOriginalUrl();
    }

    public String getShortUrl() {
        return url.getShortUrl();
    }

    public String getTitle() {
        return title;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getCreateTime() {
        return url.getCreatedAt();
    }

    public Long getViewCount() {
        return url.getViewCount();
    }
}
