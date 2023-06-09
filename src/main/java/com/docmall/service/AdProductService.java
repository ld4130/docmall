package com.docmall.service;

import java.util.List;

import com.docmall.domain.CategoryVO;
import com.docmall.domain.ProductVO;
import com.docmall.dto.Criteria;


public interface AdProductService {

	List<CategoryVO> getCategoryList();
	
	List<CategoryVO> subCategoryList(Integer cat_code);
	
	void pro_insert(ProductVO vo);
	
	// 1)페이징기능
	public List<ProductVO> getListWithPaging(Criteria cri, Integer cat_code);   //  처음 pageNum = 1, amount = 10, type=null, keyword=null
	// 2)페이징기능
	public int getTotalCount(Criteria cri, Integer cat_code);
	
	ProductVO modify(Integer pro_num);
	
	CategoryVO get(int cat_code);
	
	void update(ProductVO vo);
	
	void delete(Integer pro_num);
	
	void pro_checked_modify2(Integer pro_num,int pro_price, String pro_buy);
	
	void pro_checked_delete(List<Integer> pro_num_arr);

	void pro_checked_modify1(List<ProductVO> pro_list);
	
}
