package com.github.vdurmont.doit.controller;

import com.github.vdurmont.doit.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ExceptionResolver implements HandlerExceptionResolver {
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		ModelAndView mav = new ModelAndView();

		mav.setView(new MappingJackson2JsonView());
		mav.addObject("error", ex.getClass().getSimpleName());
		mav.addObject("message", ex.getMessage());

		HttpStatus status = getStatus(ex);
		response.setHeader("Content-Type", "application/json");
		response.setStatus(status.value());

		ex.printStackTrace();

		return mav;
	}

	private static HttpStatus getStatus(Exception e) {
		if (e instanceof IllegalInputException) {
			return HttpStatus.BAD_REQUEST;
		} else if (e instanceof NotAvailableException) {
			return HttpStatus.BAD_REQUEST;
		} else if (e instanceof NoConnectedUserException) {
			return HttpStatus.UNAUTHORIZED;
		} else if (e instanceof AuthorizationException) {
			return HttpStatus.FORBIDDEN;
		} else if (e instanceof ResourceNotFoundException) {
			return HttpStatus.NOT_FOUND;
		} else {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
	}
}
