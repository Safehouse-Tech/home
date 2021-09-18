/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shopgateway.rough;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;
import redis.clients.jedis.Jedis;

/**
 *
 * @author gagan
 */
public class RedisSessions {

    public RedisSessions() {
    }

    public List<Object> retrieveSession(String person_id) {
//        JSONObject result = new JSONObject();
        List<Object> lineItems = new ArrayList<>();

//        Jedis jedis = new Jedis("34.254.191.66");
//        jedis.set("uid102", "{ loggedIn: true, cartItems: [{ pid: 1, qty 2 }] }");
//        String value = jedis.get("uid102");
//        
//        System.out.println("value:"+ value);
        Map<String, Object> lineItem1 = new HashMap<>();
        lineItem1.put("price", "price_1JX3mgGpCsnZmArEgzvUViL6");
        lineItem1.put("quantity", 2);

        Map<String, Object> lineItem2 = new HashMap<>();
        lineItem2.put("price", "price_1JX3jfGpCsnZmArECw0S45GP");
        lineItem2.put("quantity", 1);

        lineItems.add(lineItem1);
        lineItems.add(lineItem2);

        return lineItems;
    }
}
/*

[{quantity=2, price=price_1JX3mgGpCsnZmArEgzvUViL6}, {quantity=1, price=price_1JX3jfGpCsnZmArECw0S45GP}]

 */
