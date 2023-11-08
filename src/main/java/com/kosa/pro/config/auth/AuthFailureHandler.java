package com.kosa.pro.config.auth;

import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		
	    String msg = "아이디 또는 비밀번호가 틀립니다.";
	
	    // exception 관련 메세지 처리
	    if (exception instanceof LockedException) {
        	msg = "계정이 잠겼습니다";
	    } else if (exception instanceof DisabledException) {
        	msg = "비 활성화된 계정 입니다.";
        } else if(exception instanceof CredentialsExpiredException) {
        	msg = "CredentialsExpiredException account";
        } else if(exception instanceof BadCredentialsException ) {
        	msg = "BadCredentialsException account";
        }
	
	    setDefaultFailureUrl("/auth/loginForm?error=true&exception=" + msg);
	
	    super.onAuthenticationFailure(request, response, exception);
	}
}