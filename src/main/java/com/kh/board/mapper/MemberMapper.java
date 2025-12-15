package com.kh.board.mapper;

import com.kh.board.entity.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
    Member findByEmail(String email);
    Member findById(Long id);
    int save(Member member);
    int updateById(Member member);
    int deleteById(Long id);
    int countByEmail(String email);
    int countByEmailExcludingId(String email, Long id);
}
