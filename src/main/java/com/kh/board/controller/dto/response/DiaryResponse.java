package com.kh.board.controller.dto.response;

import com.kh.board.entity.Diary;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class DiaryResponse {

    @Getter
    @Setter
    @Builder
    public static class SimpleDto {
    private Long id;
    private String title;
    private String content;
    private String emotion;
    private LocalDateTime createdAt;

    public static SimpleDto of(Diary diary) {
        return SimpleDto.builder()
                .id(diary.getId())
                .title(diary.getTitle())
                .content(diary.getContent())
                .emotion(diary.getEmotion())
                .createdAt(diary.getCreatedAt())
                .build();
    }
}

    @Getter
    @Setter
    @Builder
    public static class DetailDto {
        private Long id;
        private Long memberId;
        private String title;
        private String content;
        private String emotion;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static DetailDto of(Diary diary) {
            return DetailDto.builder()
                    .id(diary.getId())
                    .memberId(diary.getMemberId())
                    .title(diary.getTitle())
                    .content(diary.getContent())
                    .emotion(diary.getEmotion())
                    .createdAt(diary.getCreatedAt())
                    .updatedAt(diary.getUpdatedAt())
                    .build();
        }
    }
}
