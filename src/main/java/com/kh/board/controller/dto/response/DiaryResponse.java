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
    private Long userId;  // 프론트엔드 호환성을 위해 memberId를 userId로도 제공
    private String title;
    private String content;
    private String emotion;
    private String date;  // YYYY-MM-DD 형식
    private LocalDateTime createdAt;

    public static SimpleDto of(Diary diary) {
        // createdAt에서 날짜 부분만 추출 (YYYY-MM-DD)
        String dateStr = diary.getCreatedAt() != null 
            ? diary.getCreatedAt().toLocalDate().toString() 
            : null;
            
        return SimpleDto.builder()
                .id(diary.getId())
                .userId(diary.getMemberId())  // memberId를 userId로 매핑
                .title(diary.getTitle())
                .content(diary.getContent())
                .emotion(diary.getEmotion())
                .date(dateStr)
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
        private Long userId;  // 프론트엔드 호환성을 위해 memberId를 userId로도 제공
        private String title;
        private String content;
        private String emotion;
        private String date;  // YYYY-MM-DD 형식
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static DetailDto of(Diary diary) {
            // createdAt에서 날짜 부분만 추출 (YYYY-MM-DD)
            String dateStr = diary.getCreatedAt() != null 
                ? diary.getCreatedAt().toLocalDate().toString() 
                : null;
                
            return DetailDto.builder()
                    .id(diary.getId())
                    .memberId(diary.getMemberId())
                    .userId(diary.getMemberId())  // memberId를 userId로도 매핑
                    .title(diary.getTitle())
                    .content(diary.getContent())
                    .emotion(diary.getEmotion())
                    .date(dateStr)
                    .createdAt(diary.getCreatedAt())
                    .updatedAt(diary.getUpdatedAt())
                    .build();
        }
    }
}
