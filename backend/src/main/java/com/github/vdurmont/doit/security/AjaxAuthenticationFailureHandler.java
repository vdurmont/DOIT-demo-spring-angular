package com.github.vdurmont.doit.security;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AjaxAuthenticationFailureHandler extends
		SimpleUrlAuthenticationFailureHandler {
	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
										HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		response.setContentType("application/json");
		int status = getStatus(exception);
		response.setStatus(status);
		response.getWriter().print("{\"loggedIn\":false}");
	}

	private static int getStatus(AuthenticationException e) {
		if (e instanceof InsufficientAuthenticationException
				| e instanceof BadCredentialsException
				| e instanceof AuthenticationCredentialsNotFoundException) {
			return 401;
		} else if (e instanceof UsernameNotFoundException) {
			return 404;
		}
		return 500;
	}
}