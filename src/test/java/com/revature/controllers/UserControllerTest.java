package com.revature.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.driver.DartCartApplication;
import com.revature.models.User;
import com.revature.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.MOCK,
  classes = DartCartApplication.class
)
class UserControllerTest {
  private MockMvc mvc;
  ObjectMapper mapper = new ObjectMapper();

  @Autowired
  private WebApplicationContext webApplicationContext;

  @MockBean
  private UserService mockUserService;

  private final User mockUser = new User(
    1,
    "test1",
    "password",
    "Test",
    "User",
    "test1@dartcart.net",
    "123-456-7890",
    "1 Test Street, Test Town, Testonia 12345",
    123563672L,
    null, null
  );

  @BeforeEach
  void setup() {
    mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  @Test
  void newUser() throws Exception {
    Mockito.when(mockUserService.addUser(mockUser)).thenReturn(mockUser);

    mvc
      .perform(
        MockMvcRequestBuilders
          .post("/register")
          .contentType(MediaType.APPLICATION_JSON)
          .content(mapper.writeValueAsString(mockUser))
      )
      .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  void testNewUserFail() throws Exception {
    Mockito.when(mockUserService.addUser(new User())).thenReturn(new User());
    mvc
      .perform(
        MockMvcRequestBuilders
          .post("/register")
          .contentType(MediaType.APPLICATION_JSON)
          .content(mapper.writeValueAsString(new User()))
      )
      .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }
}
