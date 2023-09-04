package com.hojung.junchef.repository.member;

import com.hojung.junchef.domain.member.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findById(Long id);
    Optional<Member> findByEmail(String email);
    List<Member> findAll();
    void deleteById(Long id);
    void deleteById(Member member);
}
