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
    
    
    public JSONObject retrieveOrders(String person_id) {
     
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        JSONArray orders = new JSONArray();
        
        try{
            
            String check_personBasket = "Select * from SHOP_ORDERS where PERSON_ID = '" + person_id + "' and BASKET_STATUS = 'ordered' ";
            pst = conn.prepareStatement(check_personBasket);
            rs = pst.executeQuery();
            while(rs.next()) {
               
                JSONObject result = new JSONObject();
                result.put("order_time", "");
                
                orders.add(result);
            } 
        }
         catch (Exception ex) {
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
        
        return null;
    }
    
    
    public JSONObject updateOrders(String person_id, String basket_id, String checkoutSession_id, String payment_intent_ID) {
        
        /*update order with 
            basket status = ordered
            payment intent : payment status
            full payment intent
        */
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        try{
            
            JSONObject paymentIntent = retrievePaymentIntent(payment_intent_ID);
            JSONObject checkoutItems  = retrieveCheckoutHistory(checkoutSession_id);
            
//            System.out.println("paymentIntent: "+ paymentIntent);
//            System.out.println("orderedItems: "+ orderedItems);
            long createdAt = (Long)paymentIntent.get("created") ;
             // already id // created  // status
            
            String paymentStatus = (String) paymentIntent.get("status");
            
                    
            String orderUpdate = "Update SHOP_ORDERS set "
                    
//                    + "BASKET_STATUS= 'ordered', "
//                    + "BASKET_ITEMS= '" + checkoutItems + "', "
                    + "ORDER_TIMESTAMP= '" + createdAt + "', "
                    + "PAYMENT_STATUS= '" + paymentStatus + "', "
                    + "PAYMENT_INTENT_ID= '" + payment_intent_ID + "', "
                    + "PAYMENT_INTENT= '" + paymentIntent + "' ,"
                    + "CHECKOUT_SESSION_ID= '" + checkoutSession_id + "' ,"
                    + "CHECKOUT_HISTORY_ITEMS= '" + checkoutItems + "' "
                    
                    + "where BASKET_ID = '" + basket_id + "' ";
            pst = conn.prepareStatement(orderUpdate);
            
            pst.executeUpdate(); 
        /*    
            if(pst.executeUpdate()>0)
            {
                BasketItems bi = new BasketItems();
                long newBasketId = bi.createNewBasket(person_id);
            }
            
            */
        }
        catch (Exception ex) {
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
        
        
        return null;
    }
    
    
    
    public JSONObject retrievePaymentIntent(String payment_intent) throws StripeException, ParseException {
        
        Stripe.apiKey = CONFIG.STRIPEKEY ;
        
        //JSONObject paymentIntent = new JSONObject(); // PaymentIntent payment_Intent = PaymentIntent.retrieve(payment_intent) ;
        
        JSONObject paymentIntent = (JSONObject) jsonParser.parse(PaymentIntent.retrieve(payment_intent).toJson());
        
        return paymentIntent;
    }
    
    public JSONObject retrieveCheckoutHistory(String checkoutSession_id) throws StripeException, ParseException {
        
        Stripe.apiKey = CONFIG.STRIPEKEY ;
        
        Session session = Session.retrieve(checkoutSession_id);
        Map<String, Object> params = new HashMap<>();  params.put("limit", 5);
       //JSONObject CheckoutHistory = new JSONObject();  
        //LineItemCollection lineItems = session.listLineItems(params);   System.out.println("lineItems: " + lineItems);
        
        JSONObject checkoutItems = (JSONObject) jsonParser.parse(session.listLineItems(params).toJson()) ;
        
        return checkoutItems;
    }
    
    
}
