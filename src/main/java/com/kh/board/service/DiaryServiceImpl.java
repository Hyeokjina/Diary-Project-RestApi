package com.kh.board.service;

import com.kh.board.entity.Diary;
import com.kh.board.mapper.DiaryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DiaryServiceImpl implements DiaryService {

    private final DiaryMapper diaryMapper;

    @Override
    public List<Diary> findAll() {
        return diaryMapper.findAll();
    }

    @Override
    public List<Diary> findByMemberId(Long memberId) {
        return diaryMapper.findByMemberId(memberId);
    }

    @Override
    public Diary findById(Long id) {
        Diary diary = diaryMapper.findById(id);
        if (diary == null) {
            throw new IllegalArgumentException("해당 일기를 찾을 수 없습니다.");
        }
        return diary;
    }

    @Override
    public int save(Diary diary) {
        return diaryMapper.save(diary);
    }

    @Override
    public int updateById(Diary diary) {
        return diaryMapper.updateById(diary);
    }

    @Override
    public int deleteById(Long id) {
        return diaryMapper.deleteById(id);
    }
}
