package com.hojung.junchef.service.history;

import com.hojung.junchef.domain.history.History;
import com.hojung.junchef.domain.member.Member;
import com.hojung.junchef.domain.recipe.Recipe;
import com.hojung.junchef.repository.history.HistoryRepository;
import com.hojung.junchef.service.member.MemberService;
import com.hojung.junchef.service.recipe.RecipeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class HistoryServiceTest {
    @Autowired
    private HistoryService historyService;

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private RecipeService recipeService;

    @DisplayName("검색어 저장")
    @Test
    void save() {
        // given
        History history1 = createHistory(1, 1);

        // when
        Long historyId = historyService.save(history1);

        // then
        assertThat(history1.getId()).isEqualTo(historyId);
    }

    @DisplayName("해당 id를 가진 회원의 최근 검색어 목록 전체를 조회")
    @Test
    void findAllByMemberId() {
        // given
        Member member1 = createMember(1);
        memberService.join(member1);

        Recipe recipe1 = createRecipe(1);
        recipeService.findByName(member1.getId(), recipe1.getRecipeName());

        Recipe recipe2 = createRecipe(2);
        recipeService.findByName(member1.getId(), recipe2.getRecipeName());

        History history1_1 = new History(member1, recipe1);
        historyService.save(history1_1);

        History history1_2 = new History(member1, recipe2);
        historyService.save(history1_2);

        // when
        List<History> histories = historyService.findAllByMemberId(member1.getId());

        // then
        assertThat(histories.size()).isEqualTo(2);
    }

    @DisplayName("검색어 삭제")
    @Test
    void delete() {
        // given
        History history1 = createHistory(1, 1);
        Long historyId = historyService.save(history1);

        // when
        historyService.delete(historyId);

        // then
        assertThat(historyService.findAllByMemberId(history1.getMember().getId()).size()).isEqualTo(0);
    }

    @DisplayName("이미 존재하는 검색어를 재검색 시 수정 시간 변경")
    @Test
    void alreadyExistHistory() {
        // given
        History history1 = createHistory(1, 1);
        Long history1Id = historyService.save(history1);

        History history2 = createHistory(1, 1);

        History history = historyRepository.findById(history1Id).get();
        LocalDateTime historyDateTime = history.getLastModifiedDateTime();

        // when
        Long history2Id = historyService.save(history2);
        History compareHistory = historyRepository.findById(history2Id).get();
        LocalDateTime compareDateTime = compareHistory.getLastModifiedDateTime();

        // then
        boolean result = compareDateTime.isAfter(historyDateTime);
        assertThat(result).isEqualTo(true);
    }

    @DisplayName("최근 검색어 목록의 크기가 최대일 때, 새로운 검색어로 검색 시 가장 오래된 검색어 삭제 ")
    @Test
    void deleteOldestHistory() {
        // given
        Member member = createMember(1);
        Long memberId = memberService.join(member);

        Recipe recipe1 = createRecipe(1);
        Recipe oldestRecipe = recipeService.findByName(memberId, recipe1.getRecipeName());

        Recipe recipe2 = createRecipe(2);
        recipeService.findByName(memberId, recipe2.getRecipeName());

        Recipe recipe3 = createRecipe(3);
        recipeService.findByName(memberId, recipe3.getRecipeName());

        Recipe recipe4 = createRecipe(4);
        recipeService.findByName(memberId, recipe4.getRecipeName());

        Recipe recipe5 = createRecipe(5);
        recipeService.findByName(memberId, recipe5.getRecipeName());

        Recipe recipe6 = createRecipe(6);

        // when
        Recipe getRecipe = recipeService.findByName(memberId, recipe6.getRecipeName());

        // then
        List<History> histories = historyService.findAllByMemberId(memberId);
        
        boolean result = histories.stream().map(History::getRecipe)
                .filter(recipe -> recipe.equals(oldestRecipe)) // oldestRecipe 와 일치하는 것을 필터링
                .anyMatch(recipe -> true);

        assertThat(histories.size()).isEqualTo(5);
        assertThat(histories.get(0).getRecipe().getRecipeName()).isEqualTo(recipe6.getRecipeName());
        assertThat(result).isEqualTo(false);
    }

    @DisplayName("레시피 이름으로 검색하여 History 객체 반환")
    @Test
    void getHistoryByRecipeName() {
        // given
        Member member1 = createMember(1);
        Long memberId = memberService.join(member1);

        Recipe recipe1 = createRecipe(1);
        String recipeName = recipe1.getRecipeName();

        recipeService.findByName(memberId, recipeName);

        // when
        History getHistoryByMemberIdAndRecipeName = historyService.findByMemberIdAndRecipeName(memberId, recipeName);

        // then
        assertThat(getHistoryByMemberIdAndRecipeName.getMember().getId()).isEqualTo(memberId);
        assertThat(getHistoryByMemberIdAndRecipeName.getRecipe().getRecipeName()).isEqualTo(recipeName);
    }

    private History createHistory(int memberNumber, int recipeNumber) {
        return new History(createMember(memberNumber), createRecipe(recipeNumber));
    }

    private Member createMember(int memberNumber) {
        return Member.builder()
                .email("testEmail" + memberNumber + "@test.com")
                .name("testName" + memberNumber)
                .passwd("testPW" + memberNumber)
                .build();
    }

    private Recipe createRecipe(int recipeNumber) {
        return Recipe.builder()
                .recipeName("testRecipeName" + recipeNumber)
                .ingredients("testIngredients" + recipeNumber)
                .cookingOrder("testCookingOrder" + recipeNumber)
                .build();
    }

}