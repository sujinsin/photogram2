package com.cos.photogramstart.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultSecurityFilterChain;

import com.cos.photogramstart.config.oauth.OAuth2DetailsService;
import com.cos.photogramstart.handler.ex.CustomValidationException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig{
	
	
	private final OAuth2DetailsService oAuth2DetailsService;
	
// 새로운  SecurityFilterChain 생성 
//	@Bean
//	public DefaultSecurityFilterChain configure2(HttpSecurity http) throws Exception {
//		return http.authorizeRequests().antMatchers("/admin").authenticated()
//				.and().formLogin().loginPage("/auth/signin")			
//				.loginProcessingUrl("/auth/signin") // post 방식 , 스프링시큐리티가 로그인 프로세스를 진행한다. 
//				.defaultSuccessUrl("/").and().build();
//	}
	
	// https://velog.io/@yaho1024/Spring-Security-FilterChainProxy 프록시 관련 자세하게 

	//ioc
	@Bean
	public BCryptPasswordEncoder encode() {
		return new BCryptPasswordEncoder();
	}
	

	@Bean
	public DefaultSecurityFilterChain configure(HttpSecurity http) throws Exception  {
		
		// 일단 csrf사용안하겠다..
		http.csrf().disable();
		
		
		// super 가 없으면 - 기존 시큐리티가 가지고 있는 기능이 비활성화 됨. login 디폴트사라짐. 

		http.authorizeRequests()
			.antMatchers("/", "/user/**","/image/**/","/subscribe/**","/comment/**","/api/**").authenticated()
			//.antMatchers("/admin/**").hasRole("ADMIN")
			.anyRequest().permitAll()
			.and() 
			.formLogin() 
			.loginPage("/auth/signin") 
			.loginProcessingUrl("/auth/signin") 
			.defaultSuccessUrl("/") 
			.and()
			.oauth2Login()
			.userInfoEndpoint()
			.userService(oAuth2DetailsService);
		return http.build();
	}
}
