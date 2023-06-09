package com.docmall.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PageDTO {

	//페이지 시작, 끝 번호 지정
	private int startPage;
	private int endPage;
	
	
	// 이전 다음 표시
	private boolean prev, next;
	
	// 테이블 총 데이터 개수 
	private int total;
	
	//페이징정보(pageNum, amount)검색필드 ( type, keyword)
	private Criteria cri;
	
	public PageDTO(int total, Criteria cri) {
		
		this.cri = cri;
		this.total = total;
		
		// 블럭당 출력할 페이지 개수 
		int pageSize = 10;
		// endpage필드 값을 구하기 위한 용도
		int endPageInfo = pageSize - 1;
		
		this.endPage = (int) Math.ceil(cri.getPageNum() / (double) pageSize) * pageSize;
		this.startPage = this.endPage - endPageInfo;
		
		// 게시판 테이블의 실제데이터 개수에 따른 마지막페이지 수 구하기
		int realEnd = (int) Math.ceil((total * 1.0) / cri.getAmount());
		
		//실제 페이지 수 <= (논리) 마지막 페이지 수 
		if(realEnd <= this.endPage) {
			this.endPage = realEnd; // 페이지 데이터 개수에 따른 실제 페이지수 
		}
		
		// [next] 블럭에 표시할 데이터가 존재한다는 의미
		this.prev = this.startPage > 1;
		// 실제 페이지수가 더 크면 다음 블럭에 표시할 데이터가 존재한다는 의미
		this.next = this.endPage < realEnd; 
		
	}
}
