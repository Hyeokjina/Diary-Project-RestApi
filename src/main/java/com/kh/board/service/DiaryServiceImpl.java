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

    @Override
    public List<Diary> findByEmotion(String emotion) {
        return diaryMapper.findByEmotion(emotion);
    }

    @Override
    public List<Diary> findByMemberIdAndEmotion(Long memberId, String emotion) {
        return diaryMapper.findByMemberIdAndEmotion(memberId, emotion);
    }

    @Override
    public List<Diary> findByKeyword(String keyword) {
        return diaryMapper.findByKeyword(keyword);
    }

    @Override
    public List<Diary> findByMemberIdAndKeyword(Long memberId, String keyword) {
        return diaryMapper.findByMemberIdAndKeyword(memberId, keyword);
    }
}
