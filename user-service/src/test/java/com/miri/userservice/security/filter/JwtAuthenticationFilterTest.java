package com.miri.userservice.security.filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miri.userservice.domain.user.UserRepository;
import com.miri.userservice.dto.user.RequestUserDto.LoginReqDto;
import com.miri.userservice.dummy.DummyObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Sql("classpath:db/teardown.sql")
class JwtAuthenticationFilterTest extends DummyObject {

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() throws Exception {
        userRepository.save(newUser("test@naver.com", "홍길동", "010-1111-1111", "서울특별시"));
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    public void successfulAuthentication_test() throws Exception {

        // given
        LoginReqDto reqDto = new LoginReqDto();
        reqDto.setEmail("test@naver.com");
        reqDto.setPassword("1234");
        String requestBody = om.writeValueAsString(reqDto);

        // when
        ResultActions resultActions = mvc.perform(post("/login")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        String jwtToken = resultActions.andReturn().getResponse().getHeader("token");
        System.out.println("jwtToken = " + jwtToken);

        // then
        resultActions.andExpect(status().isOk());
        assertThat(jwtToken).isNotNull();
    }

    @Test
    @DisplayName("로그인 실패 테스트")
    public void unSuccessfulAuthentication_test() throws Exception {

        // given
        LoginReqDto reqDto = new LoginReqDto();
        reqDto.setEmail("null@naver.com");
        reqDto.setPassword("1234");
        String requestBody = om.writeValueAsString(reqDto);

        // when
        ResultActions resultActions = mvc.perform(post("/login")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        String jwtToken = resultActions.andReturn().getResponse().getHeader("token");

        // then
        resultActions.andExpect(status().isBadRequest());
        assertThat(jwtToken).isNull();
    }
}