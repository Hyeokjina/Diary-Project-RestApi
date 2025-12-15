package com.kh.board.controller;

import com.kh.board.controller.dto.request.MemberRequest;
import com.kh.board.controller.dto.response.MemberResponse;
import com.kh.board.entity.Member;
import com.kh.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody MemberRequest.SignupDto request) {
        try {
            Member member = request.toEntity();
            int result = memberService.signup(member);

            if (result > 0) {
                return new ResponseEntity<>("회원가입 성공", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("회원가입 실패", HttpStatus.BAD_REQUEST);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberRequest.LoginDto request) {
        try {
            Member member = memberService.login(request.getEmail(), request.getPassword());
            MemberResponse.InfoDto response = MemberResponse.InfoDto.of(member);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
