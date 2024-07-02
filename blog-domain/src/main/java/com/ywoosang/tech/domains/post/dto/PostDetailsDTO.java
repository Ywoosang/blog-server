package com.ywoosang.tech.domains.post.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class PostDetailsDTO {
    private Long postId;
    private String slug;
    private String title;
    private String content;
    private String author;
    private String categoryName;
}
