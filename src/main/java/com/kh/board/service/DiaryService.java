package com.kh.board.service;

import com.kh.board.entity.Diary;

import java.util.List;

public interface DiaryService {

    List<Diary> findAll();
    List<Diary> findByMemberId(Long memberId);
    Diary findById(Long id);
    int save(Diary diary);
    int updateById(Diary diary);
    int deleteById(Long id);
}