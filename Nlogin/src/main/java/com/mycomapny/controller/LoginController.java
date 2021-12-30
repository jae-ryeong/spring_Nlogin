package com.mycomapny.controller;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.mycomapny.Nlogin.NaverLoginVO;

@Controller
public class LoginController {

	// NaverLoginVO
	private NaverLoginVO naverLoginVO;
	private String apiResult = null;
	
	@Autowired
	private void setNaverLoginVO(NaverLoginVO naverLoginVO) {
		this.naverLoginVO = naverLoginVO;
	}
	
	// 로그인 첫 화면 요청 메소드
	@RequestMapping(value="/login", method= {RequestMethod.GET, RequestMethod.POST})
	public String login(Model model,HttpSession session) {
		
		// 네이버아이디로 인증 URL을 생성하기 위하여 naverLoginVO클래스의 getAuthorizationUrl메소드 호출
		String naverAuthUrl = naverLoginVO.getAuthorizationUrl(session);
		
		//https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=sE***************&
        //redirect_uri=http%3A%2F%2F211.63.89.90%3A8090%2Flogin_project%2Fcallback&state=e68c269c-5ba9-4c31-85da-54c16c658125
		System.out.println("네이버: "+naverAuthUrl);
		
		//네이버
		model.addAttribute("url",naverAuthUrl);
		
		// 생성한 인증 URL을 View로 전달
		return "login";
	}
	
	//네이버 로그인 성공시 callback호출 메소드
	@RequestMapping(value = "/callback",method= {RequestMethod.GET, RequestMethod.POST})
	public String callback(Model model, @RequestParam String code, @RequestParam String state, HttpSession session)
			throws IOException{
		System.out.println("여기는 callback");
		OAuth2AccessToken oauthToken;
		oauthToken = naverLoginVO.getAccessToken(session, code, state);
		
		//로그인 사용자 정보를 읽어온다.
		apiResult = naverLoginVO.getUserProfile(oauthToken);
		model.addAttribute("result", apiResult);
		
		return "naverSuccess";				
	}
}
