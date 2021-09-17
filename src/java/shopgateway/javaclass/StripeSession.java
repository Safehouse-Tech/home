/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shopgateway.javaclass;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import shopgateway.config.CONFIG;

/**
 *
 * @author gagan
 */
public class StripeSession {

    public StripeSession() {
    }

    JSONParser jsonParser = new JSONParser();
    
    public JSONObject createSession(JSONObject basketItems, String shippingCode) throws StripeException, ParseException {

        Stripe.apiKey = CONFIG.STRIPEKEY ;

        List<Object> paymentMethodTypes = new ArrayList<>();
        paymentMethodTypes.add("card");
        List<Object> lineItems = new ArrayList<>();
        
        Map<String, Object> adjustable_quantity = new HashMap<>();
        adjustable_quantity.put("minimum", 1);
        adjustable_quantity.put("maximum", 10);
        adjustable_quantity.put("enabled", true);
        
        basketItems.keySet().forEach(keyStr ->
        {
            JSONObject keyvalue = (JSONObject) basketItems.get(keyStr);
            
            Map<String, Object> lineItem = new HashMap<>();
            lineItem.put("price", keyvalue.get("price_id"));
            lineItem.put("quantity", keyvalue.get("quantity"));
            lineItem.put("adjustable_quantity", adjustable_quantity);
            
            lineItems.add(lineItem);
        });
        
        List<Object> countryCode = new ArrayList<>();
        countryCode.add("GB");
        countryCode.add("US");
        Map<String, Object> shipping_address = new HashMap<>();
        shipping_address.put("allowed_countries", countryCode);
        
          
        Map<String, Object> params = new HashMap<>();
        params.put("success_url","http://localhost:8080/home/orders.html?nav=payment");
        params.put("cancel_url","http://localhost:8080/home/order-cancel.html");
        params.put("payment_method_types",paymentMethodTypes);
        
        params.put("line_items", lineItems);
        params.put("mode", "payment");

        params.put("shipping_rates[]", "shr_1JaKRJGpCsnZmArEKQzOIgRJ");

        params.put("allow_promotion_codes", true);
        
        params.put("shipping_address_collection", shipping_address);

        Session session = Session.create(params);


        JSONObject sessionResult = (JSONObject) jsonParser.parse(session.toJson());     
        
//        System.out.println("sessionResult: " + sessionResult);

        return sessionResult;
    }
}
