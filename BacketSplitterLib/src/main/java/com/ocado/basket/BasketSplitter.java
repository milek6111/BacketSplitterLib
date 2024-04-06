package com.ocado.basket;

import com.ocado.exceptions.TooManyConfigProductsException;
import com.ocado.exceptions.TooManyProductsInBasketException;
import com.ocado.exceptions.TooManyShippingOptionsException;
import org.json.simple.ItemList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.*;

/**
 * BasketSplitter class, its main purpose is to split given Basket for possibly minimal amount of sets (each with different shipping method)
 */
public class BasketSplitter {

    /**
     * configuration mapper, for every key (product) holds list of possible shipping method for this product
     */
    private final Map<String, List<String>> configuration;

    /**
     * Ctor with absolute path to configuration file as argument. Its main role is to initialize configuration mapper based on config.json file
     * @param absolutePathToConfigFile
     */
    public BasketSplitter(String absolutePathToConfigFile) {
        //Reading config.json file and creating instance of mapper which will be used to get delivery methods for each product
        configuration = new HashMap<>();
        JSONParser parser = new JSONParser();
        try{
            Object obj = parser.parse(new FileReader(absolutePathToConfigFile));
            JSONObject jsonObject = (JSONObject) obj;

            if(jsonObject.keySet().size() > 1000){
                throw new TooManyConfigProductsException("Too many products in config file.");
            }

            for (Object key : jsonObject.keySet()) {
                JSONArray subObject = (JSONArray) jsonObject.get(key);
                if(subObject.size() > 10){
                    throw new TooManyShippingOptionsException("Too many shipping options per product.");
                }
                configuration.put((String) key, (List) subObject);
            }

        } catch (TooManyShippingOptionsException e){
            throw e;
        }
        catch (TooManyConfigProductsException e) {
            throw e;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * split method which applies greedy version of Set Cover Problem
     * @param items
     * @return Map which keys are delivery mehthods and values are Lists of items for each option
     */
    public Map<String, List<String>> split(List<String> items) {

        if(items.size() > 100){
            throw new TooManyProductsInBasketException("There are too many products in Basket.");
        }

        return greedySCP(items);
    }

    /**
     * Greedy SCP implementation
     * Eplanation: Greedy verion of SCP is chosen because it gives relatively good results considering its much less time complexity than other algorithms (This is NP-hard problem which can't be precisely solved in polynomial time).
     * In this case time matters because we don't want our client to wait long time to complete his order, and also we are sure that one of the delivery methods we choose will have the largest possible number of products so even if count of delivery methods may not be the smallest possible we are sure that one of them will cover most possible items in cart.
     * @param items
     * @return Map which keys are delivery mehthods and values are Lists of items for each option
     */
    private Map<String, List<String>> greedySCP(List<String> items){
        //creating map which contains delivery options as keys and products from Baskets that can be shipped with this option as list
        Map<String, List<String>> productsPerDelivery= new HashMap<>();

        for(String item : items){
            for(String delivery : configuration.get(item)){
                if(!productsPerDelivery.containsKey(delivery)){
                    productsPerDelivery.put(delivery, new ArrayList<>());
                }
                productsPerDelivery.get(delivery).add(item);
            }
        }


        //Greedy algorithm implementation which relies on selecting method with most items included in each iteration
        Map<String, List<String>> result = new HashMap<>();
        Set<String> productsToCover = new HashSet<>(items);

        while(!productsToCover.isEmpty()){
            int max = -1;
            String optimalOption = null;
            for(String delivery : productsPerDelivery.keySet()){
                int remainingProductsCovered = commonPart(productsPerDelivery.get(delivery),productsToCover).size();
                if(remainingProductsCovered > max){
                    max = remainingProductsCovered;
                    optimalOption = delivery;
                }
            }

            result.put(optimalOption, commonPart(productsPerDelivery.get(optimalOption),productsToCover));
            productsToCover.removeAll(productsPerDelivery.get(optimalOption));
            productsPerDelivery.remove(optimalOption);
        }

        //sorting result map by number of products per delivery descending
        List<Map.Entry<String, List<String>>> entryList = new ArrayList<>(result.entrySet());

        Collections.sort(entryList, (entry1, entry2) -> Integer.compare(entry2.getValue().size(), entry1.getValue().size()));

        Map<String, List<String>> sortedResultMap = new LinkedHashMap<>();
        for (Map.Entry<String, List<String>> entry : entryList) {
            sortedResultMap.put(entry.getKey(), entry.getValue());
        }

        return sortedResultMap;
    }

    /**
     * Helper method for calculating common part of items remaining items to cover and items covered by delivery
     * @param items
     * @param productsToCover
     * @return List of products which are common for items covered by delivery and products left to cover
     */
    private List<String> commonPart(List<String> items ,Set<String> productsToCover){
        List<String> result = new ArrayList<>();
        for(String item : items){
            if(productsToCover.contains(item)){
                result.add(item);
            }
        }
        return result;
    }

}
