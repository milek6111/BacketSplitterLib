package com.ocado.basket;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.List;

public class Maintest {
    public static void main(String[] args) {
        BasketSplitter b = new BasketSplitter("src\\main\\resources\\configuration\\config.json");

        for(int i = 1; i <= 5; i++){
            System.out.println("Basket no." +i + " split result:");
            System.out.println(b.split(readBasket("src\\main\\resources\\exampleBaskets\\basket-" + i+ ".json")));
            System.out.println("\n\n");
        }

    }

    public static List<String> readBasket(String filename) {
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
}
