package com.docmall.service;

import java.util.List;

import com.docmall.domain.CartVO;
import com.docmall.dto.CartListDTO;

public interface CartService {

	int cart_add(CartVO vo);

	List<CartListDTO> cart_list(String mbsp_id);
	
	int cart_amount_change(Long cart_code, int cart_amount);
	
	int cart_delete(Long cart_code);
	
	int cart_tot_price(String mbsp_id);
	
	void cart_empty(String mbsp_id);
}
