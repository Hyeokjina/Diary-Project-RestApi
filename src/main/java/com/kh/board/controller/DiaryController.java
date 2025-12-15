package com.kh.board.controller;

import com.kh.board.controller.dto.request.DiaryRequest;
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
@RequestMapping("/api/diaries")
public class DiaryController {

    private final DiaryService diaryService;

    // 전체 일기 목록 조회 (감정 필터링, 검색 지원)
    @GetMapping
    public ResponseEntity<ApiResponse<List<DiaryResponse.SimpleDto>>> getAllDiaries(
            @RequestParam(required = false) String emotion,
            @RequestParam(required = false) String keyword) {
        
        List<Diary> diaries;
        
        if (emotion != null && !emotion.trim().isEmpty()) {
            // 감정별 필터링
            diaries = diaryService.findByEmotion(emotion);
        } else if (keyword != null && !keyword.trim().isEmpty()) {
            // 검색
            diaries = diaryService.findByKeyword(keyword);
        } else {
            // 전체 조회
            diaries = diaryService.findAll();
        }

        List<DiaryResponse.SimpleDto> result = new ArrayList<>();
        for (Diary diary : diaries) {
            result.add(DiaryResponse.SimpleDto.of(diary));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(result));
    }

    // 일기 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DiaryResponse.DetailDto>> getDiary(@PathVariable Long id) {
        try {
            Diary diary = diaryService.findById(id);
            DiaryResponse.DetailDto result = DiaryResponse.DetailDto.of(diary);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.success(result));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    // 일기 작성
    @PostMapping
    public ResponseEntity<ApiResponse<DiaryResponse.DetailDto>> createDiary(@RequestBody DiaryRequest.CreateDto request) {
        if (request == null || request.getMemberId() == null || 
            request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("제목과 회원 ID는 필수입니다."));
        }

        Diary diary = request.toEntity();
        int result = diaryService.save(diary);

        if (result > 0) {
            // MyBatis의 useGeneratedKeys로 생성된 ID가 diary 객체에 자동 설정됨
            DiaryResponse.DetailDto response = DiaryResponse.DetailDto.of(diary);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("일기가 작성되었습니다.", response));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("일기 작성에 실패했습니다."));
        }
    }

    // 일기 수정
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DiaryResponse.DetailDto>> updateDiary(@PathVariable Long id, @RequestBody DiaryRequest.UpdateDto request) {
        try {
            // 일기 존재 여부 확인
            Diary existingDiary = diaryService.findById(id);

            // 수정할 일기 엔티티 생성
            Diary diary = request.toEntity(id, existingDiary.getMemberId());
            int result = diaryService.updateById(diary);

            if (result > 0) {
                Diary updatedDiary = diaryService.findById(id);
                DiaryResponse.DetailDto response = DiaryResponse.DetailDto.of(updatedDiary);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(ApiResponse.success("일기가 수정되었습니다.", response));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("일기 수정에 실패했습니다."));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    // 일기 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDiary(@PathVariable Long id) {
        try {
            // 일기 존재 여부 확인
            diaryService.findById(id);
            
            int result = diaryService.deleteById(id);

            if (result > 0) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(ApiResponse.success("일기가 삭제되었습니다.", null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("일기 삭제에 실패했습니다."));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
