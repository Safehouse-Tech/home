/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shopgateway.javaclass;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.LineItemCollection;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import shopgateway.config.CONFIG;
import shopgateway.config.ODBConnection;

/**
 *
 * @author gagan
 */
public class Orders {

    public Orders() {
    }

    JSONParser jsonParser = new JSONParser();
    Connection conn = ODBConnection.getInstance().connection;

    public JSONArray retrieveOrders(String person_id) {

        PreparedStatement pst = null;
        ResultSet rs = null;

        JSONArray previousOrders = new JSONArray();

        try {

            String check_personBasket = "Select * from SHOP_ORDERS where PERSON_ID = '" + person_id + "' and BASKET_STATUS = 'ordered' ";
            pst = conn.prepareStatement(check_personBasket);
            rs = pst.executeQuery();
            while (rs.next()) {

                JSONObject result = new JSONObject();
                result.put("order_timestamp", rs.getString("ORDER_TIMESTAMP"));
                result.put("order_id", rs.getString("ORDER_ID"));
                result.put("total_items", rs.getString("TOTAL_ITEMS"));
                result.put("checkout_items", rs.getString("CHECKOUT_ITEMS"));
                result.put("payment_status", rs.getString("PAYMENT_STATUS"));
                result.put("order_receipt", rs.getString("ORDER_RECEIPT"));
                
                // fetch installation address from installation id 
                result.put("installation_address", rs.getString("INSTALLATION_ADDRESS_ID"));
                
                result.put("shipping_address", rs.getString("SHIPPING_ADDRESS"));
                result.put("shipping_id", rs.getString("SHIPPING_ID"));
                result.put("shipping_status", rs.getString("SHIPPING_STATUS"));
                result.put("order_status", rs.getString("ORDER_STATUS"));
                result.put("order_amount", rs.getString("ORDER_AMOUNT"));
                result.put("order_amount_received", rs.getString("ORDER_AMOUNT_RECEIVED"));

                result.put("payment_currency", rs.getString("PAYMENT_CURRENCY"));
                
                previousOrders.add(result);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(BasketItems.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(BasketItems.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return previousOrders;
    }

    public JSONObject updateOrders(String person_id, String basket_id, String checkoutSession_id, String payment_intent_ID) {

        JSONObject newbasketSession = new JSONObject();
        
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {

            JSONObject paymentIntent = retrievePaymentIntent(payment_intent_ID);    //            System.out.println("paymentIntent: "+ paymentIntent); 
            JSONObject checkoutItems = retrieveCheckoutHistory(checkoutSession_id); //            System.out.println("orderedItems: "+ orderedItems);

            long orderAmount = (Long) paymentIntent.get("amount");
            long orderAmountReceived = (Long) paymentIntent.get("amount_received");
            long createdAt = (Long) paymentIntent.get("created");
            String paymentStatus = (String) paymentIntent.get("status");
            String paymentCurrency = (String) paymentIntent.get("currency");
            JSONObject shippingAddress = (JSONObject) paymentIntent.get("shipping");
            
            String orderId = payment_intent_ID.replaceAll("pi_", "SAFE-#");

            JSONObject charges = (JSONObject) paymentIntent.get("charges");
            JSONArray chargesData = (JSONArray) charges.get("data");
            JSONObject dataObject = (JSONObject) chargesData.get(0);

            String orderReceipt = (String) dataObject.get("receipt_url");
            
            long totalItems = 0;
            JSONArray checkoutData = (JSONArray) checkoutItems.get("data");
            for(int i = 0; i < checkoutData.size(); i++)
            {
                JSONObject checkoutDataObj = (JSONObject) checkoutData.get(i);
                totalItems = totalItems + (Long) checkoutDataObj.get("quantity");
            }
            
            

            String orderUpdate = "Update SHOP_ORDERS set "
                    + "BASKET_ITEMS= '', "
                    + "BASKET_STATUS= 'ordered', "
          
                    + "TOTAL_ITEMS= '" + totalItems + "', "
                    + "ORDER_TIMESTAMP= '" + createdAt + "', "
                    + "CHECKOUT_SESSION_ID= '" + checkoutSession_id + "', "
                    + "CHECKOUT_ITEMS= '" + checkoutItems + "', "
                    + "PAYMENT_INTENT_ID= '" + payment_intent_ID + "', "
                    + "PAYMENT_STATUS= '" + paymentStatus + "', "
                    //                    + "PAYMENT_INTENT= '" + paymentIntentClob + "' ,"

                    + "ORDER_RECEIPT= '" + orderReceipt + "', "
            // installation id insertion
                    + "SHIPPING_ADDRESS= '" + shippingAddress + "', "
                    + "SHIPPING_STATUS = 'pending', "
                    + "ORDER_STATUS = 'in progess', "
                    + "ORDER_AMOUNT = '"+orderAmount+"', "
                    + "ORDER_AMOUNT_RECEIVED = '"+orderAmountReceived+"', "
                    + "PAYMENT_CURRENCY = '"+paymentCurrency+"', "
                    + "ORDER_ID = '"+orderId+"' "
                    
                    + "where BASKET_ID = '" + basket_id + "' ";
            
            pst = conn.prepareStatement(orderUpdate);

//            pst.executeUpdate();

                
            if(pst.executeUpdate()>0)
            {
                BasketItems bi = new BasketItems();
                newbasketSession = bi.retrieveBasket(person_id);
            }
            
             
        } catch (Exception ex) {
            ex.printStackTrace();
            
            // if order not updated then send alert email with pi_ id and CHECKOUT_ITEMS to gagan
            
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(BasketItems.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(BasketItems.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return newbasketSession;
    }

    public JSONObject retrievePaymentIntent(String payment_intent) throws StripeException, ParseException {

        Stripe.apiKey = CONFIG.STRIPEKEY;

        //JSONObject paymentIntent = new JSONObject(); // PaymentIntent payment_Intent = PaymentIntent.retrieve(payment_intent) ;
        JSONObject paymentIntent = (JSONObject) jsonParser.parse(PaymentIntent.retrieve(payment_intent).toJson());

        return paymentIntent;
    }

    public JSONObject retrieveCheckoutHistory(String checkoutSession_id) throws StripeException, ParseException {

        Stripe.apiKey = CONFIG.STRIPEKEY;

        Session session = Session.retrieve(checkoutSession_id);
        Map<String, Object> params = new HashMap<>();
        params.put("limit", 5);
        //JSONObject CheckoutHistory = new JSONObject();  
        //LineItemCollection lineItems = session.listLineItems(params);   System.out.println("lineItems: " + lineItems);

        JSONObject checkoutItems = (JSONObject) jsonParser.parse(session.listLineItems(params).toJson());

        return checkoutItems;
    }

}

/*



Clob paymentIntentClob = conn.createClob();
paymentIntentClob.setString(1, paymentIntent.toJSONString());

System.out.println("paymentIntentClob"+ paymentIntentClob);

*/
