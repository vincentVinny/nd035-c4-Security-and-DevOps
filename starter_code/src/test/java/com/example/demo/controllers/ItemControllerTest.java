package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;

    private final ItemRepository itemRepository = mock(ItemRepository.class);

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
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void getItemsHappyPath() {
        Item item = createItem();
        when(itemRepository.findAll()).thenReturn(Collections.singletonList(item));

        final ResponseEntity<List<Item>> response = itemController.getItems();
        List<Item> items = response.getBody();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(items);
        assertEquals(1, items.size());
    }

    @Test
    public void getItemByIdHappyPath() {
        Item item = createItem();
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        final ResponseEntity<Item> response = itemController.getItemById(1L);
        Item actualItem = response.getBody();

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void getItemByIdNotFound() {
        Item item = createItem();
        when(itemRepository.findById(2L)).thenReturn(Optional.of(item));

        final ResponseEntity<Item> response = itemController.getItemById(1L);
        Item actualItem = response.getBody();

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void getItemsByNameHappyPath() {
        Item item = createItem();
        when(itemRepository.findAll()).thenReturn(Collections.singletonList(item));
        when(itemRepository.findByName(item.getName())).thenReturn(Collections.singletonList(item));

        final ResponseEntity<List<Item>> response = itemController.getItemsByName("An Item");
        List<Item> items = response.getBody();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(items);
        assertEquals(1, items.size());
    }

    @Test
    public void getItemsByNameNotFound() {
        Item item = createItem();
        when(itemRepository.findAll()).thenReturn(Collections.singletonList(item));
        when(itemRepository.findByName(item.getName())).thenReturn(Collections.singletonList(item));

        final ResponseEntity<List<Item>> response = itemController.getItemsByName("A Different Item");

        assertEquals(404, response.getStatusCodeValue());
    }
}
