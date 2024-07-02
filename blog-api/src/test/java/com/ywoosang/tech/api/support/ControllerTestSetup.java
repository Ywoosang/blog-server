package com.ywoosang.tech.api.support;


import com.ywoosang.tech.controller.TestController;
import com.ywoosang.tech.domains.post.repository.PostRepository;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

@Disabled
@WebMvcTest({
        TestController.class
})
public abstract class ControllerTestSetup {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected PostRepository postRepository;

    // 테스트 객체를 JSON 형식으로 변환
    protected String toJson(Object dto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(dto);
    }
}
