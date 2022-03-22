package com.revature.repositories;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.revature.driver.DartCartApplication;
import com.revature.models.Seller;
import com.revature.models.User;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = DartCartApplication.class)
public class SellerRepoTest {
  @Autowired
  SellerRepo sellerRepo;

  @MockBean
  private UserRepo mockUserRepo;

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

  private final Seller mockSeller = new Seller(
    1,
    "New Seller",
    "/newseller",
    "THE BEST NEW SELLER!!!",
    mockUser
  );

  @ParameterizedTest
  @ValueSource(ints = { 1, 2 })
  public void givenSellerId_whenFindById_thenFindSeller(int id) {
    Optional<Seller> output = sellerRepo.findById(id);
    assertTrue(output.isPresent());

    Seller seller = output.get();
    assertEquals(id, seller.getId());
  }

  @Test
  public void whenFindAll_thenReturnAllSellers() {
    List<Seller> sellers = (List<Seller>) sellerRepo.findAll();
    assertNotEquals(0, sellers.size());
  }

  @Test
  public void givenSeller_whenSave_thenAddSeller() {
    SellerRepo mockRepo = mock(SellerRepo.class);
    Mockito.when(mockUserRepo.save(mockUser)).thenReturn(mockUser);

    mockRepo.save(mockSeller);
    verify(mockRepo).save(mockSeller);
  }

  @Test
  public void givenSellerId_whenSave_thenUpdateSeller() {
    Seller testSeller = new Seller(
      1,
      "New Seller",
      "/newseller",
      "THE SUPER DUPER BEST NEW SELLER!!!",
      mockUser
    );

    sellerRepo.save(testSeller);
    Optional<Seller> output = sellerRepo.findById(mockUser.getId());

    Seller updatedSeller = output.get();
    assertEquals(
      "THE SUPER DUPER BEST NEW SELLER!!!",
      updatedSeller.getDescription()
    );
  }

  @ParameterizedTest
  @ValueSource(ints = { 1, 2 })
  public void givenSellerId_whenDelete_thenDeleteSeller(int id) {
    SellerRepo mockRepo = mock(SellerRepo.class);
    mockRepo.deleteById(id);
    verify(mockRepo).deleteById((id));
  }
}
