package com.urlive.domain.view;


import com.urlive.domain.BaseEntity;
import com.urlive.domain.url.Url;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@AttributeOverride(name = "id", column = @Column(name = "view_id"))
public class View extends BaseEntity {

    protected View() {

    }

    public View(Url url, LocalDateTime viewed_at) {
        this.url = url;
        this.viewed_at = viewed_at;
        url.getViews().add(this);
    }

    @ManyToOne
    @JoinColumn(name = "url_id")
    private Url url;

    private LocalDateTime viewed_at;

    public Url getUrl() {
        return url;
    }

    public LocalDateTime getViewed_at() {
        return viewed_at;
    }
}
