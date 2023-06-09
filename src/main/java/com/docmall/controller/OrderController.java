package com.docmall.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.docmall.domain.MemberVO;
import com.docmall.domain.OrderVO;
import com.docmall.domain.PaymentVO;
import com.docmall.dto.CartListDTO;
import com.docmall.kakaopay.ApproveResponse;
import com.docmall.kakaopay.ReadyResponse;
import com.docmall.service.CartService;
import com.docmall.service.KakaoPayService;
import com.docmall.service.MemberService;
import com.docmall.service.OrderService;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
@RequestMapping("/order/*")
@Controller
public class OrderController {

	@Resource(name = "cid")
	private String cid;
	
	@Setter(onMethod_ = {@Autowired})
	private OrderService orderService;
	
	@Setter(onMethod_ = {@Autowired})
	private CartService cartService;
	
	@Setter(onMethod_ = {@Autowired})
	private MemberService memberService;
	
	@Setter(onMethod_ = {@Autowired})
	private KakaoPayService kakaoPayService;
	
	@GetMapping("/order_info")
	public void order_info(HttpSession session, Model model) {
		
		String mbsp_id = ((MemberVO) session.getAttribute("loginStatus")).getMbsp_id();
		
		// 장바구니 상품
		List<CartListDTO> cart_list = cartService.cart_list(mbsp_id);
		
		cart_list.forEach(vo -> {
			vo.setPro_up_folder(vo.getPro_up_folder().replace("\\", "/"));
		});
		
		// 주문상품명
		String order_productName = cart_list.get(0).getPro_name() + "외 " + (cart_list.size() - 1) + " 건"; 
		model.addAttribute("order_productName", order_productName);
		// 주문목록
		model.addAttribute("cart_list", cart_list);
		
		// 주문 총금액
		model.addAttribute("cart_tot_price", cartService.cart_tot_price(mbsp_id));
		
		// 주문자정보
		model.addAttribute("memberVO", memberService.login(mbsp_id));
		
	}
	
	
	// 주문하기 :  결제준비요청 
	@GetMapping("/orderbuy")
	@ResponseBody
	public ReadyResponse orderPay(String pay_type, String order_productName, OrderVO o_vo, PaymentVO p_vo, HttpSession session) {
		
		ReadyResponse readyResponse = new ReadyResponse();
		
		String mbsp_id = ((MemberVO) session.getAttribute("loginStatus")).getMbsp_id();
		
		o_vo.setMbsp_id(mbsp_id); // 아이디저장.
		p_vo.setMbsp_id(mbsp_id); // 아이디저장.
		
		log.info("결제방식: " + pay_type);
		log.info("무통장주문정보: " + o_vo);
		log.info("무통장결제정보: " + p_vo);
		
		
		
		// 주문정보, 주문상제정보, 결제정보, 장바구니비우기
		if(pay_type.equals("bank")) {
			orderService.order_save(o_vo, p_vo); // 무통장 처리
		}
		
		
		// 카카오페이 결제
		if(pay_type.equals("kakaopay")) {
		
		p_vo.setPay_user(p_vo.getMbsp_id());
		p_vo.setPay_bank("kakaopay");
		
		log.info("카카오페이결제정보: " + p_vo);
		
		orderService.order_save(o_vo, p_vo);  // 카카오페이 결제처리
				
		// 결제준비요청 성공시 사용주소
		String approval_url = "http://localhost:8081/order/orderApproval";
		// 결제준비요청 취소시 사용주소
		String cancel_url = "http://localhost:8081/order/orderCancel";
		// 결제실패시 사용주소
		String fail_url = "http://localhost:8081/order/orderFail";
		
		// 결제준비요청 작업및 응답데이타
		readyResponse = kakaoPayService.payReady(cid, o_vo.getOrd_code(), mbsp_id, order_productName, 1, o_vo.getOrd_price(), 0, approval_url, cancel_url, fail_url);
		
		log.info("결제준비요청: " + readyResponse);
		log.info("결제고유번호(tid): " + readyResponse.getTid());
		log.info("결제요청URL(QR코드): " + readyResponse.getNext_redirect_pc_url());
		
		session.setAttribute("tid", readyResponse.getTid());
		session.setAttribute("ord_code", o_vo.getOrd_code());
		
		
		}
		
		return readyResponse;
	}
	
	// 걸제승인요청.  카카오페이 API서버에서 다음주소가 호출이 됨.
	@GetMapping("/orderApproval")
	public String orderApproval(String pg_token, HttpSession session) {
		
		String tid = (String) session.getAttribute("tid");
		session.removeAttribute("tid");
		
		Long ord_code = (Long) session.getAttribute("ord_code");
		session.removeAttribute("ord_code");
		
		String mbsp_id = ((MemberVO) session.getAttribute("loginStatus")).getMbsp_id();
		
		
		
		// 결제승인요청작업. 응답데이터는 여기서는 사용안했음.
		ApproveResponse approveResponse = kakaoPayService.payApprove(cid, tid, ord_code, mbsp_id, pg_token);
		
		
		return "redirect:/order/orderComplete"; // 결제 성공결과페이지 주소.
	}
	
	// 카카오페이 결제및주문 완료
	@GetMapping("/orderComplete")
	public String orderComplete() {
		
		return "/order/order_complete";
	}
	
	// 결제준비요청 취소
	@GetMapping("/orderCancel")
	public void orderCancel() {
		
	}
	
	// 결제준비요청 실패
	@GetMapping("/orderFail")
	public void orderFail() {
		
	}
	
	
	
	
	
	
	
	
	
	
}
