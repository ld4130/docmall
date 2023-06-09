package com.docmall.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.docmall.domain.AdminVO;
import com.docmall.dto.AdminDTO;
import com.docmall.service.AdminService;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/admin/*")
@Log4j
public class AdminController {
	
	@Setter(onMethod_ = {@Autowired})   // _(언더바) JDK "1.8"만 사용,
	private AdminService adminService;
	
	@Setter(onMethod_ = {@Autowired})	
	private PasswordEncoder passwordEncoder;
	
	//관리자 로그인페이지
	@GetMapping("")	// 주소가 없을경우 전체 매핑주소로 대체된다.
	public String adLogin() {
		
		return "/admin/adLogin";
	}
	
	//로그인, 매핑 주소 : admin_ok
	@PostMapping("/admin_ok")
	public String admin_ok(AdminDTO dto, HttpSession session, RedirectAttributes rttr) {
		
		log.info("관리자 로그인 정보:" + dto);
		
		AdminVO vo = adminService.admin_ok(dto.getAdmin_id());
		
		String url = "";
		String msg = "";
		
		if(vo != null) {
			if(passwordEncoder.matches(dto.getAdmin_pw(), vo.getAdmin_pw())) {
				session.setAttribute("adminStatus", vo); // 로그인한 관리자정보를 세션형태로 저장
				url = "admin/admin_menu"; //메인 메뉴 페이지 
				
				//로그인한 시간 업데이트작업.
				adminService.now_visit(vo.getAdmin_id());
				
			}else {
				//비번이 틀린경우 
				msg = "failPW";
				url ="admin/";
			}
		}else {
			// 아이디가 틀린경우
			msg = "failID";
			url ="admin/";
		}
			rttr.addFlashAttribute("msg", msg);
			
			//로그인 검사
			
			return "redirect:/" + url;
		}
	
	@GetMapping("admin_menu")
	public void admin_menu() {
		
	}
		
	//로그아웃
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		
		session.invalidate();
		
		return "redirect:/admin/";
	}
}
