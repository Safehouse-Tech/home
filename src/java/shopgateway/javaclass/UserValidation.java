/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shopgateway.javaclass;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import org.json.simple.JSONObject;
import shopgateway.config.ODBConnection;

/**
 *
 * @author gagan
 */
public class UserValidation {
    
    public UserValidation(){}
    
    Connection conn = ODBConnection.getInstance().connection;
    
    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe

    public static String generateNewToken() 
    {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
    
    public JSONObject validateUser(String email, String password) throws SQLException
    {
        JSONObject validate = new JSONObject();
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        try
        {     
            email = email.toLowerCase().replaceAll(" ", "");
            String encrypt_password = Cipher.generateDigest(password);
            
            String emailnum = "select count(*) from USER_CREDENTIALS where LOWER(USERNAME) = '"+email+"' and PASSWORD ='"+encrypt_password+"'";
            pst = conn.prepareStatement(emailnum);
            rs=pst.executeQuery();    
            rs.next();              //int userexist = rs.getInt(1);
            
            String userExist = (rs.getInt(1) > 0) ? "user exists" : null;
            
//            String person_id  = null;
            JSONObject personDetails = new JSONObject();
            
            if("user exists".equals(userExist) ) 
            {
                String sqlpersonId = "select * from PERSON where LOWER(USERNAME) = '"+email+"' ";
                pst = conn.prepareStatement(sqlpersonId);
                rs = pst.executeQuery();
                while(rs.next()) 
                {
                    personDetails.put("person_id", rs.getString("PERSON_ID") );
                    personDetails.put("person_name", rs.getString("NAME") );
                    personDetails.put("person_email", rs.getString("EMAIL") );
                }
            }
            
            validate.put("userExist", userExist);
            validate.put("personDetails", personDetails);
            
        }
        catch(NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException | SQLException ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            if (rs != null) { rs.close(); }

            if (pst != null) { pst.close(); }
        }
        
        return validate;
    }
    
/*    
    public JSONObject retrieveUserDetails(String email, String person_id) throws SQLException, java.text.ParseException
    {
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        JSONObject result = new JSONObject(); 
        JSONObject previousOrders = new JSONObject();
        
        
        try{
            String sql2 = "SELECT * from SAFEHOUSE_DASHBOARD_USER WHERE PERSON_ID='"+person_id+"' ";
            pst = conn.prepareStatement(sql2);
            rs = pst.executeQuery();
            
            if(rs.next())
            {
                
            }
            
            BasketItems bitems = new BasketItems();
            JSONObject basketSession = bitems.retrieveBasket(person_id);
            
//            result.put("subscription", subscription) ;
            result.put("basketSession", basketSession);
            result.put("previousOrders", previousOrders);
        }
        catch(SQLException  ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            if (rs != null) { rs.close(); }

            if (pst != null) { pst.close(); }
        }
        
        return result;
    }
*/    
    
    public boolean userLogout(String email, String person_id, String session_token) throws SQLException
    {
        boolean result = false;
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        try{
            String sql_update=  "Update LOGIN_HISTORY SET LOGIN_OUT = systimestamp AT TIME ZONE 'Europe/London' where LOGIN_AT IN "
                            + " ( select LOGIN_AT from "
                            + "     (Select * from LOGIN_HISTORY where LOWER(USERNAME) = '"+email+"' order by LOGIN_AT desc) "
                            + " WHERE rownum = 1 )   ";

            pst = conn.prepareStatement(sql_update);
            int result0 = pst.executeUpdate();
            
            String updateToken = "Update SAFEHOUSE_DASHBOARD_USER set "
                        + "SESSION_TOKEN = '' "
                        + "where PERSON_ID='"+person_id+"' and SESSION_TOKEN = '"+session_token+"' ";

            pst = conn.prepareStatement(updateToken);
            int result1 = pst.executeUpdate();
            
            result = ( result0>0 && result1>0) ;
            
        }
        catch(SQLException  ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            if (rs != null) { rs.close(); }

            if (pst != null) { pst.close(); }
        }
        return result;
    }
    
    
}
