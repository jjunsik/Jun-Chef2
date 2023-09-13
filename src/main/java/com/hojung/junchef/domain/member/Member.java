package com.hojung.junchef.domain.member;

import com.hojung.junchef.domain.BaseEntity;
import com.hojung.junchef.domain.history.History;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Member extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String passwd;

    @OneToMany(mappedBy = "member", orphanRemoval = true, cascade = CascadeType.REFRESH)
    private List<History> histories;

    @Builder
    public Member(String name, String email, String passwd) {
        this.name = name;
        this.email = email;
        this.passwd = passwd;
        this.histories = new ArrayList<>();
    }
}
