package com.kh.board.controller.dto.request;

import com.kh.board.entity.Member;
import lombok.Getter;
import lombok.Setter;

public class MemberRequest {

    @Getter
    @Setter
    public static class SignupDto {
        private String email;
        private String password;
        private String nickname;

        public Member toEntity() {
            return Member.builder()
                    .email(email)
                    .password(password)
                    .nickname(nickname)
                    .build();
        }
    }

    @Getter
    @Setter
    public static class LoginDto {
        private String email;
        private String password;
    }

    @Getter
    @Setter
    public static class UpdateDto {
        private String email;
        private String password;
        private String nickname;

        public Member toEntity(Long id) {
            return Member.builder()
                    .id(id)
                    .email(email)
                    .password(password)
                    .nickname(nickname)
                    .build();
        }
    }
}
