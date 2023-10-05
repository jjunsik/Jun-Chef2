package com.hojung.junchef.service.recipe;

import com.hojung.junchef.domain.history.History;
import com.hojung.junchef.domain.member.Member;
import com.hojung.junchef.domain.recipe.Recipe;
import com.hojung.junchef.repository.recipe.RecipeRepository;
import com.hojung.junchef.service.history.HistoryService;
import com.hojung.junchef.service.member.MemberService;
import com.hojung.junchef.util.error.exception.JunChefException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hojung.junchef.util.error.exception.JunChefExceptionContent.NON_EXIST_RECIPE_ERROR;

@Service
@Transactional
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeGptService recipeGptService;
    private final MemberService memberService;
    private final HistoryService historyService;

    public Recipe findByName(Long memberId, String recipeName) {
        Member member = memberService.findById(memberId);

        // RecipeRepository 에 저장할 때, 무조건 공백이 제거된 상태로 저장했기 때문에 replaceAll 을 사용
        String replaceRecipeName = recipeName.replaceAll("\\s+", "");

        Recipe recipe = recipeRepository.findByName(replaceRecipeName)
                .orElseGet(
                        () -> getRecipeFromChatGPT(recipeName)
                );

        // 사용자는 항상 공백이 제거된 음식의 이름이 최근 검색어 목록으로 저장이 됨.
        saveHistory(member, recipe);

        return recipe;
    }

    // findByName() 처럼 gpt 와 통신을 해서 레시피를 얻어와야 하는지 아직은 모르겠음.
    public Recipe findById(Long memberId, Long recipeId) {
        Member member = memberService.findById(memberId);

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(
                        () -> new JunChefException(NON_EXIST_RECIPE_ERROR)
                );

        saveHistory(member, recipe);

        return recipe;
    }

    private void saveHistory(Member member, Recipe recipe) {
        History history = new History(member, recipe);
        historyService.save(history);
    }

    private Recipe getRecipeFromChatGPT(String recipeName) {
        Recipe recipe = recipeGptService.search(recipeName);

        return recipeRepository.save(recipe);
    }
}
