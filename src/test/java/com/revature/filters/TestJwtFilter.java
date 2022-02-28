package com.revature.filters;

import com.revature.models.User;
import com.revature.utilities.JwtTokenUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class TestJwtFilter {
	
	// inject jwt token util class and user repo before
	// jwtTokenUtil,  getUsername
	// CHECKS: token valid, username, is authenticated
	// assertTrue( SecurityContextHolder.getContext().getAuthentication().isAuthenticated() );
	
	private static final String CLASSNAME = "com.revature.dartcart.filters.JwtTokenFilter";
	private static final String DOFILTER = "doFilterInternal";
	private static Object tokenFilter;
	private static Method doFilterInternal;
	
	@MockBean private static JwtTokenUtil jwtTokenUtil;
	@MockBean private static Class UserRepo;
	@Autowired private MockMvc mvc;
	
	//@Autowired MockHttpServletRequest request;
	
	
	@BeforeAll
	static void testSetup(@Autowired ApplicationContext app) {
		
		try {
			tokenFilter = app.getBean(Class.forName(CLASSNAME));
			doFilterInternal = tokenFilter.getClass().getMethod(DOFILTER);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Class: " + CLASSNAME+ " needs to be implemented", e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("Method: " + DOFILTER+ " needs to be implemented", e);
		}
	}
	
	@Test
	void tokenDNE() throws InvocationTargetException, IllegalAccessException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		MockFilterChain mockChain = new MockFilterChain();
		
		request.addHeader(HttpHeaders.AUTHORIZATION, "invalid");
		doFilterInternal.invoke(tokenFilter, request, response, mockChain);
		
		assertFalse( SecurityContextHolder.getContext().getAuthentication().isAuthenticated() );
	}
	
	@Test
	void tokenInvalid() throws InvocationTargetException, IllegalAccessException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		MockFilterChain mockChain = new MockFilterChain();
		
		request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer exampleToken");
		Mockito.when(jwtTokenUtil.validate("exampleToken")).thenReturn(false);
		
		doFilterInternal.invoke(tokenFilter, request, response, mockChain);
		assertFalse( SecurityContextHolder.getContext().getAuthentication().isAuthenticated() );
	}
	
	@Test
	void usernameInvalid() throws InvocationTargetException, IllegalAccessException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		MockFilterChain mockChain = new MockFilterChain();
		
		request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer exampleToken");
		Mockito.when(jwtTokenUtil.validate("exampleToken")).thenReturn(true);
		
		Mockito.when( jwtTokenUtil.getUsername("exampleToken")).thenReturn("invalidName");
		Mockito.when(UserRepo.findByUsername("invalidName") ).thenReturn(null);
		
		doFilterInternal.invoke(tokenFilter, request, response, mockChain);
		assertFalse(SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
	}
	
	@Test
	public void testIsAuthenticated() throws InvocationTargetException, IllegalAccessException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		MockFilterChain mockChain = new MockFilterChain();
		
		request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer exampleToken");
		Mockito.when(jwtTokenUtil.validate("exampleToken")).thenReturn(true);
		
		Mockito.when( jwtTokenUtil.getUsername("exampleToken")).thenReturn("validName");
		Mockito.when(UserRepo.findByUsername("validName") ).thenReturn(new User());
		
		doFilterInternal.invoke(tokenFilter, request, response, mockChain);
		assertTrue(SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
	}
	
	@AfterEach
	public void clearSecurityContext() {
		SecurityContextHolder.clearContext();
	}
	
}
