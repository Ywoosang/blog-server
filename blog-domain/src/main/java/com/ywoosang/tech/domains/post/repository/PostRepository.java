package com.ywoosang.tech.domains.post.repository;

import com.ywoosang.tech.domains.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    @Modifying
    @Query("DELETE FROM Post p WHERE p.slug = :slug")
    void deleteBySlug(String slug);
}
