package com.hojung.junchef.controller.member.dto.request;

import com.hojung.junchef.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberJoinRequest {

    private String name;
    private String email;
    private String passwd;

    @Builder
    public MemberJoinRequest(String name, String email, String passwd) {
        this.name = name;
        this.email = email;
        this.passwd = passwd;
    }

    public Member toMember() {
        return Member.builder()
                .name(name)
                .email(email)
                .passwd(passwd)
                .build();
    }
}
