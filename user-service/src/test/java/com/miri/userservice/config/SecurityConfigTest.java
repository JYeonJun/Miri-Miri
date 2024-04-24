package com.miri.userservice.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class SecurityConfigTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("인증 테스트")
    void authentication_test() throws Exception {
        //given

        //when
        ResultActions resultActions = mvc.perform(get("/api/auth/test"));
        int httpStatusCode = resultActions.andReturn().getResponse().getStatus();

        //then
        assertThat(httpStatusCode).isEqualTo(401);
    }

    @Test
    @DisplayName("인가 테스트")
    void authorization_test() throws Exception {
        //given

        //when
        ResultActions resultActions = mvc.perform(get("/api/admin/test"));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);
        int httpStatusCode = resultActions.andReturn().getResponse().getStatus();

        //then
        assertThat(httpStatusCode).isEqualTo(401);
    }
}