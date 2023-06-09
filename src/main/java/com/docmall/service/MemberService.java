package com.docmall.service;

import com.docmall.domain.MemberVO;

public interface MemberService {

	String idCheck(String mbsp_id);
	
	void join(MemberVO vo);
	
	MemberVO login(String mbsp_id);
	
	void modify(MemberVO vo);
	
	//비밀번호 수정 
	void pwchange(String mbsp_id, String new_mbsp_password);
	
	//현재 로그인한 시간 업데이트
	void now_visit(String mbsp_id);
	
	//회원 삭제
	void delete(String mbsp_id);
}
