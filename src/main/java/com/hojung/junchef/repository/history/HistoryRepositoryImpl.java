package com.hojung.junchef.repository.history;

import com.hojung.junchef.domain.history.History;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
@Repository
public class HistoryRepositoryImpl implements HistoryRepository {
    private final EntityManager em;

    @Override
    public History save(History history) {
        em.persist(history);

        return history;
    }

    @Override
    public Optional<History> findById(Long id) {
        History history = em.find(History.class, id);

        return Optional.ofNullable(history);
    }

    @Override
    public Optional<History> findByMemberIdAndRecipeName(Long memberId, String recipeName) {
        List<History> result = em.createQuery(
                        "SELECT h FROM History h " +
                                "WHERE h.member.id = :memberId " +
                                "AND h.recipe.recipeName = :recipeName", History.class)
                .setParameter("memberId", memberId)
                .setParameter("recipeName", recipeName)
                .getResultList();

        return result.stream().findAny();
    }

    @Override
    public List<History> findAllByMemberId(Long memberId) {
        return em.createQuery("select h from History h where h.member.id = :member_id", History.class)
                .setParameter("member_id", memberId)
                .getResultList();
    }

    @Override
    public void deleteById(Long id) {
        History history = em.find(History.class, id);

        if (history != null)
            deleteByHistory(history);
    }

    @Override
    public void deleteByHistory(History history) {
        em.remove(history);
        em.flush();
        em.clear();
    }
}
