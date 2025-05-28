package com.urlive.domain.userUrl;


import com.urlive.domain.base.BaseEntity;
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
        return url.getShortKey();
    }

    public String getTitle() {
        return url.getTitle();
    }

    public LocalDateTime getCreateTime() {
        return url.getCreatedAt();
    }

    public Long getViewCount() {
        return url.getViewCount();
    }
}
