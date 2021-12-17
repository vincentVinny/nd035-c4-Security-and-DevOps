package com.example.demo.controllers;


import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;

    private final UserRepository userRepository = mock(UserRepository.class);

    private final CartRepository cartRepository = mock(CartRepository.class);

    private final ItemRepository itemRepository = mock(ItemRepository.class);

    private static User createUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("testPassword");
        return user;
    }

    private static Item createItem() {
        Item item = new Item();
        item.setId(1L);
        item.setPrice(BigDecimal.valueOf(200.00));
        item.setName("An Item");
        item.setDescription("An Item that does something");
        return item;
    }

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void addToCartHappyPath() {
        User user = createUser();
        Item item = createItem();
        Cart cart = new Cart();

        cart.setUser(user);
        cart.addItem(item);
        user.setCart(cart);

        when(userRepository.findByUsername("test")).thenReturn(user);
        when(itemRepository.findById(any())).thenReturn(Optional.of(item));

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(2);
        final ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void addToCartUserNotFound() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(2);
        final ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void addToCartItemIdNotFound() {
        User user = createUser();
        when(userRepository.findByUsername("test")).thenReturn(user);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(2L);
        modifyCartRequest.setQuantity(2);

        final ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void removeToCartHappyPath() {
        User user = createUser();
        Item item = createItem();
        Cart cart = new Cart();

        cart.setUser(user);
        cart.addItem(item);
        user.setCart(cart);

        when(userRepository.findByUsername("test")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(2);
        final ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void removeToCartUserNotFound() {
        User user = createUser();
        Item item = createItem();
        Cart cart = new Cart();

        cart.setUser(user);
        cart.addItem(item);
        user.setCart(cart);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(2);
        final ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void removeToCartRequestIdNotFound() {
        User user = createUser();
        Item item = createItem();
        Cart cart = new Cart();

        cart.setUser(user);
        cart.addItem(item);
        user.setCart(cart);

        when(userRepository.findByUsername("test")).thenReturn(user);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(2);
        final ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);

        assertEquals(404, response.getStatusCodeValue());
    }
}
