package com.hojung.junchef.service.member;

import com.hojung.junchef.domain.member.Member;
import com.hojung.junchef.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.hojung.junchef.service.constant.MemberServiceConstant.*;

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
                    throw new IllegalStateException(DUPLICATE_MEMBER_ERROR_MESSAGE);
                });
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(
                        () -> new IllegalStateException(NON_EXIST_MEMBER_ERROR_MESSAGE)
                );
    }

    public void changePassword(Long memberId, String newPassword) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(
                        () -> new IllegalStateException(NON_EXIST_MEMBER_ERROR_MESSAGE)
                );

        member.setPasswd(newPassword);
    }

    public void delete(Long memberId) {
        memberRepository.deleteById(memberId);
    }

    public Long login(String email, String passwd) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(
                        () -> new IllegalStateException(NON_EXIST_EMAIL_ERROR_MESSAGE)
                );

        if (!member.getPasswd().equals(passwd)) {
            throw new IllegalStateException(PASSWORD_ERROR_MESSAGE);
        }

        return member.getId();
    }
}
