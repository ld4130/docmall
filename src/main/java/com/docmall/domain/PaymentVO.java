package com.docmall.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PaymentVO {
	/*
    PAY_CODE    NUMBER CONSTRAINT PK_PAY_CODE PRIMARY KEY,
    ODR_CODE    NUMBER NOT NULL,
    MBSP_ID     VARCHAR2(15) NOT NULL,
    PAY_METHOD  VARCHAR2(15) NOT NULL,
    PAY_PRICE   NUMBER NOT NULL,
    PAY_USER    VARCHAR2(15) NOT NULL,
    PAY_BANK    VARCHAR2(15) NOT NULL,
    PAY_DATE    DATE    DEFAULT SYSDATE,
    PAY_MEMO    VARCHAR(100) NULL
	*/
	
	private Integer pay_code;
	private long ord_code;
	private String mbsp_id;
	private String pay_method;
	private int pay_price;
	private String pay_user;
	private String pay_bank;
	private Date pay_date;
	private String pay_memo;
	
}
