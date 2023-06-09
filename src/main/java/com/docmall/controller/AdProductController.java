package com.docmall.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.docmall.domain.CategoryVO;
import com.docmall.domain.ProductVO;
import com.docmall.dto.Criteria;
import com.docmall.dto.PageDTO;
import com.docmall.service.AdProductService;
import com.docmall.util.FileUtils;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
@Controller
@RequestMapping("/admin/product/*")
public class AdProductController {
	
	@Setter(onMethod_ = {@Autowired})   // _(언더바) JDK "1.8"만 사용,
	private AdProductService adProductService;

	//4.업로드 폴더 주입작업 
	@Resource(name = "uploadPath") // servlet-context.xml 참조
	private String uploadPath; // C:\\dev\\upload\\pds\\
	
	//6.CKEditor 폴더 주입작업
	@Resource(name = "uploadCkEditorPath") // servlet-context.xml 참조
	private String uploadCkEditorPath; // C:\\dev\\upload\\ckeditor\\
	
	//1.상품등록 폼
	@GetMapping("/pro_insert")
	public void pro_insert(Model model) {
		// 필요한 리스트가 있을때 model로 뽑아낸다.
	
		model.addAttribute("categoryList", adProductService.getCategoryList());
	}
	//2
	@ResponseBody
	@GetMapping("/subCategory/{cat_code}")
	public ResponseEntity<List<CategoryVO>> subCategory(@PathVariable("cat_code") Integer cat_code){
		
		ResponseEntity<List<CategoryVO>> entity = null;
		
		entity = new ResponseEntity<List<CategoryVO>>(adProductService.subCategoryList(cat_code), HttpStatus.OK);
		
		return entity;
	}
	
	//3.상품정보 저장 : 프레임만 생성, 4. 업로드 폴더 주입작업 진행후 상품정보 스코프 완성
	@PostMapping("/pro_insert")
	public String pro_insert(ProductVO vo, RedirectAttributes rttr) {
		
		
		log.info("상품정보: " + vo);
		
		String uploadDateFolder = FileUtils.getFolder();
		String saveImageName = FileUtils.uploadFile(uploadPath, uploadDateFolder, vo.getUploadFile());
		
		vo.setPro_img(saveImageName); // 실제 업로드된 파일명
		vo.setPro_up_folder(uploadDateFolder); // 날짜폴더명
		
		log.info("상품정보: " + vo);
		
		//상품테이블에 db작업
		//pro_insert
		adProductService.pro_insert(vo);
		
		return "redirect:/admin/product/pro_list";
	}

