package com.kh.board.controller;

import com.kh.board.controller.dto.request.DiaryRequest;
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
@RequestMapping("/api/diaries")
public class DiaryController {

    private final DiaryService diaryService;

    // 전체 일기 목록 조회
    @GetMapping
    public ResponseEntity<List<DiaryResponse.SimpleDto>> getAllDiaries() {
        List<Diary> diaries = diaryService.findAll();

        List<DiaryResponse.SimpleDto> result = new ArrayList<>();
        for (Diary diary : diaries) {
            result.add(DiaryResponse.SimpleDto.of(diary));
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 회원별 일기 목록 조회
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<DiaryResponse.SimpleDto>> getDiariesByMember(@PathVariable Long memberId) {
        List<Diary> diaries = diaryService.findByMemberId(memberId);

        List<DiaryResponse.SimpleDto> result = new ArrayList<>();
        for (Diary diary : diaries) {
            result.add(DiaryResponse.SimpleDto.of(diary));
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 일기 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> getDiary(@PathVariable Long id) {
        try {
            Diary diary = diaryService.findById(id);
            DiaryResponse.DetailDto result = DiaryResponse.DetailDto.of(diary);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // 일기 작성
    @PostMapping
    public ResponseEntity<String> createDiary(@RequestBody DiaryRequest.CreateDto request) {
        if (request == null || request.getMemberId() == null) {
            return new ResponseEntity<>("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
        }

        Diary diary = request.toEntity();
        int result = diaryService.save(diary);

        if (result > 0) {
            return new ResponseEntity<>("일기 작성 성공", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("일기 작성 실패", HttpStatus.BAD_REQUEST);
        }
    }

    // 일기 수정
    @PutMapping("/{id}")
    public ResponseEntity<String> updateDiary(@PathVariable Long id, @RequestBody DiaryRequest.UpdateDto request) {
        try {
            // 일기 존재 여부 확인
            Diary existingDiary = diaryService.findById(id);

            // 수정할 일기 엔티티 생성
            Diary diary = request.toEntity(id, existingDiary.getMemberId());
            int result = diaryService.updateById(diary);

            if (result > 0) {
                return new ResponseEntity<>("일기 수정 성공", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("일기 수정 실패", HttpStatus.BAD_REQUEST);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // 일기 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDiary(@PathVariable Long id) {
        int result = diaryService.deleteById(id);

        if (result > 0) {
            return new ResponseEntity<>("일기 삭제 성공", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("일기 삭제 실패", HttpStatus.NOT_FOUND);
        }
    }
}
