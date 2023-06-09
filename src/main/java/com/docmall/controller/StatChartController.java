package com.docmall.controller;

import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.docmall.dto.StatChartDTO;
import com.docmall.service.StatChartService;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
@RequestMapping("/admin/chart/*")
@Controller
public class StatChartController {

	@Setter(onMethod_ = {@Autowired})
	private StatChartService statChartService;
	
	// 2차원 배열데이타
	@GetMapping("/overall")
	public void overall(Model model) {
		/*
		List<StatChartDTO> firstCategoryOrderPrice = statChartService.firstCategoryOrderPrice();
		
		
		String firstCategoryData = "[";
		
		firstCategoryData += "['1차 카테고리별', '주문금액'], ";
		
		for(StatChartDTO dto : firstCategoryOrderPrice) {
			
			// ['Bob', 35000]
			firstCategoryData += "['" + dto.getCategoryname() + "', " + dto.getOrderprice() + "]";
			firstCategoryData += ",";
		}
		
		firstCategoryData = firstCategoryData.substring(0, firstCategoryData.lastIndexOf(","));
		firstCategoryData += "]";
		
		log.info("차트데이타: " + firstCategoryData);
		
		model.addAttribute("firstCategoryData", firstCategoryData);
		*/
	}
	
	// JSON데이타		
	@ResponseBody
	@GetMapping("/firstCategoryOrderPrice")
	public ResponseEntity<JSONObject> firstCategoryOrderPrice3() {
		
		ResponseEntity<JSONObject> entity = null;
						
		entity = new ResponseEntity<JSONObject>(statChartService.firstCategoryChart(), HttpStatus.OK);
		
		return entity;
	}
	
}

