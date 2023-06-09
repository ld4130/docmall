package com.docmall.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.docmall.domain.OrderVO;
import com.docmall.domain.PaymentVO;
import com.docmall.mapper.CartMapper;
import com.docmall.mapper.OrderMapper;

import lombok.Setter;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Setter(onMethod_ = {@Autowired})
	private OrderMapper orderMapper;
	
	//장바구니 Mapper
	@Setter(onMethod_ = {@Autowired})
	private CartMapper cartMapper;

	// XML에서 selectKey를 통해서  OrderVO-ord_code 공유하기떄문에 주문번호를 공유하기때문에 @Transactional를 활용한다.
	@Transactional // 트랙잭션 테스트 할 것. root-context.xml에서 트랜잭션 파일 빈 설정
	@Override
	public void order_save(OrderVO o_vo, PaymentVO p_vo) {
		
		// 주문저장
		orderMapper.order_save(o_vo); // Mapper.xml에서 시퀀스를 통해 주문번호 공유
		
		// 주문상세 저장
		orderMapper.order_detail_save(o_vo.getOrd_code());
		
		// 결제정보 저장
		p_vo.setOrd_code(o_vo.getOrd_code());
		orderMapper.payment_save(p_vo);
		
		// 장바구니 비우기
		cartMapper.cart_empty(o_vo.getMbsp_id());
	}


}
