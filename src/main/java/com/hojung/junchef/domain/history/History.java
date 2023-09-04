package com.hojung.junchef.domain.history;

import com.hojung.junchef.domain.BaseEntity;
import com.hojung.junchef.domain.member.Member;
import com.hojung.junchef.domain.recipe.Recipe;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

import static com.hojung.junchef.domain.constant.HistoryConstant.DATE_TIME_FORMAT;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class History extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    public History(Member member, Recipe recipe) {
        this.member = member;
        this.recipe = recipe;

        if(member != null){
            member.getHistory().add(0, this);
        }
    }

    public String getDateTime() {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        return this.lastModifiedDateTime.format(timeFormatter);
    }
}
