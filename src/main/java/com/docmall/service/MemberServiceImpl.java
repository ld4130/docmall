package com.docmall.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.docmall.domain.MemberVO;
import com.docmall.mapper.MemberMapper;

import lombok.Setter;

@Service
public class MemberServiceImpl implements MemberService{
	
	@Setter(onMethod_ = {@Autowired})	// jdk 1.8 _언더바 사용.
	private MemberMapper memberMapper;

	@Override
	public String idCheck(String mbsp_id) {
		// TODO Auto-generated method stub
		return memberMapper.idCheck(mbsp_id);
	}

	@Override
	public void join(MemberVO vo) {
		
		memberMapper.join(vo);
	}

	@Override
	public MemberVO login(String mbsp_id) {
		// TODO Auto-generated method stub
		return memberMapper.login(mbsp_id);
	}

	@Override
	public void modify(MemberVO vo) {
		
		memberMapper.modify(vo);
		
	}

	@Override
	public void pwchange(String mbsp_id, String new_mbsp_password) {
		
		memberMapper.pwchange(mbsp_id, new_mbsp_password);
		
	}

	@Override
	public void now_visit(String mbsp_id) {
		
		memberMapper.now_visit(mbsp_id);
	}

	@Override
	public void delete(String mbsp_id) {
		
		memberMapper.delete(mbsp_id);
	}





	
	
}
