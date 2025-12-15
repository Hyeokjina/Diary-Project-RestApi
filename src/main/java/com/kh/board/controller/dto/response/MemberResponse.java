package com.kh.board.controller.dto.response;

import com.kh.board.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class MemberResponse {

    @Getter
    @Setter
    @Builder
    public static class InfoDto {
        private Long id;
        private String email;
        private String nickname;
        private LocalDateTime createdAt;

        public static InfoDto of(Member member) {
            return InfoDto.builder()
                    .id(member.getId())
                    .email(member.getEmail())
                    .nickname(member.getNickname())
                    .createdAt(member.getCreatedAt())
                    .build();
        }
    }
}
