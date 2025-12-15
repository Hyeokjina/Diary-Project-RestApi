package com.kh.board.controller;

import com.kh.board.controller.dto.response.ApiResponse;
import com.kh.board.controller.dto.response.DiaryResponse;
import com.kh.board.entity.Diary;
import com.kh.board.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/members/{memberId}/diaries")
public class MemberDiaryController {

    private final DiaryService diaryService;

    // 회원별 일기 목록 조회 (계층 구조, 감정 필터링, 검색 지원)
    @GetMapping
    public ResponseEntity<ApiResponse<List<DiaryResponse.SimpleDto>>> getDiariesByMember(
            @PathVariable Long memberId,
            @RequestParam(required = false) String emotion,
            @RequestParam(required = false) String keyword) {
        
        List<Diary> diaries;
        
        if (emotion != null && !emotion.trim().isEmpty()) {
            // 감정별 필터링
            diaries = diaryService.findByMemberIdAndEmotion(memberId, emotion);
        } else if (keyword != null && !keyword.trim().isEmpty()) {
            // 검색
            diaries = diaryService.findByMemberIdAndKeyword(memberId, keyword);
        } else {
            // 전체 조회
            diaries = diaryService.findByMemberId(memberId);
        }

        List<DiaryResponse.SimpleDto> result = new ArrayList<>();
        for (Diary diary : diaries) {
            result.add(DiaryResponse.SimpleDto.of(diary));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(result));
    }
}

