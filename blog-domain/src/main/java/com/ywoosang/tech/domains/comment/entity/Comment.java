package com.ywoosang.tech.domains.comment.entity;
import com.ywoosang.tech.domains.common.entity.BaseEntity;
import com.ywoosang.tech.domains.member.entity.Member;
import com.ywoosang.tech.domains.post.entity.Post;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Entity
@Table(name = "comments", indexes = {
        @Index(name = "ix-comments-post_id", columnList = "post_id"),
        @Index(name = "ix-comments-user_id", columnList = "user_id"),
        @Index(name = "ix-comments-nickname", columnList = "nickname")
})
@Getter
@Setter
@NoArgsConstructor
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false, foreignKey = @ForeignKey(name = "fk-comments-posts"))
    private Post post;

    // 답글이 아닐 수도 있으므로 null 허용
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id", foreignKey = @ForeignKey(name = "fk-comments-comments"))
    private Comment parentComment;

    // 부모 댓글이 삭제되었을 때 orphanRemoval = true 로 자식 댓글도 함께 삭제
    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> replies = new ArrayList<>();

    // 회원 또는 비회원 댓글 생성이 가능해야 하므로 null 허용
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk-comments-members"))
    private Member member;

    @Column(length = 50)
    private String nickname;

    @Column(length = 255)
    private String password;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private boolean isPublic = true;

    @Builder
    public Comment(Member member,  String nickname, String password, String content, boolean isPublic) {
        this.member = member;
        this.nickname = nickname;
        this.password = password;
        this.content = content;
        this.isPublic = isPublic;
    }

    public void addReply(Comment reply) {
        this.replies.add(reply);
        reply.setParentComment(this);
    }

    public void setPost(Post post) {
        this.post = post;
        if (!post.getComments().contains(this)) {
            post.getComments().add(this);
        }
    }

    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
        if (parentComment != null && !parentComment.getReplies().contains(this)) {
            parentComment.getReplies().add(this);
        }
    }
}
