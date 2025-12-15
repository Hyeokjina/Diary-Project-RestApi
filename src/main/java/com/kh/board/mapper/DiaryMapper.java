package com.kh.board.mapper;

import com.kh.board.entity.Diary;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DiaryMapper {
    List<Diary> findAll();
    List<Diary> findByMemberId(Long memberId);
    Diary findById(Long id);
    int save(Diary diary);
    int updateById(Diary diary);
    int deleteById(Long id);
}