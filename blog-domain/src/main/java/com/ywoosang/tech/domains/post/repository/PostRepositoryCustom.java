package com.ywoosang.tech.domains.post.repository;

import com.ywoosang.tech.domains.post.dto.PostDetailsDTO;

import java.util.Optional;

public interface PostRepositoryCustom {
    // 게시물과 함께 작성자명, 카테고리명을 가져온다.
    Optional<PostDetailsDTO> findDetailsBySlug(String slug);
}
