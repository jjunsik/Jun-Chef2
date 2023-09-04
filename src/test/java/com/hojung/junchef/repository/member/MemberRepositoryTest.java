package com.hojung.junchef.repository.member;

import com.hojung.junchef.domain.member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@DataJpaTest
@Import(MemberRepositoryImpl.class)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("회원 정보를 저장합니다.")
    @Test
    void saveMember() {
        // given
        Member member = Member.builder()
                .name("Hwang")
                .passwd("passwd")
                .email("hwang@email.com")
                .build();

        // when
        memberRepository.save(member);

        // then
        Member result = memberRepository.findById(member.getId()).get();
        assertThat(member).isEqualTo(result);
    }

    @DisplayName("해당 아이디와 일치하는 회원을 조회합니다.")
    @Test
    void getMemberById() {
        // given
        Member member = Member.builder()
                .email("hwang@email.com")
                .name("Hwang")
                .passwd("passwd")
                .build();
        memberRepository.save(member);

        // when
        Member result = memberRepository.findById(member.getId()).get();

        // then
        assertThat(member).isEqualTo(result);

    }

    @DisplayName("해당 이메일과 일치하는 회원을 조회합니다.")
    @Test
    void getMemberByEmail() {
        // given
        Member member = Member.builder()
                .email("hwang@email.com")
                .name("Hwang")
                .passwd("passwd")
                .build();
        memberRepository.save(member);

        // when
        Member result = memberRepository.findByEmail(member.getEmail()).get();

        // then
        assertThat(member).isEqualTo(result);
    }

    @DisplayName("전체 회원을 조회합니다.")
    @Test
    void getAllMember() {
        // given
        Member member = Member.builder()
                .email("hwang@email.com")
                .name("Hwang")
                .passwd("passwd")
                .build();

        Member member1 = Member.builder()
                .email("hwang1@email.com")
                .name("Hwang1")
                .passwd("passwd1")
                .build();

        memberRepository.save(member);
        memberRepository.save(member1);

        // when
        List<Member> memberList = memberRepository.findAll();

        // then
        assertThat(memberList.size()).isEqualTo(2);
    }

    @DisplayName("회원을 삭제합니다.")
    @Test
    void deleteMember() {
        // given
        Member member = Member.builder()
                .email("hwang@email.com")
                .name("Hwang")
                .passwd("passwd")
                .build();
        memberRepository.save(member);

        Member member1 = Member.builder()
                .email("hwang2@email.com")
                .name("Hwang2")
                .passwd("passwd2")
                .build();
        memberRepository.save(member1);

        // when
        memberRepository.deleteById(member.getId());

        // then
        assertThat(memberRepository.findById(member.getId())).isEmpty();
        assertThat(memberRepository.findAll().size()).isEqualTo(1);
    }
}