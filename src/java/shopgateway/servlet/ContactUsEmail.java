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
import java.util.Properties;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONValue;
import shopgateway.config.CONFIG;
import shopgateway.javaclass.SendEmail;

/**
 *
 * @author gagan
 */
@WebServlet(name = "contactusemail", urlPatterns = {"/contactusemail"})
public class ContactUsEmail extends HttpServlet {

    public ContactUsEmail() {
        super();
    }

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter printWriter = response.getWriter();

        try {

            String contactusName = request.getParameter(CONFIG.contactusName);
            String contactusEmail = request.getParameter(CONFIG.contactusEmail);
            String contactusSubject = request.getParameter(CONFIG.contactusSubject);
            String contactusMessage = request.getParameter(CONFIG.contactusMessage);

            SendEmail sm = new SendEmail();
            String mailAction = sm.sendcontactUSEmail(getServletContext(),  contactusName, contactusEmail, contactusSubject, contactusMessage);
            
//            String mailAction = "Email action executed successfully";
            
            printResponse(printWriter, mailAction);
            
            
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
