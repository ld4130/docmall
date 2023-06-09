package com.docmall.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class OrderListDTO {
	
	private int num;
	private Long cart_code;
	private String pro_up_folder;
	private String pro_img;
	private int pro_num;
	private String pro_name;
	private int pro_price;
	private int cart_amount;
	private int unitprice;

}
