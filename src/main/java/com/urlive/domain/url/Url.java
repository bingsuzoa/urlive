package com.urlive.domain.url;

import com.urlive.domain.BaseEntity;
import com.urlive.domain.userUrl.UserUrl;
import com.urlive.domain.view.View;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@AttributeOverride(name = "id", column = @Column(name = "url_id"))
public class Url extends BaseEntity {

    protected Url() {

    }

    public Url(String originalUrl, String shortUrl) {
        this.originalUrl = originalUrl;
        this.shortUrl = shortUrl;
    }

    @Column(unique = true, nullable = false)
    private String originalUrl;

    @Column(unique = true)
    private String shortUrl;

    @Column(columnDefinition = "int default 0")
    private Long viewCount = 0L;

    @OneToMany(mappedBy = "url")
    private List<View> views = new ArrayList<>();

    @OneToMany(mappedBy = "url")
    private Set<UserUrl> users = new HashSet<>();

    public static final String NOT_EXIST_SHORT_URL = "입력하신 단축 URL은 없는 URL입니다.";

    public String getOriginalUrl() {
        return originalUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public List<View> getViews() {
        return views;
    }

    public void saveView(View view) {
        views.add(view);
    }

    public Set<UserUrl> getUsers() {
        return users;
    }

    public Long getViewCount() {
        return viewCount;
    }

}
