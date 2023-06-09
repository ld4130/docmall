package com.docmall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.docmall.domain.CategoryVO;
import com.docmall.service.ProductService;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
// com.docmall.controller 패키지안의 컨트롤러 기능을  갖는 컨트롤러에서 설정된 
// 매핑주소가 클라이언트로부터 요청을 받으면, 해당 컨트롤러의 메서드가 동작하기 이전에 이 클래스가 "선" 동작 된다.
@ControllerAdvice(basePackages = {"com.docmall.controller"})
public class GlobalControllerAdvice {
	
	@Setter(onMethod_ = {@Autowired})
	private ProductService productService;
	
	@ModelAttribute
	public void categoryList(Model model) {
		log.info("1차 카테고리 목록");
		
		List<CategoryVO> cateList = productService.getCategoryList();
		model.addAttribute("cateList", cateList);
	}

}
