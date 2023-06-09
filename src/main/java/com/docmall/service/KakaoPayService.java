package com.docmall.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.docmall.kakaopay.ApproveResponse;
import com.docmall.kakaopay.ReadyResponse;

@Service
public class KakaoPayService {

	
	/*
	 URL 요청 작업을 하기위한 클래스
	 - HttpURLConnection 클래스 : HTTP통신지원
	 - RestTemplate 클래스 : JSON데이터 -> 객체변환 가능 (스프링에서 지원함, 권장)
	 
	[1]결제 준비요청 -기본정보
	POST /v1/payment/ready HTTP/1.1
	Host: kapi.kakao.com
	- 헤더
	Authorization: KakaoAK ${APP_ADMIN_KEY}
	Content-type: application/x-www-form-urlencoded;charset=utf-8
	
	- 요청파라미터
	Request Parameter : Kakao API 참조. 
	*/
	
	//[1]결제준비 요청 ReadyResponse - https://kapi.kakao.com/v1/payment/ready
	public ReadyResponse payReady(
			String cid, Long partner_order_id, String partner_user_id, 
			String item_name, int quantity, int total_amount, int tax_free_amount,
			String approval_url, String cancel_url, String fail_url) {
		// 가맹점 코드, 주문번호, 회원, 상품명, 수량, 총금액, 부과세, 결제 성공.실패.취소 
		
		String url = "https://kapi.kakao.com/v1/payment/ready";
		
		//요청에 사용될 파라미터 
		//하나의 Key에 여러개의 Value을 저장가능. (스프링 지원). 참고 - Map 컬렉션 : 하나의 K 에  하나의 V 저장 (key 중복불가)
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
		parameters.add("cid", cid);
		parameters.add("partner_order_id", String.valueOf(partner_order_id));
		parameters.add("partner_user_id", partner_user_id);
		parameters.add("item_name", item_name);
		parameters.add("quantity", String.valueOf(quantity));
		parameters.add("total_amount", String.valueOf(total_amount));
		parameters.add("tax_free_amount", "0");
		parameters.add("approval_url", approval_url);
		parameters.add("cancel_url", cancel_url);
		parameters.add("fail_url", fail_url);
		
		//Header 작업
		//HttpEntity 클래스 : 헤더와 뭄체를 하나로 관리하는 기능제공. HttpHeader, HttpBody 를 포함 
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String,String>>(parameters, getHeaders());
		
		//외부API(카카오페이 서버 API)
		RestTemplate template = new RestTemplate();	
		ReadyResponse readyResponse = template.postForObject(url, requestEntity, ReadyResponse.class);
		
		return readyResponse;
	}
	
	/*
	[2]결제 승인 요청하기 - 기본정보
	POST /v1/payment/approve HTTP/1.1
	Host: kapi.kakao.com
	Authorization: KakaoAK ${APP_ADMIN_KEY}
	Content-type: application/x-www-form-urlencoded;charset=utf-8
	 */

	//[2]결제승인 요청 ApproveResponse - https://kapi.kakao.com/v1/payment/approve
	public ApproveResponse payApprove(
			String cid, String tid, long partner_order_id, 
			String partner_user_id, String pg_token) {
		//가맹점 코드, 결제 고유번호, 주문번호, 회원, 결제승인 요청하는 토큰
		
		String url = "https://kapi.kakao.com/v1/payment/approve";
		
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
		parameters.add("cid", cid);
		parameters.add("tid", tid);
		parameters.add("partner_order_id", String.valueOf(partner_order_id));
		parameters.add("partner_user_id", partner_user_id);
		parameters.add("pg_token", pg_token);
		
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String,String>>(parameters, getHeaders());
		
		//외부API(카카오페이 서버 API)
		RestTemplate template = new RestTemplate();	
		ApproveResponse approveResponse = template.postForObject(url, requestEntity, ApproveResponse.class);
		
		return approveResponse;
	}
	
	//HttpEntity.class("body","header")
	//header 공통정보
	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "KakaoAK 2fd79433bc5498e91c162dfe73a02695");
		headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		return headers;
	}
	
	
	
	
	
	
	
	
	
}
