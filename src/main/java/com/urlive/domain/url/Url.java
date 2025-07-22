package com.urlive.domain.url;

import com.urlive.domain.BaseEntity;
import com.urlive.domain.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "url")
@AttributeOverride(name = "id", column = @Column(name = "url_id"))
public class Url extends BaseEntity {

    protected Url() {

    }

    public Url(User user, String originalUrl, String shortUrl) {
        this.user = user;
        this.originalUrl = originalUrl;
        this.shortUrl = shortUrl;
        user.getUrls().add(this);
    }

    public static final String NOT_EXIST_SHORT_URL = "입력하신 단축 URL은 없는 URL입니다.";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(unique = true, nullable = false, length = 767)
    private String originalUrl;

    @Column(unique = true)
    private String shortUrl;

    @Column(nullable = false, columnDefinition = "varchar(255) default ''")
    private String title = "";

    @Column(columnDefinition = "bigint default 0")
    private Long viewCount = 0L;

    public String getOriginalUrl() {
        return originalUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void increaseViewCount() {
        viewCount++;
    }

    public Long getViewCount() {
        return viewCount;
    }

    public Long getUser() {
        return user.getId();
    }

    public String getTitle() {
        return title;
    }

    public Url updateTitle(String title) {
        this.title = title;
        return this;
    }
}
