package com.ywoosang.tech.domains.category.entity;

import com.ywoosang.tech.domains.common.entity.BaseEntity;
import com.ywoosang.tech.domains.post.entity.Post;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories", uniqueConstraints = {
        @UniqueConstraint(name = "uc_categories_name", columnNames = "name"),
        @UniqueConstraint(name = "uc_categories_slug", columnNames = "slug")
}, indexes = {
        @Index(name = "ix_categories_slug", columnList = "slug")
})
@Getter
@Setter
@NoArgsConstructor
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String slug;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @Builder
    public Category(String name, String slug) {
        this.name = name;
        this.slug = slug;
    }

    // 연관관계 편의 메서드
    public void addPost(Post post) {
        this.posts.add(post);
        post.setCategory(this);
    }
}

