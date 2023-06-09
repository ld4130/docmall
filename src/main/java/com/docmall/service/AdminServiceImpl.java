package com.docmall.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.docmall.domain.AdminVO;
import com.docmall.mapper.AdminMapper;

import lombok.Setter;

@Service
public class AdminServiceImpl implements AdminService {

	@Setter(onMethod_ = {@Autowired})
	private AdminMapper adminMapper;

	@Override
	public AdminVO admin_ok(String admin_id) {
		
		return adminMapper.admin_ok(admin_id);
	}

	@Override
	public void now_visit(String admin_id) {
		
		adminMapper.now_visit(admin_id);
		
	}

	
}
