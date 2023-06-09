package com.docmall.service;

import com.docmall.domain.AdminVO;

public interface AdminService {
	
	//로그인 
	AdminVO admin_ok(String admin_id);
	
	void now_visit(String admin_id);
}
