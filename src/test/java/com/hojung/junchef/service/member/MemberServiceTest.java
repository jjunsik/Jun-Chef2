package com.hojung.junchef.service.member;

import com.hojung.junchef.domain.member.Member;
import com.hojung.junchef.repository.member.MemberRepository;
import com.hojung.junchef.repository.member.MemberRepositoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import(MemberRepositoryImpl.class)
class MemberServiceTest {
    private final MemberService memberService;

    @Autowired
    public MemberServiceTest(MemberRepository memberRepository) {
        memberService = new MemberService(memberRepository);
    }

    Member member;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .name("test1")
                .email("test1@test.com")
                .passwd("test1").build();
    }

    @AfterEach
    void tearDown() {
    }

    @DisplayName("회원 가입")
    @Test
    void join() {
        // when
        Long memberId = memberService.join(this.member);

        // then
        assertThat(this.member.getId()).isEqualTo(memberId);
    }

    @DisplayName("이미 존재하는 이메일로 회원 가입을 할 경우, IllegalStateException 을 throw")
    @Test
    void joinExistEmail() {
        // given
        Member duplicateMember = Member.builder()
                .name("duplicateMember")
                .email("test1@test.com")
                .passwd("test1").build();

        memberService.join(this.member);

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
                .hasMessageContaining("중복 회원");

        // BDD X
        // when & then
        // 1.
        assertThatThrownBy(
                () -> memberService.join(duplicateMember))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("중복 회원");

        // 2. 대표적인 4가지 Exception 인 경우(NullPointer, IllegalArgument. IllegalState, IO)
//        assertThatIllegalStateException().isThrownBy(
//                        () -> {
//                            memberService.join(duplicateMember);
//                        })
//                .withMessageContaining("중복 회원");
////                .withNoCause();

        // 3. 대표적인 4가지 Exception 이 아닌 경우
//        assertThatExceptionOfType(IllegalStateException.class)
//                .isThrownBy(() -> {
//            memberService.join(duplicateMember);
//        })
//                .withMessageContaining("중복 회원");
////                .withNoCause();
    }

    @DisplayName("모든 회원 조회")
    @Test
    void findAll() {
        // given
        Member member1 = Member.builder()
                .name("test2")
                .email("test2@test.com")
                .passwd("test2").build();

        memberService.join(this.member);
        memberService.join(member1);

        // when
        List<Member> memberList = memberService.findAll();

        // then
        assertThat(memberList.size()).isEqualTo(2);
    }

    @DisplayName("아이디로 회원 조회")
    @Test
    void findById() {
        // given
        memberService.join(this.member);

        // when
        Member getMember = memberService.findById(this.member.getId());

        // then
        assertThat(getMember.getId()).isEqualTo(this.member.getId());
    }

    @DisplayName("존재하지 않는 아이디로 회원을 조회할 경우, IllegalStateException 을 throw")
    @Test
    void findInvalidId() {
        // given
        memberService.join(this.member);

        // when
        Throwable thrown = catchThrowable(
                () -> {
                    memberService.findById(this.member.getId()+1L);
                });

        // then
        assertThat(thrown)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("없는 회원");
    }

    @DisplayName("회원 비밀번호 변경")
    @Test
    void changePassword() {
        // given
        memberService.join(this.member);

        // when
        memberService.changePassword(this.member.getId(), "newPasswd");

        // then
        assertThat(this.member.getPasswd()).isEqualTo("newPasswd");
    }

    @DisplayName("회원 삭제")
    @Test
    void delete() {
        // given
        memberService.join(this.member);

        // when
        memberService.delete(this.member.getId());

        // then
        assertThat(memberService.findAll().size()).isEqualTo(0);
    }

    @DisplayName("로그인")
    @Test
    void login() {
        // given
        memberService.join(this.member);

        // when
        Long memberId = memberService.login(this.member.getEmail(), this.member.getPasswd());

        // then
        assertThat(memberId).isEqualTo(this.member.getId());
    }

    @DisplayName("존재하지 않는 이메일로 로그인하는 경우, IllegalStateException 을 throw")
    @Test
    void loginInvalidEmail() {
        // given
        memberService.join(this.member);

        // when
        Throwable throwable = catchThrowable(
                () -> {
                    memberService.login(this.member.getEmail() + "test", this.member.getPasswd());
                });

        // then
        assertThat(throwable)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("존재하지 않는 아이디");
    }

    @DisplayName("해당 이메일에 일치하지 않는 비밀번호로 로그인 하는 경우, IllegalStateException 을 throw")
    @Test
    void loginInvalidPassword() {
        // given
        memberService.join(this.member);

        // when
        Throwable throwable = catchThrowable(
                () -> {
                    memberService.login(this.member.getEmail(), this.member.getPasswd() + "test");
                });

        // then
        assertThat(throwable)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("비밀번호가 일치하지 않음");
    }
}