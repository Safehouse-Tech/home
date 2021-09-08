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
import shopgateway.config.CONFIG;
import shopgateway.javaclass.UserValidation;

/**
 *
 * @author gagan
 */

@WebServlet(name = "usercredentials", urlPatterns = {"/usercredentials"})
public class UserCredentials extends HttpServlet {

   
    public UserCredentials() 
    {
        super();
    }
    
    
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
    	PrintWriter printWriter = response.getWriter();
        
        try
        {
            String authkey = request.getParameter(CONFIG.key);
            String methodstring = request.getParameter(CONFIG.method);
            String email = request.getParameter(CONFIG.email);
            String password = request.getParameter(CONFIG.password);
            
            String person_id = request.getParameter(CONFIG.person_id);

            if(authkey == null || methodstring == null || email == null)
            {
                 System.out.println("wcwec");
                printError(printWriter, CONFIG.RESULT_BAD_REQUEST, "Bad Syntax", request);
                return;
            }

            if(!authkey.equals(CONFIG.AUTHKEY))
            {
                
                printError(printWriter, CONFIG.RESULT_UNAUTHORISED_ACCESS, "Unauthorised Access", request);
                return;
            }
            
            UserValidation vu = new UserValidation();
    
            int method = Integer.parseInt(methodstring);
            switch (method) 
            {
                case CONFIG.LOGIN:
                {
// http://localhost:8080/viper-gateway/usercredentials?key=BzJKl8b4UQ76nLw&method=1002&email=gaganpreet.singh@safehouse.technology&password=111
                    
                    JSONObject validate = vu.validateUser(email, password);
            
                    if(validate.get("userExist") == null )
                    {
                        printError(printWriter, CONFIG.RESULT_FAILED, "Invalid Login Detail", request);
                        return;
                    }

                    if(validate.get("personDetails")== null)
                    {
                        printError(printWriter, CONFIG.RESULT_FAILED, "Person Do Not Exists.", request);
                        return;
                    }

                    JSONObject personDetails = (JSONObject) validate.get("personDetails");
                    
                    String personId = personDetails.get("person_id").toString() ;
                    
                    JSONObject result = vu.retrieveUserDetails( email, personId) ; //generate login token for session;
                    
                    validate.put("previousOrders", result.get("personOrders"));
                    
                    
                    printResponse(printWriter, validate);
                    
                    break;
                }
                
                case CONFIG.LOGOUT:
                {
// http://localhost:8080/viper-gateway/usercredentials?key=BzJKl8b4UQ76nLw&method=1003&email=gaganpreet.singh@safehouse.technology&person_id=1672&session_token=7fx-2FxvTdousidOLWkhPFs_8JB-Y8wm
                   
                    String session_token = request.getParameter(CONFIG.session_token);
                    boolean result = vu.userLogout(email, person_id, session_token);
                    
                    printResponse(printWriter, result);
                    break;
                }
                    
                default:
                   
                    printError(printWriter, CONFIG.RESULT_BAD_REQUEST, "Bad Syntax", request);
                break;
            }

            
            
        }
        catch (Exception ex)
    	{
            ex.printStackTrace();
            printError(printWriter, CONFIG.RESULT_SERVER_ERROR, ex.getMessage(), request);
       	}
    }
    
    private void printResponse(PrintWriter printWriter, Object result)
    {
        Map<String, Object> obj = new LinkedHashMap<>();
        obj.put("status", CONFIG.RESULT_SUCCESS);
        obj.put("error", null);
        obj.put("extra", result);
       
        printWriter.println(JSONValue.toJSONString(obj));
    }
    
    private void printError(PrintWriter printWriter, String status, String error, HttpServletRequest request)
    {
		// print error result
        Map<String, String> response = new LinkedHashMap<>();
        response.put("status", status);
        response.put("error", error);
        response.put("extra", null);

        printWriter.println(JSONValue.toJSONString(response));
        
        
    }
}
