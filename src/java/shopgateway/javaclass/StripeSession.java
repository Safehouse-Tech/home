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
        
        basketItems.keySet().forEach(keyStr ->
        {
            JSONObject keyvalue = (JSONObject) basketItems.get(keyStr);
            
            Map<String, Object> lineItem = new HashMap<>();
            lineItem.put("price", keyvalue.get("price_id"));
            lineItem.put("quantity", keyvalue.get("quantity"));
            
            lineItems.add(lineItem);
        });
        
        System.out.println("lineItems: " + lineItems);
        
        
   // /*    
        Map<String, Object> params = new HashMap<>();
        params.put(
                "success_url",
                "http://localhost:8080/TEST/success"
        );
        params.put(
                "cancel_url",
                "https://example.com/cancel"
        );
        params.put(
                "payment_method_types",
                paymentMethodTypes
        );
        params.put("line_items", lineItems);
        params.put("mode", "payment");

        params.put("shipping_rates[]", "shr_1JaKRJGpCsnZmArEKQzOIgRJ");

        params.put("allow_promotion_codes", true);

//        params.put("payment_intent_data", payment);
//            System.out.println("params: "+ params);
        Session session = Session.create(params);

//            System.out.println("get order id / payment id");

        JSONObject sessionResult = (JSONObject) jsonParser.parse(session.toJson());
 // */       
//        sessionResult =  {"metadata":{},"after_expiration":null,"livemode":false,"amount_total":800,"line_items":null,"subscription":null,"locale":null,"mode":"payment","customer_details":null,"consent_collection":null,"expires_at":1631904326,"allow_promotion_codes":true,"shipping":null,"client_reference_id":null,"currency":"gbp","id":"cs_test_b1cSTe5HVfwoqDhXOrAeyEQv6Xu3ZYgkMVtgW65tMasOL4a1CiwCbThXuo","payment_method_options":{"acss_debit":null,"oxxo":null,"boleto":null},"billing_address_collection":null,"success_url":"http:\/\/localhost:8080\/TEST\/success","setup_intent":null,"shipping_address_collection":null,"payment_method_types":["card"],"total_details":{"amount_tax":0,"amount_discount":0,"breakdown":null,"amount_shipping":500},"payment_status":"unpaid","consent":null,"url":"https:\/\/checkout.stripe.com\/pay\/cs_test_b1cSTe5HVfwoqDhXOrAeyEQv6Xu3ZYgkMVtgW65tMasOL4a1CiwCbThXuo#fidkdWxOYHwnPyd1blpxYHZxWjA0T1V0cVFCdUZ2a19oRHdAMTxjNEJ2ZjZHdXBHZExKV3xAbTRdYUF2TlRvcUdrajV2VkJrVklrdjM0TjNCS1RdR3JxdFxLYkRTbTZRTHx1YXZRSGljMUxANTU8dD1idVFiQScpJ2N3amhWYHdzYHcnP3F3cGApJ2lkfGpwcVF8dWAnPydocGlxbFpscWBoJyknYGtkZ2lgVWlkZmBtamlhYHd2Jz9xd3BgeCUl","recovered_from":null,"submit_type":null,"automatic_tax":{"enabled":false,"status":null},"tax_id_collection":null,"customer_email":null,"payment_intent":"pi_3JaPaMGpCsnZmArE1JZQb6b0","cancel_url":"https:\/\/example.com\/cancel","amount_subtotal":300,"customer":null,"object":"checkout.session"};
        

       // System.out.println("sessionResult: " + sessionResult);

        return sessionResult;
    }
}
