package com.docmall.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.docmall.domain.CategoryVO;
import com.docmall.domain.ProductVO;
import com.docmall.dto.Criteria;
import com.docmall.dto.PageDTO;
import com.docmall.service.ProductService;
import com.docmall.util.FileUtils;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
@RequestMapping("/product/*")
@Controller
public class ProductController {
	
	@Setter(onMethod_ = {@Autowired})
	private ProductService productService;
	
	//4.업로드 폴더 주입작업 
	@Resource(name = "uploadPath") // servlet-context.xml 참조
	private String uploadPath; // C:\\dev\\upload\\pds\\

	@ResponseBody	// jackson라이브러리 필수
	@GetMapping("/subCategory/{cat_code}")  // product/subCategory/{1}
	public ResponseEntity<List<CategoryVO>> subCategory(@PathVariable("cat_code") Integer cat_code){
		
		ResponseEntity<List<CategoryVO>> entity = null;
		
		entity = new ResponseEntity<List<CategoryVO>>(productService.subCategoryList(cat_code), HttpStatus.OK);
		
		return entity;
	}
	
	@GetMapping("/pro_list/{cat_code}/{cat_name}")
	public String pro_list(
			@ModelAttribute("cri")Criteria cri,
			@PathVariable("cat_code") Integer cat_code,
			@PathVariable("cat_name") String cat_name, 
			Model model) {
		
		List<ProductVO> pro_list = productService.pro_list(cat_code, cri);
		
		pro_list.forEach(vo -> {
			vo.setPro_up_folder(vo.getPro_up_folder().replace("\\", "/"));
		});
		
		model.addAttribute("pro_list", pro_list);
		
		int count = productService.pro_count(cat_code, cri);
		PageDTO pageDTO = new PageDTO(count, cri);
		
		model.addAttribute("pageMaker", pageDTO);
		
		return "/product/pro_list";
	}
	
	// 이미지 매핑주소. // <img scr="text.gif">, <img scr="매핑주소">
	// pro_list.jsp파일에서 <img scr="/admin/product/dispalyImage?folderName=값&fileName=값">
	@ResponseBody
	@GetMapping("/displayImage")
	public ResponseEntity<byte[]> displayFile(String folderName, String fileName) throws IOException{
		
		//
		return FileUtils.getFile(uploadPath + folderName, fileName);
	}
	
	//5.상품상세 
	@GetMapping("/pro_detail")
	public void pro_detail(Integer pro_num,  @ModelAttribute("cri") Criteria cri, Model model) {
		
		ProductVO vo = productService.pro_detail(pro_num);
		vo.setPro_up_folder(vo.getPro_up_folder().replace("\\", "/"));
		
	    
		model.addAttribute("productVO", vo);

	}
	
}
