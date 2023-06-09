package com.docmall.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

//인터셉터 기능을 갖는 클래스는 HandlerInterceptorAdapter 추상클래스를 상속.
public class TestInterceptor extends HandlerInterceptorAdapter {

	
	//매핑주소가 요청되면, 매핑주소가 인터셉터설정으로 되어있으면, 인터셉터 클래스의 preHandle 메서드가 동작된다.
	//1)인터셉터 클래스의 메서드가 "preHandle" 실행된다.
	//2)매핑주소의 Controller로 제어가 넘어가 매핑주소의 메서드가 실행된다. ( 중요 : jsp가 실행되지않는다.)
	//3)인터셉터 클래스의 "postHandle" 로 제어가 넘어가 실행된다.
	//4)인터셉터 클래스의 "afterCompletion" 으로 넘어가 실행된다.
	//5)JSP 작업이 완료된다.
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		System.out.println("preHandle called...");
		
		return super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		super.afterCompletion(request, response, handler, ex);
	}

	@Override
	public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		super.afterConcurrentHandlingStarted(request, response, handler);
	}

}
