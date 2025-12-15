package com.kh.board.controller;

import com.kh.board.controller.dto.request.MemberRequest;
import com.kh.board.controller.dto.response.ApiResponse;
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
    @PostMapping
    public ResponseEntity<ApiResponse<MemberResponse.InfoDto>> signup(@RequestBody MemberRequest.SignupDto request) {
        try {
            Member member = request.toEntity();
            int result = memberService.signup(member);

            if (result > 0) {
                Member savedMember = memberService.findByEmail(member.getEmail());
                MemberResponse.InfoDto response = MemberResponse.InfoDto.of(savedMember);
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(ApiResponse.success("회원가입이 완료되었습니다.", response));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("회원가입에 실패했습니다."));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<MemberResponse.InfoDto>> login(@RequestBody MemberRequest.LoginDto request) {
        try {
            Member member = memberService.login(request.getEmail(), request.getPassword());
            MemberResponse.InfoDto response = MemberResponse.InfoDto.of(member);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.success("로그인에 성공했습니다.", response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    // 회원 정보 조회
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MemberResponse.InfoDto>> getMember(@PathVariable Long id) {
        try {
            Member member = memberService.findById(id);
            MemberResponse.InfoDto response = MemberResponse.InfoDto.of(member);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.success(response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    // 회원 정보 수정
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MemberResponse.InfoDto>> updateMember(
            @PathVariable Long id,
            @RequestBody MemberRequest.UpdateDto request) {
        try {
            // 회원 존재 여부 확인
            memberService.findById(id);

            // 수정할 회원 엔티티 생성
            Member member = request.toEntity(id);
            int result = memberService.updateById(member);

            if (result > 0) {
                Member updatedMember = memberService.findById(id);
                MemberResponse.InfoDto response = MemberResponse.InfoDto.of(updatedMember);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(ApiResponse.success("회원 정보가 수정되었습니다.", response));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("회원 정보 수정에 실패했습니다."));
            }
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("찾을 수 없습니다")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(e.getMessage()));
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(e.getMessage()));
            }
        }
    }

    // 회원 탈퇴
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMember(@PathVariable Long id) {
        try {
            int result = memberService.deleteById(id);

            if (result > 0) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(ApiResponse.success("회원 탈퇴가 완료되었습니다.", null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("회원 탈퇴에 실패했습니다."));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
