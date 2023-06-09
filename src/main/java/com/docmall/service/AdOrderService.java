package com.docmall.service;

import java.util.List;

import com.docmall.domain.AdOrderDetailVO;
import com.docmall.domain.OrderVO;
import com.docmall.dto.Criteria;

public interface AdOrderService {

	List<OrderVO> orderList(Criteria cri, String sDate, String eDate);
	
	int getTotalCount(Criteria cri, String sDate, String eDate);
	
	List<AdOrderDetailVO> orderDetail(Long ord_code);
	
	void orderDetailProductDelete(Long ord_code, Integer pro_num);
}
