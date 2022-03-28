package com.revature.services;

import com.revature.driver.DartCartApplication;
import com.revature.models.User;
import com.revature.repositories.UserRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.MOCK,
  classes = DartCartApplication.class
)
class UserServiceTest {
  private final User mockUser = new User(
    1,
    "test1",
    "password",
    "Test",
    "User",
    "test1@DartCart.net",
    "123-456-7890",
    "1 Test Street, Test Town, Testonia 12345",
    123563672L,
    null, null, null, null
  );

  @Autowired
  private WebApplicationContext webApplicationContext;

  @MockBean
  private UserRepo mockUserRepo;

  @Autowired
  private UserService mockUserService;

  @Autowired
  BCryptPasswordEncoder bCryptEncoder;

  @BeforeEach
  void setup() {
    MockMvc mvc = MockMvcBuilders
      .webAppContextSetup(webApplicationContext)
      .build();
  }

  @Test
  void givenUser_whenAddUser_thenAddNewUser() {
    User encrypted = mockUser;
    encrypted.setPassword(bCryptEncoder.encode(encrypted.getPassword()));
    Mockito.when(mockUserRepo.save(mockUser)).thenReturn(encrypted);
    Assertions.assertEquals(encrypted, mockUserService.addUser(mockUser));
  }
}
