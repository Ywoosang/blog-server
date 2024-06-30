package com.ywoosang.tech.domains.post.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ywoosang.tech.domains.category.entity.QCategory;
import com.ywoosang.tech.domains.member.entity.QMember;
import com.ywoosang.tech.domains.post.dto.PostDetailsDTO;
import com.ywoosang.tech.domains.post.entity.QPost;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<PostDetailsDTO> findDetailsBySlug(String slug) {
        QMember member = QMember.member;
        QPost post = QPost.post;
        QCategory category = QCategory.category;

        PostDetailsDTO foundPost = queryFactory
                .select(
                        Projections.fields(PostDetailsDTO.class,
                                post.postId,
                                post.slug,
                                post.title,
                                post.content,
                                member.nickname.as("author"),
                                category.name.as("categoryName")
                        )
                )
                .from(post)
                .innerJoin(post.member, member)
                .leftJoin(post.category, category)
                .where(post.slug.eq(slug))
                .fetchOne();
        return Optional.ofNullable(foundPost);
    }
}
