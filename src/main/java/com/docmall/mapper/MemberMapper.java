package com.docmall.mapper;

import org.apache.ibatis.annotations.Param;

import com.docmall.domain.MemberVO;

public interface MemberMapper {

	//아이디 중복체크
	String idCheck(String mbsp_id);
	
	//회원가입
	void join(MemberVO vo);
	
	//로그인, 회원수정폼
	MemberVO login(String mbsp_id);
	
	//회원 수정 : 데이터작업은 void
	void modify(MemberVO vo);
	
	//비밀번호 수정 
	void pwchange(@Param("mbsp_id") String mbsp_id,@Param("new_mbsp_password") String new_mbsp_password);
	
	//현재 로그인한 시간 업데이트
	void now_visit(String mbsp_id);
	
	//회원 삭제
	void delete(String mbsp_id);
}
