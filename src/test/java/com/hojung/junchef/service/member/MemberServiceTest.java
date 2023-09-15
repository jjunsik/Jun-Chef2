package com.hojung.junchef.service.member;

import com.hojung.junchef.domain.member.Member;
import com.hojung.junchef.repository.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("회원 가입")
    @Test
    void join() {
        // given
        Member member1 = createMember(1);

        // when
        Long memberId = memberService.join(member1);

        // then
        assertThat(member1.getId()).isEqualTo(memberId);
    }

    @DisplayName("이미 존재하는 이메일로 회원 가입을 할 경우, IllegalStateException 을 throw")
    @Test
    void joinExistEmail() {
        // given
        String duplicateMemberErrorMsg = "중복 회원";
        Member member1 = createMember(1);

        Member duplicateMember = Member.builder()
                .name("duplicateMember")
                .email(member1.getEmail())
                .passwd("test1").build();

        memberService.join(member1);

        // 예상한 Exception 인지 검증하는 방법

        // BDD 방식
        // when
        Throwable thrown = catchThrowable(
                () -> {
                    memberService.join(duplicateMember);
                });

        // then
        assertThat(thrown)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(duplicateMemberErrorMsg);

        // BDD X
        // when & then
        // 1.
        assertThatThrownBy(
                () -> memberService.join(duplicateMember))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(duplicateMemberErrorMsg);

        // 2. 대표적인 4가지 Exception 인 경우(NullPointer, IllegalArgument. IllegalState, IO)
//        assertThatIllegalStateException().isThrownBy(
//                        () -> {
//                            memberService.join(duplicateErrorMsg);
//                        })
//                .withMessageContaining(duplicateMemberErrorMsg);
////                .withNoCause();

        // 3. 대표적인 4가지 Exception 이 아닌 경우
//        assertThatExceptionOfType(IllegalStateException.class)
//                .isThrownBy(() -> {
//            memberService.join(duplicateMember);
//        })
//                .withMessageContaining(duplicateMemberErrorMsg);
////                .withNoCause();
    }

    @DisplayName("모든 회원 조회")
    @Test
    void findAll() {
        // given
        Member member1 = createMember(1);
        Member member2 = createMember(2);

        memberService.join(member1);
        memberService.join(member2);

        // when
        List<Member> members = memberService.findAll();

        // then
        assertThat(members.size()).isEqualTo(2);
    }

    @DisplayName("아이디로 회원 조회")
    @Test
    void findById() {
        // given
        Member member1 = createMember(1);
        memberService.join(member1);

        // when
        Member getMember = memberService.findById(member1.getId());

        // then
        assertThat(getMember.getId()).isEqualTo(member1.getId());
    }

    @DisplayName("존재하지 않는 아이디로 회원을 조회할 경우, IllegalStateException 을 throw")
    @Test
    void findInvalidId() {
        // given
        String nonExistMemberErrorMsg = "없는 회원";

        Member member1 = createMember(1);
        Long illegalId = 99L;

        memberService.join(member1);

        // when
        Throwable thrown = catchThrowable(
                () -> {
                    memberService.findById(illegalId);
                });

        // then
        assertThat(thrown)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(nonExistMemberErrorMsg);
    }

    @DisplayName("회원 비밀번호 변경")
    @Test
    void changePassword() {
        // given
        String newPasswd = "newPasswd";

        Member member1 = createMember(1);
        memberService.join(member1);

        // when
        memberService.changePassword(member1.getId(), newPasswd);

        // then
        assertThat(member1.getPasswd()).isEqualTo(newPasswd);
    }

    @DisplayName("회원 삭제")
    @Test
    void delete() {
        // given
        Member member1 = createMember(1);
        memberService.join(member1);

        // when
        memberService.delete(member1.getId());

        // then
        assertThat(memberService.findAll().size()).isEqualTo(0);
    }

    @DisplayName("로그인")
    @Test
    void login() {
        // given
        Member member1 = createMember(1);
        memberService.join(member1);

        // when
        Long memberId = memberService.login(member1.getEmail(), member1.getPasswd());

        // then
        assertThat(memberId).isEqualTo(member1.getId());
    }

    @DisplayName("존재하지 않는 이메일로 로그인하는 경우, IllegalStateException 을 throw")
    @Test
    void loginInvalidEmail() {
        // given
        String errorEmail = "error@error.com";
        String nonExistEmailErrorMsg = "존재하지 않는 이메일";

        Member member1 = createMember(1);
        memberService.join(member1);

        // when
        Throwable throwable = catchThrowable(
                () -> {
                    memberService.login(errorEmail, member1.getPasswd());
                });

        // then
        assertThat(throwable)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(nonExistEmailErrorMsg);
    }

    @DisplayName("해당 이메일에 일치하지 않는 비밀번호로 로그인 하는 경우, IllegalStateException 을 throw")
    @Test
    void loginInvalidPassword() {
        // given
        String errorPasswd = "ErrorPasswd";
        String passwordErrorMsg = "비밀번호가 일치하지 않음";

        Member member1 = createMember(1);
        memberService.join(member1);

        // when
        Throwable throwable = catchThrowable(
                () -> {
                    memberService.login(member1.getEmail(), errorPasswd);
                });

        // then
        assertThat(throwable)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(passwordErrorMsg);
    }

    private Member createMember(int number) {
        return Member.builder()
                .email("testEmail" + number + "@test.com")
                .name("testName" + number)
                .passwd("testPW" + number)
                .build();
    }
}