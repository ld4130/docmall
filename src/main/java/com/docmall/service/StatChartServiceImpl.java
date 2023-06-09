package com.docmall.service;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.docmall.dto.StatChartDTO;
import com.docmall.mapper.StatChartMapper;

import lombok.Setter;

@Service
public class StatChartServiceImpl implements StatChartService {

	@Setter(onMethod_ = {@Autowired})
	private StatChartMapper statChartMapper;

	@Override
	public List<StatChartDTO> firstCategoryOrderPrice() {
		// TODO Auto-generated method stub
		return statChartMapper.firstCategoryOrderPrice();
	}

	@Override
	public JSONObject firstCategoryChart() {
		/*
		 google chart APi 데이터 구조.
		 categoryData: 
		 {"rows":[{"c":[{"v":"TOP"},{"v":2748000}]}],
		 "cols":[{"label":"1차카테고리이름",
		 "type":"string"},{"label":"주문금액","type":"number"}]}
		 
		 JSONObject 클래스 : JSON데이터를 생성하는 기능
		 - JSONObject obj = new JSONObject();
		 
		 JSONArry 클래스    : JSON데이터를 배열로 생성하는 기능.
		 
		 */
		
		List<Map<String, Object>> list = statChartMapper.firstCategoryChart();
		
		JSONObject data = new JSONObject(); // JSON데이터 전체적인 관점에서 사용
		// 배열 구조 [JSON Data ]
		JSONObject col1 = new JSONObject();
		
		// {"label":"1차 카테고리이름", "type" : "number"}
		JSONObject col2 = new JSONObject();
		
		JSONArray title = new JSONArray();
		col1.put("label", "1차카테고리이름");
		col1.put("type", "string");
		col2.put("label", "주문금액");
		col2.put("type", "number");
		
		title.add(col1);
		title.add(col2);
		
		data.put("cols", title);
		
		JSONArray body = new JSONArray();
		for(Map<String,Object> map : list) {
			
			JSONArray row = new JSONArray();
			
			// 첫 for문 {"v" : " PANTS"}, 둘 for문 {"v" : " Top" }
			JSONObject categoryName = new JSONObject();
			categoryName.put("v", map.get("categoryname"));
			
			// 첫 for문 {"v":100000} , 둘 for문 {"v" 255100}
			JSONObject orderprice = new JSONObject();
			orderprice.put("v", map.get("orderprice"));
			
			// 배열로 추가
			row.add(categoryName);
			row.add(orderprice);
			
			JSONObject cell = new JSONObject();
			cell.put("c", row);
			
			body.add(cell);
		}
		
		data.put("rows", body);
		
		return data;
	}
}
