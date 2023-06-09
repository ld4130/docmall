package com.docmall.kakaopay;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
//[1]카카오 결제준비 요청에 의한 응답 데이터(첫 번째 요청) https://kapi.kakao.com/v1/payment/ready
public class ReadyResponse {

	/*
	 "tid": "T1234567890123456789",
	 "next_redirect_app_url": "https://mockup-pg-web.kakao.com/v1/xxxxxxxxxx/aInfo",
	 "next_redirect_mobile_url": "https://mockup-pg-web.kakao.com/v1/xxxxxxxxxx/mInfo",
	 "next_redirect_pc_url": "https://mockup-pg-web.kakao.com/v1/xxxxxxxxxx/info",
	 "android_app_scheme": "kakaotalk://kakaopay/pg?url=https://mockup-pg-web.kakao.com/v1/xxxxxxxxxx/order",
	 "ios_app_scheme": "kakaotalk://kakaopay/pg?url=https://mockup-pg-web.kakao.com/v1/xxxxxxxxxx/order",
	 "created_at": "2016-11-15T21:18:22"
	 */
	
	// 메뉴얼 response O
	private String tid; // 단일결제 고유번호, 최대 20자
	// 요청한 클라이언트가 PC WEB일 경우, 카카오토으로 결제요청 메시지(TMS)를 보내기위한 사용자 정보 입력화면 Redirect URL
	private String next_redirect_pc_url;
	
	// 메뉴얼 response X
	//	private String partner_order_id; // 가맹점 주문번호 , 최대 100자
	
	private String pay_type;
	
}
