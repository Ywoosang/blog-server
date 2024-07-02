package com.ywoosang.tech.controller;

import com.ywoosang.tech.domains.member.entity.Member;
import com.ywoosang.tech.domains.member.repository.MemberRepository;
import com.ywoosang.tech.domains.post.dto.PostDetailsDTO;
import com.ywoosang.tech.domains.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TestController {

    private final PostRepository postRepository;

    // test
    @GetMapping("/")
    public String root() {
        return "hello root";
    }

    // test
    @GetMapping("/home")
    public String home() {
        return "hello home";
    }

    @GetMapping("/test")
    public PostDetailsDTO posts() {
        return postRepository.findDetailsBySlug("테스트-게시물")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
    }
}