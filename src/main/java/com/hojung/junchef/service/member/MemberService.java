package com.hojung.junchef.service.member;

import com.hojung.junchef.domain.member.Member;
import com.hojung.junchef.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    String duplicateMemberErrorMsg = "중복 회원";

    String nonExistMemberErrorMsg = "없는 회원";

    String nonExistsEmailErrorMsg = "존재하지 않는 이메일";

    String passwordErrorMsg = "비밀번호가 일치하지 않음";

    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);

        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByEmail(member.getEmail())
                .ifPresent(m -> {
                    throw new IllegalStateException(duplicateMemberErrorMsg);
                });
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(
                        () -> new IllegalStateException(nonExistMemberErrorMsg)
                );
    }

    public void changePassword(Long memberId, String newPassword) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(
                        () -> new IllegalStateException(nonExistMemberErrorMsg)
                );
        member.setPasswd(newPassword);

        memberRepository.save(member);
    }

    public void delete(Long memberId) {
        memberRepository.deleteById(memberId);
    }

    public Long login(String email, String passwd) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(
                        () -> new IllegalStateException(nonExistsEmailErrorMsg)
                );

        if (!member.getPasswd().equals(passwd)) {
            throw new IllegalStateException(passwordErrorMsg);
        }

        return member.getId();
    }
}
