package com.ywoosang.tech.domain.post;

import com.ywoosang.tech.config.TestConfig;
import com.ywoosang.tech.domains.category.entity.Category;
import com.ywoosang.tech.domains.category.repository.CategoryRepository;
import com.ywoosang.tech.domains.member.entity.Member;
import com.ywoosang.tech.domains.member.repository.MemberRepository;
import com.ywoosang.tech.domains.post.dto.PostDetailsDTO;
import com.ywoosang.tech.domains.post.entity.Post;
import com.ywoosang.tech.domains.post.repository.PostRepository;
import com.ywoosang.tech.enums.MemberRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = TestConfig.class)
@DisplayName("PostRepository 테스트")
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Member createTestMember() {
        return Member.builder()
                .nickname("테스트 사용자")
                .email("testuser@example.com")
                .role(MemberRole.ADMIN)
                .build();
    }

    private Category createTestCategory() {
        return Category.builder()
                .name("테스트 카테고리")
                .slug("technology")
                .build();
    }

    private Post createTestPost(Member member, Category category, String slug) {
        Post post = Post.builder()
                .title("Post 도메인 테스트")
                .content("테스트 내용")
                .slug(slug)
                .member(member)
                .build();
        post.setCategory(category);
        return post;
    }

    @Nested
    @DisplayName("save 메서드는")
    class SaveTests {

        @Test
        @DisplayName("새로운 게시물을 저장할 수 있어야 한다")
        void shouldSaveNewPost() {
            // Given
            Member member = memberRepository.save(createTestMember());
            Category category = categoryRepository.save(createTestCategory());

            Post post = createTestPost(member, category, "새로운-포스트");

            // When
            Post savedPost = postRepository.save(post);

            // Then
            assertThat(savedPost.getPostId()).isNotNull();
            assertThat(savedPost.getTitle()).isEqualTo("Post 도메인 테스트");
            assertThat(savedPost.getContent()).isEqualTo("테스트 내용");
            assertThat(savedPost.getSlug()).isEqualTo("새로운-포스트");
        }

        @Test
        @DisplayName("필수 필드가 없는 경우 저장할 수 없어야 한다")
        void shouldNotSavePostWithoutRequiredFields() {
            // Given
            Member member = memberRepository.save(createTestMember());

            Post post = Post.builder()
                    .content("내용만 있는 포스트")
                    .slug("내용만-있는-포스트")
                    .member(member)
                    .build();

            // When
            Exception exception = null;
            try {
                postRepository.save(post);
            } catch (Exception e) {
                exception = e;
            }

            // Then
            assertThat(exception).isNotNull();
        }
    }


    @Nested
    @DisplayName("findDetailsBySlug 메서드는")
    class FindBySlugTests {

        @Test
        @DisplayName("Slug 로 게시물을 조회할 수 있어야 한다")
        void shouldReturnPostDetailsWhenSlugExists() {
            // Given
            Member member = memberRepository.save(createTestMember());
            Category category = categoryRepository.save(createTestCategory());

            Post post = createTestPost(member, category, "Post-도메인-테스트");
            postRepository.save(post);

            // When
            Optional<PostDetailsDTO> postDetails = postRepository.findDetailsBySlug("Post-도메인-테스트");

            // Then
            assertThat(postDetails).isPresent();
            assertThat(postDetails.get().getTitle()).isEqualTo("Post 도메인 테스트");
            assertThat(postDetails.get().getContent()).isEqualTo("테스트 내용");
            assertThat(postDetails.get().getSlug()).isEqualTo("Post-도메인-테스트");
            assertThat(postDetails.get().getAuthor()).isEqualTo("테스트 사용자");
            assertThat(postDetails.get().getCategoryName()).isEqualTo("테스트 카테고리");
        }

        @Test
        @DisplayName("존재하지 않는 Slug로 조회할 경우 빈 Optional을 반환해야 한다.")
        void shouldReturnEmptyOptionalWhenSlugDoesNotExist() {
            // When
            Optional<PostDetailsDTO> postDetails = postRepository.findDetailsBySlug("없는-슬러그");

            // Then
            assertThat(postDetails).isNotPresent();
        }

        @Test
        @DisplayName("카테고리가 없는 게시물은 카테고리 이름이 null 이어야 한다.")
        void shouldReturnNullCategoryNameWhenPostHasNoCategory() {
            // Given
            Member member = memberRepository.save(createTestMember());

            Post post = createTestPost(member, null, "카테고리-없는-포스트");
            postRepository.save(post);

            // When
            Optional<PostDetailsDTO> postDetails = postRepository.findDetailsBySlug("카테고리-없는-포스트");

            // Then
            assertThat(postDetails).isPresent();
            assertThat(postDetails.get().getTitle()).isEqualTo("Post 도메인 테스트");
            assertThat(postDetails.get().getContent()).isEqualTo("테스트 내용");
            assertThat(postDetails.get().getSlug()).isEqualTo("카테고리-없는-포스트");
            assertThat(postDetails.get().getAuthor()).isEqualTo("테스트 사용자");
            assertThat(postDetails.get().getCategoryName()).isNull();
        }
    }


    @Nested
    @DisplayName("deleteBySlug 메서드는")
    class DeleteBySlugTests {

        @Test
        @DisplayName("주어진 Slug로 게시물을 삭제할 수 있어야 한다")
        void shouldDeletePostBySlug() {
            // Given
            Member member = memberRepository.save(createTestMember());
            Category category = categoryRepository.save(createTestCategory());

            Post post = createTestPost(member, category, "Post-도메인-테스트");
            postRepository.save(post);

            // When
            postRepository.deleteBySlug("Post-도메인-테스트");

            // Then
            Optional<PostDetailsDTO> postDetails = postRepository.findDetailsBySlug("Post-도메인-테스트");
            assertThat(postDetails).isNotPresent();
        }

        @Test
        @DisplayName("존재하지 않는 Slug로 삭제를 시도해도 에러를 발생시키지 않아야 한다.")
        void shouldDoNothingWhenSlugDoesNotExist() {
            // Given
            Member member = memberRepository.save(createTestMember());
            Category category = categoryRepository.save(createTestCategory());

            Post post = createTestPost(member, category, "Post-도메인-테스트");
            postRepository.save(post);

            // When
            // Then
            Assertions.assertDoesNotThrow(() -> postRepository.deleteBySlug("없는-슬러그"));
        }
    }


    @Nested
    @DisplayName("update 메서드는")
    class UpdateTests {
        @Test
        @DisplayName("주어진 게시물의 내용을 업데이트할 수 있어야 한다")
        void shouldUpdatePostContent() {
            // Given
            Member member = memberRepository.save(createTestMember());
            Category category = categoryRepository.save(createTestCategory());

            Post post = createTestPost(member, category, "Post-도메인-테스트");
            postRepository.save(post);

            // When
            post.updateContent("업데이트된 내용");
            postRepository.save(post);

            // Then
            Optional<PostDetailsDTO> postDetails = postRepository.findDetailsBySlug("Post-도메인-테스트");
            assertThat(postDetails).isPresent();
            assertThat(postDetails.get().getContent()).isEqualTo("업데이트된 내용");
        }
    }

}
