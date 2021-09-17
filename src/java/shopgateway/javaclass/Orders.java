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
    
    
    public JSONObject updateOrders(String person_id, String checkoutHistory, String payment_intent) {
        
        /*update order with 
            basket status = ordered
            payment intent : payment status
            full payment intent
        */
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        try{
            
            JSONObject paymentIntent = retrievePaymentIntent(payment_intent);
            JSONObject orderedItems  = retrieveCheckoutHistory(checkoutHistory);
            
//            System.out.println("paymentIntent: "+ paymentIntent);
//            System.out.println("orderedItems: "+ orderedItems);
            
            System.out.println(paymentIntent.get("created")); // already id // created  // status
            
            System.out.println(paymentIntent.get("status"));
            
            long time =  (Long) paymentIntent.get("created") ;
                    
//                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
//                    Date date = df.parse(String.valueOf(time));
//                    long currentTimestamp = date.getTime();
//                    System.out.println(currentTimestamp);
            



        LocalDateTime ldt = Instant.ofEpochMilli(time)
                .atZone(ZoneId.systemDefault()).toLocalDateTime();
        System.out.println(ldt);
        
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
    
    public JSONObject retrieveCheckoutHistory(String checkoutHistory) throws StripeException, ParseException {
        
        Stripe.apiKey = CONFIG.STRIPEKEY ;
        
        //Session session = Session.retrieve(checkoutHistory);
        //Map<String, Object> params = new HashMap<>();  params.put("limit", 5);
       //JSONObject CheckoutHistory = new JSONObject();  //LineItemCollection lineItems = session.listLineItems(params);   System.out.println("lineItems: " + lineItems);
        
        JSONObject CheckoutHistory = (JSONObject) jsonParser.parse(Session.retrieve(checkoutHistory).toJson()) ;
        
        return CheckoutHistory;
    }
    
    
}
