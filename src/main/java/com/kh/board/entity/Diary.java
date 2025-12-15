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
public class Diary {
    private Long id;
    private Long memberId;
    private String title;
    private String content;
    private String emotion;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}