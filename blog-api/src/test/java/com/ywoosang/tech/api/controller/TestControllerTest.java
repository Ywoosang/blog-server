package com.ywoosang.tech.api.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ywoosang.tech.api.support.RestDocsTestSupport;
import com.ywoosang.tech.domains.post.dto.PostDetailsDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.Optional;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class TestControllerTest extends RestDocsTestSupport {

    @Test
    void testExample() throws Exception {
        // Given
        PostDetailsDTO postDetails = PostDetailsDTO.builder()
                .postId(1L)
                .slug("test-post")
                .title("Test Post")
                .content("This is a test post.")
                .author("Author Name")
                .categoryName("Category Name")
                .build();

        given(postRepository.findDetailsBySlug(anyString())).willReturn(Optional.of(postDetails));

        // When & Then
        this.mockMvc.perform(get("/api/v1/test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                responseFields(
                                        fieldWithPath("postId").type(JsonFieldType.NUMBER).description("게시물 ID"),
                                        fieldWithPath("slug").type(JsonFieldType.STRING).description("게시물 슬러그"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("게시물 제목"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("게시물 내용"),
                                        fieldWithPath("author").type(JsonFieldType.STRING).description("작성자"),
                                        fieldWithPath("categoryName").type(JsonFieldType.STRING).description("카테고리 이름")
                                )
                        )
                );
    }


}
