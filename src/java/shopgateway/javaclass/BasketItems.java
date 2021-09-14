/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shopgateway.javaclass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import shopgateway.config.ODBConnection;

/**
 *
 * @author gagan
 */
public class BasketItems {

    public BasketItems() {
    }

    JSONParser jsonParser = new JSONParser();
    Connection conn = ODBConnection.getInstance().connection;

    public JSONObject retrieveBasket(String person_id) {

        // fetch basket items with reference to person_id
        JSONObject result = new JSONObject();
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {

            JSONObject basketSession = new JSONObject();
            long basketId = 0;
            JSONObject basketItems = new JSONObject();
            JSONObject previousOrders = new JSONObject();

            String check_personBasket = "Select * from SHOP_ORDERS where PERSON_ID = '" + person_id + "' and BASKET_STATUS = 'basket' ";
            pst = conn.prepareStatement(check_personBasket);
            rs = pst.executeQuery();
            if (rs.next()) {
                basketId = rs.getLong("BASKET_ID");
                basketItems = (JSONObject) jsonParser.parse(rs.getString("BASKET_ITEMS"));
            } else {

                String sqlIdentifier = "select SHOP_SEQ.NEXTVAL from dual";
                pst = conn.prepareStatement(sqlIdentifier);
                synchronized (this) {
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        basketId = rs.getLong(1);
                    }
                }

                String insert_basket = "insert into SHOP_ORDERS (BASKET_ID, PERSON_ID, BASKET_STATUS)"
                        + "values (?,?,?)";

                pst = conn.prepareStatement(insert_basket);

                pst.setLong(1, basketId);
                pst.setString(2, person_id);
                pst.setString(3, "basket");
                pst.executeUpdate();
            }

            /*
            
            write same sql to get previous orders where status is ordered or deliverd
             */
            basketSession.put("basketId", basketId);
            basketSession.put("basketItems", basketItems);

            result.put("basketSession", basketSession);
            result.put("previousOrders", previousOrders);

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

        return result;
    }

    public String updateBasket(String person_id, String basket_id, JSONObject basketItems) {

        String result = "basket items not updated";

        PreparedStatement pst = null;
        ResultSet rs = null;

        try 
        {
            String basketUpdate = "Update SHOP_ORDERS set "
                    + "BASKET_ITEMS= '" + basketItems + "'"
                    + "where BASKET_ID = '" + basket_id + "' ";
            pst = conn.prepareStatement(basketUpdate);

            result = (pst.executeUpdate() > 0) ? "basket items updated" : "basket items not updated";

        } catch (SQLException ex) {
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
        return result;
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
