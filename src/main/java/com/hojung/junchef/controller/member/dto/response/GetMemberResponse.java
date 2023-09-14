package com.hojung.junchef.controller.member.dto.response;

import com.hojung.junchef.controller.history.dto.response.GetHistoryResponse;
import com.hojung.junchef.domain.history.History;
import com.hojung.junchef.domain.member.Member;
import lombok.Data;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class GetMemberResponse {
    private final String name;
    private final String email;
    private final List<GetHistoryResponse> histories;

    public GetMemberResponse(Member member) {
        this.name = member.getName();
        this.email = member.getEmail();

        List<History> memberHistories = member.getHistories();

        // 내림차순으로 정렬
        memberHistories.sort(Comparator.comparing(History::getLastModifiedDateTime).reversed());

        this.histories = member.getHistories().stream()
                .map(GetHistoryResponse::of)
                .collect(Collectors.toList());
    }
}
