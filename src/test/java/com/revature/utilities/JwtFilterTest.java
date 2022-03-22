package com.revature.utilities;

import static org.junit.jupiter.api.Assertions.*;

import com.revature.driver.DartCartApplication;
import com.revature.models.User;
import com.revature.repositories.UserRepo;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootTest(classes = DartCartApplication.class)
public class JwtFilterTest {
  private static final String CLASSNAME =
    "com.revature.utilities.JwtTokenFilter";
  private static final String DOFILTER = "doFilterInternal";
  private static Object tokenFilter;
  private static Method doFilterInternal;

  @MockBean
  private static JwtTokenUtil jwtTokenUtil;

  @MockBean
  private static UserRepo userRepo;

  @BeforeAll
  static void testSetup(@Autowired ApplicationContext app) {
    try {
      tokenFilter = app.getBean(Class.forName(CLASSNAME));
      doFilterInternal =
        tokenFilter
          .getClass()
          .getDeclaredMethod(
            DOFILTER,
            HttpServletRequest.class,
            HttpServletResponse.class,
            FilterChain.class
          );
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(
        "Class: " + CLASSNAME + " needs to be implemented",
        e
      );
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(
        "Method: " + DOFILTER + " needs to be implemented",
        e
      );
    }
  }

  @Test
  //@ParameterizedTest
  void tokenDNE() throws InvocationTargetException, IllegalAccessException {
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    MockFilterChain mockChain = new MockFilterChain();

    request.addHeader(HttpHeaders.AUTHORIZATION, "invalid");
    doFilterInternal.invoke(tokenFilter, request, response, mockChain);

    Authentication authentication = SecurityContextHolder
      .getContext()
      .getAuthentication();
    assertNull(authentication);
  }

  @Test
  void tokenInvalid() throws InvocationTargetException, IllegalAccessException {
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    MockFilterChain mockChain = new MockFilterChain();

    request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer exampleToken");
    Mockito.when(jwtTokenUtil.validate("exampleToken")).thenReturn(false);

    doFilterInternal.invoke(tokenFilter, request, response, mockChain);
    Authentication authentication = SecurityContextHolder
      .getContext()
      .getAuthentication();
    assertNull(authentication);
  }

  @Test
  void usernameInvalid()
    throws InvocationTargetException, IllegalAccessException {
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    MockFilterChain mockChain = new MockFilterChain();

    request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer exampleToken");
    Mockito.when(jwtTokenUtil.validate("exampleToken")).thenReturn(true);

    Mockito
      .when(jwtTokenUtil.getUsername("exampleToken"))
      .thenReturn("invalidName");
    Mockito.when(userRepo.findByUsername("invalidName")).thenReturn(null);

    doFilterInternal.invoke(tokenFilter, request, response, mockChain);
    Authentication authentication = SecurityContextHolder
      .getContext()
      .getAuthentication();
    assertNull(authentication);
  }

  @Test
  public void testIsAuthenticated()
    throws InvocationTargetException, IllegalAccessException {
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    MockFilterChain mockChain = new MockFilterChain();

    request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer exampleToken");
    Mockito.when(jwtTokenUtil.validate("exampleToken")).thenReturn(true);

    Mockito
      .when(jwtTokenUtil.getUsername("exampleToken"))
      .thenReturn("validName");
    Mockito
      .when(userRepo.findByUsername("validName"))
      .thenReturn(
        new User(
          1,
          "admin",
          "pass",
          "Bob",
          "Latency",
          "Bob@Babbo.com",
          "555 555 5555",
          "Address",
          0L,
          null,null
         // new ArrayList<>()
        )
      );

    doFilterInternal.invoke(tokenFilter, request, response, mockChain);
    Authentication authentication = SecurityContextHolder
      .getContext()
      .getAuthentication();
    assertNotNull(authentication);
    assertTrue(authentication.isAuthenticated());
  }

  @AfterEach
  public void clearSecurityContext() {
    SecurityContextHolder.clearContext();
  }
}
