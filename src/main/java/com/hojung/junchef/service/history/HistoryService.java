package com.hojung.junchef.service.history;

import com.hojung.junchef.domain.history.History;
import com.hojung.junchef.repository.history.HistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.hojung.junchef.service.constant.HistoryServiceConstant.DELETE_MEMBER_HISTORY_INDEX;
import static com.hojung.junchef.service.constant.HistoryServiceConstant.HISTORY_MAX_SIZE;
import static com.hojung.junchef.service.constant.RecipeServiceConstant.NON_EXIST_RECIPE_ERROR_MESSAGE;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class HistoryService {
    private final HistoryRepository historyRepository;

    public Long save(History history) {
        List<History> memberHistories = history.getMember().getHistories();

        History finalHistory = history;

        // 공백을 제거한 레시피 이름이 같은지 확인
        Optional<History> alreadyExistHistory = memberHistories.stream()
                .filter(getHistoryResponse ->
                        getHistoryResponse.getRecipe().getRecipeName().equals(finalHistory.getRecipe().getRecipeName())
                )
                .findFirst();

        // 최근 검색어에 존재하는 검색어를 검색 시 그 검색어의 수정 시간 변경
        if (alreadyExistHistory.isPresent()) {
            History modifyHistory = historyRepository.findById(alreadyExistHistory.get().getId()).get();

            // 바꿀 필드가 없어서 변경 감지를 못하기 때문에 수정 시간을 수동으로 변경해주어야 됨
            // 이게 맞는 방법인지는 모르겠음
            modifyHistory.setLastModifiedDateTime(LocalDateTime.now());
        }

        // 최근 검색어 목록에 존재하지 않는 것을 검색하고 최근 검색어 목록의 개수가 가득 찼을 때, 가장 오래된 검색어를 삭제
        if(memberHistories.size() == HISTORY_MAX_SIZE && alreadyExistHistory.isEmpty()) {
            memberHistories.sort(Comparator.comparing(History::getLastModifiedDateTime));
            delete(memberHistories.get(DELETE_MEMBER_HISTORY_INDEX).getId());
            log.info("가장 오래된 검색어 삭제");
        }

        // 최근 검색어 목록에 존재하지 않는 것을 검색했을 때, member 객체의 histories 에 추가하고 historyRepository 에 저장
        if (alreadyExistHistory.isEmpty()) {
            memberHistories.add(history);
            history = historyRepository.save(history);
            log.info("새로운 최근 검색어 추가");
        }

        return history.getId();
    }

    public void delete(Long historyId) {
        historyRepository.deleteById(historyId);
    }

    // 일단 안 씀
    public List<History> findAllByMemberId(Long memberId) {
        List<History> histories = historyRepository.findAllByMemberId(memberId);
        histories.sort(Comparator.comparing(History::getLastModifiedDateTime).reversed());

        return histories;
    }

    public History findByRecipeName(String recipeName) {
        recipeName = recipeName.replaceAll("\\s+", "");

        return historyRepository.findByName(recipeName).orElseThrow(
                () -> new IllegalStateException(NON_EXIST_RECIPE_ERROR_MESSAGE)
        );
    }
}
