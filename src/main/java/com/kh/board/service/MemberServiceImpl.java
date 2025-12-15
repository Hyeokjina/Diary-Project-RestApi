package com.kh.board.service;

import com.kh.board.entity.Member;
import com.kh.board.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;

    @Override
    public int signup(Member member) {
        // 이메일 중복 체크
        if (memberMapper.countByEmail(member.getEmail()) > 0) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        return memberMapper.save(member);
    }

    @Override
    public Member login(String email, String password) {
        Member member = memberMapper.findByEmail(email);

        if (member == null) {
            throw new IllegalArgumentException("존재하지 않는 이메일입니다.");
        }

        if (!member.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return member;
    }

    @Override
    public Member findByEmail(String email) {
        return memberMapper.findByEmail(email);
    }

    @Override
    public Member findById(Long id) {
        Member member = memberMapper.findById(id);
        if (member == null) {
            throw new IllegalArgumentException("해당 회원을 찾을 수 없습니다.");
        }
        return member;
    }

    @Override
    public int updateById(Member member) {
        // 이메일 중복 체크 (본인 제외)
        if (memberMapper.countByEmailExcludingId(member.getEmail(), member.getId()) > 0) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        return memberMapper.updateById(member);
    }

    @Override
    public int deleteById(Long id) {
        // 회원 존재 여부 확인
        findById(id);
        return memberMapper.deleteById(id);
    }
}
