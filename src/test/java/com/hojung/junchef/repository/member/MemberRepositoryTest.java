package com.hojung.junchef.repository.member;

import com.hojung.junchef.domain.member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(MemberRepositoryImpl.class)
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("회원 정보를 저장합니다.")
    @Test
    void saveMember() {
        // given
        Member member1 = createMember(1);

        // when
        member1 = memberRepository.save(member1);

        // then
        Member result = memberRepository.findById(member1.getId()).get();
        assertThat(member1).isEqualTo(result);
    }

    @DisplayName("해당 아이디와 일치하는 회원을 조회합니다.")
    @Test
    void getMemberById() {
        // given
        Member member1 = createMember(1);
        member1 = memberRepository.save(member1);

        // when
        Member result = memberRepository.findById(member1.getId()).get();

        // then
        assertThat(member1).isEqualTo(result);

    }

    @DisplayName("해당 이메일과 일치하는 회원을 조회합니다.")
    @Test
    void getMemberByEmail() {
        // given
        Member member1 = createMember(1);
        member1 = memberRepository.save(member1);

        // when
        Member result = memberRepository.findByEmail(member1.getEmail()).get();

        // then
        assertThat(member1).isEqualTo(result);
    }

    @DisplayName("전체 회원을 조회합니다.")
    @Test
    void getAllMember() {
        // given
        Member member1 = createMember(1);
        Member member2 = createMember(2);

        memberRepository.save(member1);
        memberRepository.save(member2);

        // when
        List<Member> members = memberRepository.findAll();

        // then
        assertThat(members.size()).isEqualTo(2);
    }

    @DisplayName("회원을 삭제합니다.")
    @Test
    void deleteMember() {
        // given
        Member member1 = createMember(1);
        member1 = memberRepository.save(member1);

        Member member2 = createMember(2);
        memberRepository.save(member2);

        // when
        memberRepository.deleteById(member1.getId());

        // then
        assertThat(memberRepository.findById(member1.getId())).isEmpty();
        assertThat(memberRepository.findAll().size()).isEqualTo(1);
    }

    private Member createMember(int number) {
        return Member.builder()
                .email("testEmail" + number + "@test.com")
                .name("testName" + number)
                .passwd("testPW" + number)
                .build();
    }
}