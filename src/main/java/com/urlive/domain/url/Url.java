package com.urlive.domain.url;

import com.urlive.domain.base.BaseEntity;
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

    public Url(String originalUrl, String shortKey) {
        this.originalUrl = originalUrl;
        this.shortKey = shortKey;
    }

    @Column(nullable = false)
    private String originalUrl;

    @Column(nullable = false)
    private String shortKey;

    private String title;

    @Column(columnDefinition = "int default 0")
    private Long viewCount = 0L;

    @OneToMany(mappedBy = "url")
    private List<View> views = new ArrayList<>();

    @OneToMany(mappedBy = "url")
    private Set<UserUrl> users = new HashSet<>();

    public String getOriginalUrl() {
        return originalUrl;
    }

    public String getShortKey() {
        return shortKey;
    }

    public String getTitle() {
        return title;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public List<View> getViews() {
        return views;
    }

    public Set<UserUrl> getUsers() {
        return users;
    }
}
