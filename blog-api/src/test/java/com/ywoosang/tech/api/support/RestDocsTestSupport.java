package com.ywoosang.tech.api.support;

import com.ywoosang.tech.api.config.RestDocsConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@Disabled
@Import(RestDocsConfig.class)
@ExtendWith(RestDocumentationExtension.class)
public class RestDocsTestSupport extends ControllerTestSetup{
    @Autowired
    protected RestDocumentationResultHandler restDocs;

    @BeforeEach
    void setUp(
            final WebApplicationContext context,
            final RestDocumentationContextProvider provider
            ) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
            .alwaysDo(MockMvcResultHandlers.print())
            .alwaysDo(restDocs)
            .addFilter(new CharacterEncodingFilter("UTF-8", true))
            .build();
    }
}
