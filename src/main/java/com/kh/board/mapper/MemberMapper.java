package com.kh.board.mapper;

import com.kh.board.entity.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
    Member findByEmail(String email);
    int save(Member member);
    int countByEmail(String email);
}
