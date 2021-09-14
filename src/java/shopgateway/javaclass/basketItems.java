/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shopgateway.javaclass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;

/**
 *
 * @author gagan
 */
public class basketItems {
    
    public basketItems() {
    }

    public JSONObject retrieveBasket(String person_id) {

        // fetch basket items with reference to person_id
        
        JSONObject result = new JSONObject();
        
        
// if basket session exists in database or redis 
// add that to result or it will be just empty
        
        return result;
    }
    
    public JSONObject updateBasket(String person_id) {
        
        
        return null;
    }
}
/*

List<Object> lineItems = new ArrayList<>();

        Map<String, Object> lineItem1 = new HashMap<>();
        lineItem1.put("price", "price_1JX3mgGpCsnZmArEgzvUViL6");
        lineItem1.put("quantity", 2);

        Map<String, Object> lineItem2 = new HashMap<>();
        lineItem2.put("price", "price_1JX3jfGpCsnZmArECw0S45GP");
        lineItem2.put("quantity", 1);

        lineItems.add(lineItem1);
        lineItems.add(lineItem2);

        return lineItems;

[{quantity=2, price=price_1JX3mgGpCsnZmArEgzvUViL6}, {quantity=1, price=price_1JX3jfGpCsnZmArECw0S45GP}]

 */
