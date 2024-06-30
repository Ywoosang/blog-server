package com.ywoosang.tech.domains.post.entity;

import com.ywoosang.tech.domains.category.entity.Category;
import com.ywoosang.tech.domains.comment.entity.Comment;
import com.ywoosang.tech.domains.common.entity.BaseEntity;
import com.ywoosang.tech.domains.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.ArrayList;

@Entity
@Getter
@Table(name = "posts", indexes = {
        @Index(name = "ix_posts_user_id", columnList = "user_id"),
        @Index(name = "ix_posts_category_id", columnList = "category_id"),
        @Index(name = "ix_posts_title", columnList = "title"),
        @Index(name = "ix_posts_slug", columnList = "slug")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uc_posts_slug", columnNames = "slug")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_posts_members"))
    private Member member;


    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @JoinColumn(name = "category_id", foreignKey = @ForeignKey(name = "fk_posts_categories"))
    private Category category;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, length = 255)
    private String slug;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    // 다대일 단방향일 경우 Builder 에서 처리
    @Builder
    public Post(Member member, String title, String content, String slug) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.slug = slug;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setPost(this);
    }

    public void setCategory(Category category) {
        this.category = category;
        if (category != null && !category.getPosts().contains(this)) {
            category.getPosts().add(this);
        }
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateTitle(String title) {
        this.title = title;
    }
}
