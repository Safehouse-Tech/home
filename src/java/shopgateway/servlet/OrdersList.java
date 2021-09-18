/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shopgateway.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import shopgateway.config.CONFIG;
import shopgateway.javaclass.Orders;


/**
 *
 * @author gagan
 */
@WebServlet(name = "orderslist", urlPatterns = {"/orderslist"})
public class OrdersList extends HttpServlet {

    public OrdersList() {
        super();
    }

    JSONParser jsonParser = new JSONParser();
    
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter printWriter = response.getWriter();

        try {
            String authkey = request.getParameter(CONFIG.key);
            String methodstring = request.getParameter(CONFIG.method);
            String person_id = request.getParameter(CONFIG.person_id);

            if (authkey == null || methodstring == null || person_id == null ) {

                printError(printWriter, CONFIG.RESULT_BAD_REQUEST, "Bad Syntax", request);
                return;
            }

            if (!authkey.equals(CONFIG.AUTHKEY)) {

                printError(printWriter, CONFIG.RESULT_UNAUTHORISED_ACCESS, "Unauthorised Access", request);
                return;
            }

            int method = Integer.parseInt(methodstring);
            switch (method) {
                case CONFIG.CREATION: {


                    printResponse(printWriter, null);

                    break;
                }

                

                default:

                    printError(printWriter, CONFIG.RESULT_BAD_REQUEST, "Bad Syntax", request);
                    break;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            printError(printWriter, CONFIG.RESULT_SERVER_ERROR, ex.getMessage(), request);
        }
    }

    private void printResponse(PrintWriter printWriter, Object result) {
        Map<String, Object> obj = new LinkedHashMap<>();
        obj.put("status", CONFIG.RESULT_SUCCESS);
        obj.put("error", null);
        obj.put("extra", result);

        printWriter.println(JSONValue.toJSONString(obj));
    }

    private void printError(PrintWriter printWriter, String status, String error, HttpServletRequest request) {
        // print error result
        Map<String, String> response = new LinkedHashMap<>();
        response.put("status", status);
        response.put("error", error);
        response.put("extra", null);

        printWriter.println(JSONValue.toJSONString(response));
    }

    
}