	//5.CKEditor에서 파일업로드 사용 : pro_insert.jsp 258 Line - filebrowserUploadUrl : '/admin/product/imageUpload'
	// -> 파일이 업로드되고, 파일정보를 CKEditor에 반환한다.
	// CKEditor에서 사용되는 이미지업로드 -> "파일선택" 기능 : 개발자도구 검사로 해당 name값 과 멀티파일 파라미터 이름을 일치시켜줘야한다.
	// -> <input style="width:100%" id="cke_124_fileInput_input" aria-labelledby="cke_123_label" type="file" name="upload" size="38">
	@PostMapping("/imageUpload")
	public void imageUpload(HttpServletRequest req, HttpServletResponse res, MultipartFile upload) {
		
		OutputStream out = null;
		PrintWriter printWriter = null;
		
		// 클라이언트에게 보내는 정보 설명
		// 서버에서 클라이언트에게 정보를 보낼때, 브라우저에 설명의미로 해당 인코딩과 타입을 입력해야한다.
		res.setCharacterEncoding("utf-8");
		res.setContentType("text/html; charset=utf-8");
		
		try {
			// 1) 업로드 작업
			String fileName = upload.getOriginalFilename(); // 클라리언트에서 서버로 보내는 파일이름
			byte[] bytes = upload.getBytes();	// 클라이언트에서 보낸 업로드 파일을 바이트배열로 확보.
			
			String uploadPath = uploadCkEditorPath + fileName;
			
			log.info("파일업로드 : " + uploadPath);
			
			out = new FileOutputStream(new File(uploadPath));
			out.write(bytes);
			out.flush();
			
			// 2) 업로드된 파일정보를 CKEditor에게 보내는 작업
			// 클라이언트에서 서버측에 객체를 보내는 작업
			printWriter = res.getWriter();
			
			// 1. 톰캣 의 환경설정 Context Path에서 Add External WEB Module 경로와 업르도드될 브라우저를 설정해야한다. 
			// Path : /upload , Document Base : C:\\dev\\upload\\ckeditor 설정
			// 2. 또는 직접 톰캣 서버 경로설정
			// 톰캣 server.xml에서 <Context docBase="업로드 경로" path="/upload" reloadable="true"/> 
			String fileUrl = "/upload/" + fileName;
			
			// {"filename":"abc.gif", "uploaded":1, "url":"/upload/abc.gif"}
			printWriter.println("{\"filename\":\"" + fileName + "\", \"uploaded\":1,\"url\":\"" + fileUrl + "\"}"); // 오타주의
			
			printWriter.flush();
			
		}catch (Exception ex) {
			ex.printStackTrace();
		}finally {
			if(out != null) {
				try {
					out.close();
				}catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			if(printWriter != null) printWriter.close();
		}
		
	}
	
	//페이징, 검색기능
	@GetMapping("/pro_list")
	public void pro_list(@ModelAttribute("cri")Criteria cri, Integer cat_code, Model model) {
		
		log.info("파라미터: " + cri);
		
		//검색기능 사용후, 카테고리 검색시 검색파라미터 초기화
		if(cat_code != null) {
			cri = new Criteria();
		}
		
		model.addAttribute("categoryList", adProductService.getCategoryList());
		
		//Model객체 - 목록데이터
		List<ProductVO> pro_list = adProductService.getListWithPaging(cri, cat_code);
		
//		log.info("폴더경로: " + pro_list.get(0).getPro_up_folder());
		
		pro_list.forEach(vo -> {
			vo.setPro_up_folder(vo.getPro_up_folder().replace("\\", "/"));
		});
		
		model.addAttribute("pro_list", pro_list); // JSP에서 참조하게될 이름 지정

		int totalCount = adProductService.getTotalCount(cri, cat_code);	
		model.addAttribute("pageMaker", new PageDTO(totalCount, cri)); // pageDTO객체안에 시작,끝 페이지 데이터존재 
	}
	
	// 이미지 매핑주소. // <img scr="text.gif">, <img scr="매핑주소">
	// pro_list.jsp파일에서 <img scr="/admin/product/dispalyImage?folderName=값&fileName=값">
	@ResponseBody
	@GetMapping("/displayImage")
	public ResponseEntity<byte[]> displayFile(String folderName, String fileName) throws IOException{
		
		//
		return FileUtils.getFile(uploadPath + folderName, fileName);
	}
	
	@GetMapping("/pro_modify")
	public void modify(Integer pro_num, int cat_code, @ModelAttribute("cri") Criteria cri, Model model) {
		
		// 상품수정 폼
		ProductVO productVO = adProductService.modify(pro_num);
		productVO.setPro_up_folder(productVO.getPro_up_folder().replace("\\", "/"));
		
		// 2차 카테고리 정보
		model.addAttribute("productVO", productVO);
		
		// 1차 카테고리 정보
		CategoryVO categoryVO = adProductService.get(cat_code);
		model.addAttribute("CategoryVO", categoryVO);
		
		// 1차 카테고리 목록
		model.addAttribute("categoryList", adProductService.getCategoryList());
		// 1차 카테고리를 참조하는 2차 카테고리 목록
		model.addAttribute("subCategoryList", adProductService.subCategoryList(categoryVO.getCat_code()));
	}
	
	@PostMapping("/pro_modify")
	public String modify(ProductVO vo, RedirectAttributes rttr) {
		
		//상품이미지 업로드(변경) 한 경우, * keyPoint : " ! " 
		if(!vo.getUploadFile().isEmpty()) {
			
			// 1)기존 상품이미지 삭제
			FileUtils.deleteFile(uploadPath, vo.getPro_up_folder(), vo.getPro_img());
			// 2)변경 상품이미지 업로드작업
			String uploadDateFolder = FileUtils.getFolder();
			String saveImageName = FileUtils.uploadFile(uploadPath, uploadDateFolder, vo.getUploadFile());
			
			vo.setPro_img(saveImageName); // 실제 업로드된 파일명
			vo.setPro_up_folder(uploadDateFolder); // 날짜폴더명
		}
		
		// 공통 : 상품수정 작업. modify()
		adProductService.update(vo);
		
		rttr.addFlashAttribute("msg", "modify");
		
		
		return "redirect:/admin/product/pro_list";
	}
	
	@PostMapping("/pro_delete")
	public String pro_delete(Integer pro_num, Criteria cri, RedirectAttributes rttr ) {
		
		adProductService.delete(pro_num);
		
		rttr.addFlashAttribute("msg", "delete");
		
		return "redirect:/admin/product/pro_list";
	}
	
	// 선택상품수정 , jsp에서 전달받을 파라미터데이터가 배열성격이므로 파라미터를 배열로 설정해줘야 한다.
	@ResponseBody
	@PostMapping("/pro_checked_modify")
	public ResponseEntity<String> 
			pro_checked_modify(
					@RequestParam("pro_num_arr[]") List<Integer> pro_num_arr,
					@RequestParam("pro_price_arr[]") List<Integer> pro_price_arr,
					@RequestParam("pro_buy_arr[]") List<String> pro_buy_arr) {
		
		ResponseEntity<String> entity = null;
		
		
		log.info("수정상품코드: " + pro_num_arr);
		log.info("수정상품가격: " + pro_price_arr);
		log.info("수정상품판매여부: " + pro_buy_arr);
		
		// 수정작업 방법1.
		List<ProductVO> pro_list = new ArrayList<ProductVO>();
		
		for(int i=0; i<pro_num_arr.size(); i++) {
			
			ProductVO productVO = new ProductVO();
			productVO.setPro_num(pro_num_arr.get(i));
			productVO.setPro_price(pro_price_arr.get(i));
			productVO.setPro_buy(pro_buy_arr.get(i));
			
			pro_list.add(productVO);
		}
		
		adProductService.pro_checked_modify1(pro_list);

		// 수정작업 방법2.
		/* 
		for(int i=0; i<pro_num_arr.size(); i++) {
			adProductService.pro_checked_modify2(pro_num_arr.get(i), pro_price_arr.get(i), pro_buy_arr.get(i));
		}
		*/
		
		entity = new ResponseEntity<String>("success", HttpStatus.OK);
		
		return entity;
	}
	
	//선택상품 삭제
	@ResponseBody
	@PostMapping("/pro_checked_delete")
	public ResponseEntity<String> pro_checked_delete(@RequestParam("pro_num_arr[]") List<Integer> pro_num_arr){
		
		ResponseEntity<String> entity = null;
		
		// 삭제작업. mybatis 문법
		// 선택1. Connection객체 하나로 sql구문 실행 : 성능 향상
		adProductService.pro_checked_delete(pro_num_arr);
		
		entity = new ResponseEntity<String>("success", HttpStatus.OK);
		
		// 선택2. Connection객체 for문을 반복횟수 만큼 생성, sql구문 개별실행 : 성능저하 
		/*
		for(int i=0; i<pro_num_arr.size(); i++) {	
		}
		*/
		log.info("수정삭제코드: " + pro_num_arr);
		
		return entity;
	}
	
}
