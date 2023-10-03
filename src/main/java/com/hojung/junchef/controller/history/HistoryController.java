package com.hojung.junchef.controller.history;

import com.hojung.junchef.controller.history.dto.response.GetHistoryResponse;
import com.hojung.junchef.controller.history.dto.response.HistoryResponse;
import com.hojung.junchef.domain.history.History;
import com.hojung.junchef.service.history.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("jun-chef/v1/history")
public class HistoryController {
    private final HistoryService historyService;

    @Autowired
    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @DeleteMapping("/{historyId}")
    public HistoryResponse delete(@PathVariable("historyId") Long historyId) {
        historyService.delete(historyId);

        return new HistoryResponse(historyId);
    }

    @GetMapping("/{memberId}/{recipeName}")
    public HistoryResponse getHistoryId(@PathVariable("memberId") Long memberId, @PathVariable("recipeName") String recipeName) {
        History history = historyService.findByMemberIdAndRecipeName(memberId, recipeName);

        return new HistoryResponse(history.getId());
    }

    @GetMapping("/{memberId}")
    public List<GetHistoryResponse> getHistories(@PathVariable("memberId") Long memberId) {
        List<History> getHistories = historyService.findAllByMemberId(memberId);

        return getHistories.stream()
                .map(GetHistoryResponse::of)
                .collect(Collectors.toList());
    }
}
