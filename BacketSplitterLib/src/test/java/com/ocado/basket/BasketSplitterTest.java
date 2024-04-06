package com.ocado.basket;

import com.ocado.exceptions.TooManyProductsInBasketException;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class BasketSplitterTest {
    static BasketSplitter basketSplitter;
    static final String absolutePath = "src\\main\\resources\\configuration\\config.json";
    static final String exampleBasket = "src\\test\\resources\\basket-1.json";

    static List<String> readBasket(String filename) {
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = null;
        try {
            Object obj = parser.parse(new FileReader(filename));
            jsonArray = (JSONArray) obj;

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return (List<String>) jsonArray;
    }

    @BeforeAll
    static void setUp(){
        basketSplitter = new BasketSplitter(absolutePath);
    }

    @Test
    void singleItemInBracketTest() {
        List<String> singleItemList = new ArrayList<>();
        singleItemList.add("Brandy - Bar");
        Assertions.assertEquals(1, basketSplitter.split(singleItemList).size());
    }

    @Test
    void emptyBracketTest() {
        Assertions.assertEquals(0, basketSplitter.split(new ArrayList<>()).size());
    }

    @Test
    //This test only checks if program doesn't crash when we pass multiple identical product in list, however algorithm counts it as one so the result may not be exactly optimal considering every repeated product is counted (this case wasn't specified in task descripiton)
    void repeatedItemsInListTest() {
        List<String> repeatedItemList = new ArrayList<>();
        repeatedItemList.add("Cookies - Englishbay Wht");
        repeatedItemList.add("Cookies - Englishbay Wht");
        repeatedItemList.add("Cookies - Englishbay Wht");

        assertEquals(1, basketSplitter.split(repeatedItemList).size());
    }

    @Test
    void multipleItemsWithCommonOptionInListTest() {
        List<String> itemList = new ArrayList<>();
        itemList.add("Cookies Oatmeal Raisin");
        itemList.add("Cheese Cloth");
        itemList.add("English Muffin");


        Map<String, List<String>> result = basketSplitter.split(itemList);
        assertEquals(1, result.size());
        assertTrue(result.containsKey("Parcel locker"));
        assertEquals(3, result.get("Parcel locker").size());
    }

    @Test
    void multipleDeliveryMethodsRequiredTest() {
        List<String> itemList = new ArrayList<>();
        itemList.add("Cookies Oatmeal Raisin");
        itemList.add("Cheese Cloth");
        itemList.add("Wine - Port Late Bottled Vintage");

        Map<String, List<String>> result = basketSplitter.split(itemList);
        assertEquals(2, result.size());
    }

    @Test
    void throwsExceptionWhenBasketTooLargeTest() {
        List<String> largeBasket = new ArrayList<>();
        for (int i = 0; i < 101; i++) {
            largeBasket.add("Brandy - Bar");
        }
        Assertions.assertThrows(TooManyProductsInBasketException.class, () -> basketSplitter.split(largeBasket));
    }

    @Test
    void allProductsInBasketCoveredTest(){
        List<String> itemList = readBasket(exampleBasket);
        int expectedSize = itemList.size();

        Map<String, List<String>> result = basketSplitter.split(itemList);
        int actualSize = 0;
        for(List<String> products : result.values()){
            actualSize += products.size();
        }

        Assertions.assertEquals(expectedSize, actualSize);

    }
}