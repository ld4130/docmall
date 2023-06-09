package com.docmall.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.docmall.domain.AdOrderDetailVO;
import com.docmall.domain.OrderVO;
import com.docmall.dto.Criteria;

public interface AdOrderMapper {
	
	List<OrderVO> orderList(@Param("cri") Criteria cri,@Param("sDate") String sDate,@Param("eDate") String eDate);
	
	int getTotalCount(@Param("cri") Criteria cri, @Param("sDate") String sDate, @Param("eDate") String eDate);
	
	List<AdOrderDetailVO> orderDetail(Long ord_code);

	void orderDetailProductDelete(@Param("ord_code") Long ord_code, @Param("pro_num") Integer pro_num);
}
