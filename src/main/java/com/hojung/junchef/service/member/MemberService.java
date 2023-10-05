package com.hojung.junchef.service.member;

import com.hojung.junchef.domain.member.Member;
import com.hojung.junchef.repository.member.MemberRepository;
import com.hojung.junchef.util.error.exception.JunChefException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.hojung.junchef.util.error.exception.JunChefExceptionContent.*;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);

        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByEmail(member.getEmail())
                .ifPresent(m -> {
                    throw new JunChefException(DUPLICATE_MEMBER_ERROR);
                });
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(
                        () -> new JunChefException(NON_EXIST_MEMBER_ERROR)
                );
    }

    public void changePassword(Long memberId, String newPassword) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(
                        () -> new JunChefException(NON_EXIST_MEMBER_ERROR)
                );

        member.setPasswd(newPassword);
    }

    public void delete(Long memberId) {
        memberRepository.deleteById(memberId);
    }

    public Long login(String email, String passwd) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(
                        () -> new JunChefException(NON_EXIST_MEMBER_EMAIL_ERROR)
                );

        if (!member.getPasswd().equals(passwd)) {
            throw new JunChefException(MEMBER_PASSWORD_ERROR);
        }

        return member.getId();
    }

    public Long logout(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(
                        () -> new JunChefException(NON_EXIST_MEMBER_ERROR)
                );

        return member.getId();
    }
}
