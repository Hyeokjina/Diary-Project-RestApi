package com.kh.board.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Member {
    private Long id;
    private String email;
    private String password;
    private String nickname;
    private LocalDateTime createdAt;
}