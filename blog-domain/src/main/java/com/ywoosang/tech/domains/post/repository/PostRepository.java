package com.ywoosang.tech.domains.post.repository;

import com.ywoosang.tech.domains.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    void deleteBySlug(String slug);
}
