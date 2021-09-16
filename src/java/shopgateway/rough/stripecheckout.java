/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shopgateway.rough;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Product;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gagan
 */
@WebServlet(name = "stripecheckout", urlPatterns = {"/stripecheckout"})
public class stripecheckout extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, StripeException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */

            Stripe.apiKey = "sk_test_51JPqtTGpCsnZmArETlyBBu1ut4PoOsIfxihO1srVaAXDOSRqzBH8zyKO2ymWWznIapvYC8qCIRWOtLQeQ73uGDFs00LDHtuCAc";

//            Product a = Product.retrieve("prod_KBQdddQBX3l7pR");
//
//            System.out.println("product: " + a);
//
//            Stripe.apiKey = "sk_test_51JPqtTGpCsnZmArETlyBBu1ut4PoOsIfxihO1srVaAXDOSRqzBH8zyKO2ymWWznIapvYC8qCIRWOtLQeQ73uGDFs00LDHtuCAc";

//            Price a = Price.retrieve("price_1JX3mgGpCsnZmArEgzvUViL6");
        

            List<Object> paymentMethodTypes = new ArrayList<>();
            paymentMethodTypes.add("card");
            List<Object> lineItems = new ArrayList<>();
            
            Map<String, Object> lineItem1 = new HashMap<>();
            lineItem1.put("price", "price_1JX3mgGpCsnZmArEgzvUViL6");
            lineItem1.put("quantity", 2);
            
            Map<String, Object> lineItem2 = new HashMap<>();
            lineItem2.put("price", "price_1JX3jfGpCsnZmArECw0S45GP");
            lineItem2.put("quantity", 1);
            
            
            lineItems.add(lineItem1);
            lineItems.add(lineItem2);
            
            System.out.println("lineItems: "+ lineItems);
                    
            
            Map<String,Object> payment = new HashMap<>();
            payment.put("receipt_email", "gagansingh2822@gmail.com");
            
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

            params.put("payment_intent_data", payment);

            System.out.println("params: "+ params);
            
            Session session = Session.create(params);
            
//            System.out.println("get order id / payment id");
            
            System.out.println("session: "+session);
            
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (StripeException ex) {
            Logger.getLogger(stripecheckout.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (StripeException ex) {
            Logger.getLogger(stripecheckout.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
