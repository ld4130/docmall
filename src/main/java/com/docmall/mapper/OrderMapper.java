package com.docmall.mapper;

import com.docmall.domain.OrderVO;
import com.docmall.domain.PaymentVO;

public interface OrderMapper {

	// 주문정보 저장
	void order_save(OrderVO o_vo);
	
	// 주문상세 : 장바구니 활용
	void order_detail_save(Long ord_code);
	
	// 결제정보
	void payment_save(PaymentVO p_vo);
	
}
