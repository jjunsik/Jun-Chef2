package com.hojung.junchef.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

public class BaseEntity {
    @CreatedDate
    protected LocalDateTime createDateTime;
    @LastModifiedDate
    protected LocalDateTime lastModifiedDateTime;
}
