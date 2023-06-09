package com.docmall.kakaopay;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
//[2]-1 결제승인 요청시 응답데이터 하위데이터 "Amount: 결제금액정보"
public class Amount {
	
	private int total;		// 전체 결제금액
	private int tax_free;	// 비과세 금액
	private int vat;		// 부가세 금액
	private int point;		// 포인트 금액
	private int discount;	// 할인 금액
}
