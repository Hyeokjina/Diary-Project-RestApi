package com.kh.board.service;

import com.kh.board.entity.Member;

public interface MemberService {
    int signup(Member member);
    Member login(String email, String password);
    Member findByEmail(String email);
}
