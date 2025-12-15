package com.kh.board.controller.dto.request;

import com.kh.board.entity.Diary;
import lombok.Getter;
import lombok.Setter;

public class DiaryRequest {

    @Getter
    @Setter
    public static class CreateDto {
        private Long memberId;
        private String title;
        private String content;
        private String emotion;

        public Diary toEntity() {
            return Diary.builder()
                    .memberId(memberId)
                    .title(title)
                    .content(content)
                    .emotion(emotion)
                    .build();
        }
    }

    @Getter
    @Setter
    public static class UpdateDto {
        private String title;
        private String content;
        private String emotion;

        public Diary toEntity(Long id, Long memberId) {
            return Diary.builder()
                    .id(id)
                    .memberId(memberId)
                    .title(title)
                    .content(content)
                    .emotion(emotion)
                    .build();
        }
    }
}
