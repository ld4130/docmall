package com.docmall.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.docmall.domain.CategoryVO;
import com.docmall.domain.ProductVO;
import com.docmall.dto.Criteria;

// Category Mapper, Product Mapper 
public interface AdProductMapper {

	// 1차 카테고리.
	List<CategoryVO> getCategoryList();
	
	//2차 카테고리ㅣ
	List<CategoryVO> subCategoryList(Integer cat_code);
	
	//상품 등록
	void pro_insert(ProductVO vo);
	
	// 1)페이징기능 // 카테고리 검색조건은 null값이 허용되기때문에, integer로 null 기본값으로 처리한다.
	List<ProductVO> getListWithPaging(@Param("cri") Criteria cri, @Param("cat_code") Integer cat_code);   //  처음 pageNum = 1, amount = 10, type=null, keyword=null 
	// 2)페이징기능
	int getTotalCount(@Param("cri") Criteria cri, @Param("cat_code") Integer cat_code);
	
	//상품수정
	ProductVO modify(Integer pro_num);
	
	CategoryVO get(int cat_code);
	
	// 상품수정 업데이트
	void update(ProductVO vo);
	// 상품 삭제
	void delete(Integer pro_num);
	
	//선택상품수정1
	void pro_checked_modify1(List<ProductVO> pro_list);

	//선택상품 수정 Mapper interface에서 파라미터가 2개 이상일때 @Param 쓰여야한다.
	void pro_checked_modify2(
			@Param("pro_num")Integer pro_num,
			@Param("pro_price")int pro_price,
			@Param("pro_buy")String pro_buy
			);


	//선택상품 삭제. 1. 파라미티가 컬렉션일 경우에 .xml에서 쓰일  mybasit foreach구문에서는 list
	//	         2. 배열일 경우에는 array			 
	void pro_checked_delete(List<Integer> pro_num_arr);
	
	
}
