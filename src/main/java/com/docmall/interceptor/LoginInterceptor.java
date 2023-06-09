package com.docmall.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.docmall.domain.MemberVO;

import lombok.extern.log4j.Log4j;

// 사용자 로그인 인증관리 인터셉터 가능.
@Log4j
public class LoginInterceptor extends HandlerInterceptorAdapter {@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		log.info("로그인 인증 인터셉터: preHandle");
		
		HttpSession session = request.getSession();
		
		MemberVO loginAuth = (MemberVO)session.getAttribute("loginStatus");
		
		//로그인 세션이 없을경우, 로그인 화면 출력
		if(loginAuth == null) {
			
			
			if(isAjaxRequest(request)) {
				
				// AJAX요청은 로그인 JSP를 사용불가능. 이유는 리턴값만 부분 업데이트 성격으로 받는 특징때문에,
				// response.sendRedirect("member/login");
				
				response.sendError(400);
			}else {
				
				// 사용자가 로그인으로 강제 진행되기전 요청했던 주소를 저장하는 작업. ( 원래 요청된 주소 )
				getTargetUrl(request);
				response.sendRedirect("/member/login");
			}	
			
			return false;
		}
		
		return true; // 인증된 사용자 일 경우 요청한 컨트롤러의 매핑주소로 제어가 넘어간다.
	}

	private void getTargetUrl(HttpServletRequest request) {
		
		String uri = request.getRequestURI();
		String query = request.getQueryString();
		
		if(query == null || query.equals(null)){
			query = "";
		}else {
			query = "?" + query;
		}
		
		String targetUrl = uri + query;
		
		// 요청방식, GET/POST/PATCH/PUT/DELETE 등
		if(request.getMethod().equals("GET")) {
			
			log.info("dest: " + targetUrl);
			// 원래요청주소를 세션으로 저장
			request.getSession().setAttribute("dest", targetUrl); // 로그인 인증 처리작업에서 참조해야한다.
		}
	}
	
	// ajax요청 구분
	private boolean isAjaxRequest(HttpServletRequest request) {
		
		boolean isAjax = false;
		
		String header = request.getHeader("AJAX"); // 클라이언트 ajax호출하는 부분에서 작업이 선행되어야 한다.
		if(header != null && header.equals("true")) {
			isAjax = true;
		}
		
		return isAjax;
	}
}
