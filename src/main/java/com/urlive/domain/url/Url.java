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

    public Url(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    @Column(unique = true, nullable = false)
    private String originalUrl;

    @Column(unique = true)
    private String shortKey;

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

    public void createShortKey(String shortKey) {
        this.shortKey = shortKey;
    }

    public List<View> getViews() {
        return views;
    }

    public Set<UserUrl> getUsers() {
        return users;
    }

    public Long getViewCount() {
        return viewCount;
    }

}
