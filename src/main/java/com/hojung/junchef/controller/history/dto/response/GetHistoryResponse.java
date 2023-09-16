package com.hojung.junchef.controller.history.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hojung.junchef.domain.history.History;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class GetHistoryResponse {
    private String recipeName;

    @JsonFormat(pattern = "MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDateTime createDateTime;

    public static GetHistoryResponse of(History history) {
        return GetHistoryResponse.builder()
                .recipeName(history.getRecipe().getRecipeName())
                .createDateTime(history.getLastModifiedDateTime())
                .build();
    }
}
