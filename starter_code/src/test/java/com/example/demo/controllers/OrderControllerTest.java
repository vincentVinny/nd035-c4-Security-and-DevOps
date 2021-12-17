package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;
    private final UserRepository userRepository = mock(UserRepository.class);
    private final OrderRepository orderRepository = mock(OrderRepository.class);

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
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void submitOrderHappyPath() {
        Item item = createItem();
        User user = createUser();
        Cart cart = new Cart();
        cart.setUser(user);
        cart.addItem(item);
        user.setCart(cart);
        UserOrder userOrder = UserOrder.createFromCart(cart);

        when(userRepository.findByUsername("test")).thenReturn(user);
        when(orderRepository.findByUser(any())).thenReturn(Collections.singletonList(userOrder));

        final ResponseEntity<UserOrder> response = orderController.submit("test");

        UserOrder actualUserOrder = response.getBody();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(actualUserOrder);
        assertEquals(1, actualUserOrder.getItems().size());
    }

    @Test
    public void submitOrderUserNotFound() {
        Item item = createItem();
        User user = createUser();
        Cart cart = new Cart();
        cart.setUser(user);
        cart.addItem(item);
        user.setCart(cart);
        UserOrder userOrder = UserOrder.createFromCart(cart);

        when(orderRepository.findByUser(any())).thenReturn(Collections.singletonList(userOrder));

        final ResponseEntity<UserOrder> response = orderController.submit("test");

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void getOrdersForUserHappyPath() {
        Item item = createItem();
        User user = createUser();
        Cart cart = new Cart();
        cart.setUser(user);
        cart.addItem(item);
        user.setCart(cart);
        UserOrder userOrder = UserOrder.createFromCart(cart);

        when(userRepository.findByUsername("test")).thenReturn(user);
        when(orderRepository.findByUser(any())).thenReturn(Collections.singletonList(userOrder));

        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");

        List<UserOrder> actualUserOrder = response.getBody();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(actualUserOrder);
        assertEquals(1, actualUserOrder.size());
    }

    @Test
    public void getOrdersForUserNotFound() {
        Item item = createItem();
        User user = createUser();
        Cart cart = new Cart();
        cart.setUser(user);
        cart.addItem(item);
        user.setCart(cart);
        UserOrder userOrder = UserOrder.createFromCart(cart);

        when(orderRepository.findByUser(any())).thenReturn(Collections.singletonList(userOrder));

        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");

        assertEquals(404, response.getStatusCodeValue());
    }
}
