package com.hojung.junchef.repository.history;

import com.hojung.junchef.domain.history.History;

import java.util.List;
import java.util.Optional;

public interface HistoryRepository {
    History save(History history);
    Optional<History> findById(Long id);
    List<History> findAllByMemberId(Long memberId);
    void deleteById(Long id);
    void deleteByHistory(History history);
}
