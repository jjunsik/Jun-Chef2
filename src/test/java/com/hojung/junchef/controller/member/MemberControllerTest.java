package com.hojung.junchef.controller.member;

import com.google.gson.Gson;
import com.hojung.junchef.controller.member.dto.request.ChangePasswordRequest;
import com.hojung.junchef.controller.member.dto.request.LoginRequest;
import com.hojung.junchef.controller.member.dto.request.MemberJoinRequest;
import com.hojung.junchef.domain.member.Member;
import com.hojung.junchef.service.member.MemberService;
import com.hojung.junchef.util.GsonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MemberController.class)
class MemberControllerTest {

    @MockBean
    MemberService memberService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    static final long MEMBER_ID = 1L;

    Member member;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();

        member = Member.builder()
                .name("test1")
                .email("test1@test.com")
                .passwd("test1").build();
    }

    // jun-chef/v1/members

    @DisplayName("회원 한 명 조회")
    @Test
    void getMember() throws Exception {
        // given
        // given(): Mock 객체가 특정 상황에서 해야하는 행위를 정의하는 메소드
        given(memberService.findById(MEMBER_ID)).willReturn(this.member);
//        given(memberService.findById(anyLong())).willReturn(member1);

        // when & then
        // andExpect(): 기대하는 값이 나왔는지 체크해볼 수 있는 메소드
        mockMvc.perform(MockMvcRequestBuilders.get("/jun-chef/v1/members/{MEMBER_ID}", MEMBER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.email").exists());
        // BeforeEach()에 alwaysDo(print())를 하였으므로 할 필요 없음.
//                .andDo(print());

        // verify(): 해당 객체의 메소드가 실행되었는지 체크해줌
        // 현재 코드에선 memberService 라는 객체에서 1L로 findById() 가 실행되었는지 체크
//        verify(memberService, times(1)).findById(this.memberId);

        // BDDMockito 사용 - 밑에 두 개의 코드는 1번만 실행한 지 검사하는 것
//        then(memberService).should(times(1)).findById(this.memberId);
        then(memberService).should().findById(MEMBER_ID);
    }

    @DisplayName("전체 회원 조회")
    @Test
    void getAllMembers() throws Exception {
        // given
//        String ;
        Member memberDto2 = Member.builder()
                .name("test2")
                .email("test2@test.com")
                .passwd("test2").build();

        Member memberDto3 = Member.builder()
                .name("test3")
                .email("test3@test.com")
                .passwd("test3").build();

        given(memberService.findAll()).willReturn(List.of(this.member, memberDto2, memberDto3));

//        List<Member> members = new ArrayList<>();
//        members.add(this.memberDto);
//        members.add(memberDto2);
//        members.add(memberDto3);
//        given(memberService.findAll()).willReturn(members);

        // when & then
        // andExpect(content().string(containsString(""))): 리턴 받은 Body 에 ""문자열이 존재하는지 확인
        mockMvc.perform(MockMvcRequestBuilders.get("/jun-chef/v1/members"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("test1@test.com")))
                .andExpect(content().string(containsString("test2")))
                .andExpect(content().string(containsString("test3")));

        then(memberService).should().findAll();
    }

    @DisplayName("회원 가입")
    @Test
    void join() throws Exception {
        // given
        MemberJoinRequest joinRequest = MemberJoinRequest.builder()
                .name("test2")
                .email("test2@test.com")
                .passwd("test2")
                .build();

        String content = GsonUtils.toJson(joinRequest);

        given(memberService.join(any(Member.class))).willReturn(MEMBER_ID);
//        given(memberService.join()member1).willReturn(this.memberId);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/jun-chef/v1/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(content)))
                .andExpect(status().isOk());

//        verify(memberService).join(any(Member.class));
        then(memberService).should().join(any(Member.class));
    }

    @DisplayName("비밀번호 변경")
    @Test
    void changePassword() throws Exception {
        // given
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("newPasswd");

        Gson gson = new Gson();
        String content = gson.toJson(changePasswordRequest);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.put("/jun-chef/v1/members/{MEMBER_ID}", MEMBER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk());
    }

    @DisplayName("회원 삭제")
    @Test
    void delete() throws Exception {
        // given
        Gson gson = new Gson();
        String content = gson.toJson(MEMBER_ID);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.delete("/jun-chef/v1/members/{MEMBER_ID}", MEMBER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk());
    }

    @DisplayName("로그인")
    @Test
    void login() throws Exception {
        // given
        LoginRequest loginRequest = new LoginRequest(this.member.getEmail(), this.member.getPasswd());

        Gson gson = new Gson();
        String content = gson.toJson(loginRequest);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/jun-chef/v1/members/" + "login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk());
    }
}