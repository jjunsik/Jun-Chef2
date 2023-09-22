package com.hojung.junchef.repository.history;

import com.hojung.junchef.domain.history.History;
import com.hojung.junchef.domain.member.Member;
import com.hojung.junchef.domain.recipe.Recipe;
import com.hojung.junchef.repository.member.MemberRepository;
import com.hojung.junchef.repository.member.MemberRepositoryImpl;
import com.hojung.junchef.repository.recipe.RecipeRepository;
import com.hojung.junchef.repository.recipe.RecipeRepositoryImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({HistoryRepositoryImpl.class, MemberRepositoryImpl.class, RecipeRepositoryImpl.class})
class HistoryRepositoryTest {
    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private RecipeRepository recipeRepository;

    @DisplayName("최근 검색어를 저장합니다.")
    @Test
    void saveHistory() {
        // given
        Member member = Member.builder()
                .name("test")
                .email("test@test.com")
                .passwd("testPasswd")
                .build();

        Recipe recipe = Recipe.builder()
                .recipeName("testRecipe")
                .ingredients("testIngredients")
                .cookingOrder("testCookingOrder")
                .build();

        History history = new History(member, recipe);

        // when
        historyRepository.save(history);

        // then
        History validateHistory = historyRepository.findById(history.getId()).get();

        assertThat(validateHistory.getId())
                .isEqualTo(history.getId());
    }

    @DisplayName("전체 최근 검색어를 조회합니다.")
    @Test
    void getHistory() {
        // given
        Member member = Member.builder()
                .name("test")
                .email("test@test.com")
                .passwd("testPasswd")
                .build();

        Recipe recipe1 = Recipe.builder()
                .recipeName("recipe1")
                .ingredients("testIngredients1")
                .cookingOrder("testCookingOrder1")
                .build();

        Recipe recipe2 = Recipe.builder()
                .recipeName("recipe2")
                .ingredients("testIngredients2")
                .cookingOrder("testCookingOrder2")
                .build();

        member = memberRepository.save(member);
        recipe1 = recipeRepository.save(recipe1);
        recipe2 = recipeRepository.save(recipe2);

        History history1 = new History(member, recipe1);
        history1 = historyRepository.save(history1);

        History history2 = new History(member, recipe2);
        historyRepository.save(history2);

        // when
        List<History> memberHistories = historyRepository.findAllByMemberId(member.getId());

        // then
        assertThat(memberHistories.size()).isEqualTo(2);
        assertThat(memberHistories.get(0)).isEqualTo(history1);
    }

    @DisplayName("최근 검색어를 삭제합니다.")
    @Test
    void deleteHistoryById() {
        // given
        Member member1 = Member.builder()
                .name("test1")
                .email("test1@test.com")
                .passwd("testPasswd1")
                .build();
        member1 = memberRepository.save(member1);
        Long memberId = member1.getId();

        Recipe recipe1 = Recipe.builder()
                .recipeName("recipe1")
                .ingredients("ingredients1")
                .cookingOrder("cookingOrder1")
                .build();
        recipe1 = recipeRepository.save(recipe1);

        History history1 = new History(member1, recipe1);
        history1 = historyRepository.save(history1);

        // when
        historyRepository.deleteById(history1.getId());

        // then
        Optional<History> validateHistory = historyRepository.findById(history1.getId());
        assertThat(validateHistory).isEmpty();
    }

    @DisplayName("멤버 아이디와 레시피 이름으로 History 객체를 조회")
    @Test
    void findByMemberIdAndRecipeName() {
        // given
        Member member1 = Member.builder()
                .name("test1")
                .email("test1@test.com")
                .passwd("testPasswd1")
                .build();
        member1 = memberRepository.save(member1);
        Long memberId = member1.getId();

        Recipe recipe1 = Recipe.builder()
                .recipeName("recipe1")
                .ingredients("ingredients1")
                .cookingOrder("cookingOrder1")
                .build();
        recipe1 = recipeRepository.save(recipe1);

        History history1 = new History(member1, recipe1);
        history1 = historyRepository.save(history1);

        // when
        History history = historyRepository.findByMemberIdAndRecipeName(memberId, recipe1.getRecipeName()).get();

        // then
        assertThat(history).isEqualTo(history1);
    }
}