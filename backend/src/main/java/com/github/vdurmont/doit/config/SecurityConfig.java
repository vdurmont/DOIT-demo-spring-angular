package com.github.vdurmont.doit.config;

import com.github.vdurmont.doit.repository.UserRepository;
import com.github.vdurmont.doit.security.AjaxAuthenticationFailureHandler;
import com.github.vdurmont.doit.security.AjaxAuthenticationSuccessHandler;
import com.github.vdurmont.doit.security.AjaxLogoutSuccessHandler;
import com.github.vdurmont.doit.service.LoginService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import javax.inject.Inject;

@Configuration
@EnableWebMvcSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Inject
	private UserRepository userRepository;

	@Bean
	public StandardPasswordEncoder standardPasswordEncoder() {
		return new StandardPasswordEncoder("myawesomesecret");
	}

	@Bean
	public LoginService loginService() {
		return new LoginService(userRepository);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(loginService()).passwordEncoder(standardPasswordEncoder());
	}


	@Override
	public AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	@Bean
	public AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler() {
		return new AjaxAuthenticationSuccessHandler();
	}

	@Bean
	public AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler() {
		return new AjaxAuthenticationFailureHandler();
	}

	@Bean
	public AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler() {
		return new AjaxLogoutSuccessHandler();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.formLogin()
				.loginProcessingUrl("/login")
				.successHandler(ajaxAuthenticationSuccessHandler())
				.failureHandler(ajaxAuthenticationFailureHandler())
				.usernameParameter("email")
				.passwordParameter("password")
				.permitAll();
		http.logout()
				.logoutUrl("/logout")
				.logoutSuccessHandler(ajaxLogoutSuccessHandler())
				.deleteCookies("JSESSIONID")
				.permitAll();
		http.authorizeRequests()
				.antMatchers("/projects").authenticated();
		// TODO enable csrf with angular later
		http.csrf().disable();
	}
}
