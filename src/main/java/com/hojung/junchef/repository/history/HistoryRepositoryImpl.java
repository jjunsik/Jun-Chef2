package com.hojung.junchef.repository.history;

import com.hojung.junchef.domain.history.History;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class HistoryRepositoryImpl implements HistoryRepository {
    private final EntityManager em;

    public HistoryRepositoryImpl(EntityManager em) {
        this.em = em;
    }

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
