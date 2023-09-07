package com.hojung.junchef.controller.member.dto.response;

import com.hojung.junchef.domain.history.History;
import com.hojung.junchef.domain.member.Member;
import lombok.Data;

import java.util.List;

@Data
public class GetMemberResponse {
    private final String name;
    private final String email;
    private final List<History> history;

    public GetMemberResponse(Member member) {
        this.name = member.getName();
        this.email = member.getEmail();
        this.history = member.getHistory();
    }
}
