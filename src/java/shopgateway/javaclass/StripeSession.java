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

/**
 *
 * @author gagan
 */
public class StripeSession {

    public StripeSession() {
    }

    JSONParser jsonParser = new JSONParser();
    
    public JSONObject createSession(JSONObject basketItems, String shippingCode) throws StripeException, ParseException {

        Stripe.apiKey = "sk_test_51JPqtTGpCsnZmArETlyBBu1ut4PoOsIfxihO1srVaAXDOSRqzBH8zyKO2ymWWznIapvYC8qCIRWOtLQeQ73uGDFs00LDHtuCAc";

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
        
       // System.out.println("lineItems: " + lineItems);
        
        
   // /*    
        Map<String, Object> params = new HashMap<>();
        params.put(
                "success_url",
                "http://localhost:8080/home/orders.html?nav=payment"
        );
        params.put(
                "cancel_url",
                "http://localhost:8080/home/order-cancel.html"
        );
        params.put(
                "payment_method_types",
                paymentMethodTypes
        );
        params.put("line_items", lineItems);
        params.put("mode", "payment");

        params.put("shipping_rates[]", "shr_1JaKRJGpCsnZmArEKQzOIgRJ");

        params.put("allow_promotion_codes", true);

//        params.put("payment_intent_data", payment);           //            System.out.println("params: "+ params);
        Session session = Session.create(params);

//            System.out.println("get order id / payment id");

        JSONObject sessionResult = (JSONObject) jsonParser.parse(session.toJson());
 // */       
        
        System.out.println("sessionResult: " + sessionResult);

        return sessionResult;
    }
}
