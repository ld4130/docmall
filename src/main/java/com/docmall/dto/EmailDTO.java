package com.docmall.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class EmailDTO {
	
	private String senderName;		// 발신자 이름
	private String senderMail;		// 발신자 메일주소
	private String receiverMail;	// 수신자 메일주소, 즉 회원메일 주소로 사용
	private String sunject;			// 제목
	private String message;			// 본문 내용
	
	public EmailDTO() {
		this.senderName = "DocMall";
		this.senderMail = "DocMall";
		this.sunject = "DocMall 회원가입 메일인증코드 입니다.";
		this.message = "메일 인증을 위한 인증코드를 확안하고, 회원가입시 인증코드를 입력해주세요.";
	}

}
