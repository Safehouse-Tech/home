/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shopgateway.javaclass;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletContext;
/**
 *
 * @author gagan
 */
public class SendEmail {
    
    private static final String smtp = "smtp.gmail.com";
    private static final String port = "587";
    private static final String email = "info@safehouse.technology"; //     "info@csintegration.co.uk";
    private static final String password = "Tontotonto007";         //          "Csitonto007";  
	
    static String hostname;
    
    public SendEmail()
    {	
    	// set hostname to user with id for attach the image in e-mail body
    	try
        {
            hostname = java.net.InetAddress.getLocalHost().getCanonicalHostName();
        }
        catch (UnknownHostException e)
        {
                // we can't find our hostname? okay, use something no one else is likely to use
            hostname = new Random(System.currentTimeMillis()).nextInt(100000) + ".localhost";
        }
    }
    
    
    public String sendcontactUSEmail(ServletContext context, String contactusName, String contactusEmail, String contactusSubject, String contactusMessage) throws MessagingException, IOException
    {   
        String resp ="";
        try {
            Session session = getSession();        	
            MimeMessage message = sendMessage(context, session, contactusName, contactusEmail, contactusSubject, contactusMessage);

            Transport.send(message); // Send message
            resp = "Email action executed successfully";
        }
        catch (MessagingException e) 
        {
            e.printStackTrace();
            resp = "Email action error";
            System.out.println("resp" + resp);
            throw new RuntimeException(e);
        }  
        return resp;
    }
    
    private Session getSession()
    {
        Properties properties = new Properties();

        // Setup mail server
        properties.put("mail.smtp.host", smtp);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // setup authenticator 
        Authenticator auth = new Authenticator() 
        {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(email, password);
            }
        };   

    // return the default Session object.
        return Session.getInstance(properties, auth);
    } 
     
    private MimeMessage sendMessage(ServletContext context, Session session, String contactusName, String contactusEmail, String contactusSubject, String contactusMessage) throws MessagingException, IOException
    {
        String defautSendTo = "gaganpreet.singh@safehouse.technology";
        String defautSubject = "Message from Safehouse Website";
    	// Create a default MimeMessage object.
        MimeMessage message = new MimeMessage(session);

        // Set From: header field of the header.
        message.setFrom(new InternetAddress(email));

        // Set To: header field of the header.
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(defautSendTo));

        // Set Subject: header field
        message.setSubject(defautSubject);

 
        // get the id from the image attachment, in order to display the image with in the message body
        String cid = getContentId();

        
        // Create a multipart message
        MimeMultipart content = new MimeMultipart("related");
        
        
        // Create the message part 
        MimeBodyPart textPart = new MimeBodyPart();  
          
        String html = "<!DOCTYPE html><html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><title>Website Messge</title></head><body leftmargin=\"0\" marginwidth=\"0\" topmargin=\"0\" marginheight=\"0\" offset=\"0\"><div style=\"background-color: #f5f5f5; width:100%; -webkit-text-size-adjust:none !important; margin:0; padding: 70px 0 70px 0;\"><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" height=\"100%\" width=\"100%\"><tr><td align=\"center\" valign=\"top\"><div id=\"template_header_image\"></div><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" id=\"template_container\" style=\"box-shadow:0 0 0 3px rgba(0,0,0,0.025) !important; border-radius:6px !important; background-color: #fdfdfd; border: 1px solid #dcdcdc; border-radius:6px !important; \"><tr><td align=\"center\" valign=\"top\"><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" id=\"template_header\" style=\" background-color: #3CB6E1; color: #ffffff; border-top-left-radius:6px !important; border-top-right-radius:6px !important; border-bottom: 0; font-family:Arial;font-weight:bold; line-height:100%; vertical-align:middle;\" bgcolor=\"#557da1\"><tr><td><h1 style=\" color: #ffffff; margin:0; padding: 28px 24px; text-shadow: 0 1px 0 #7797b4; display:block; font-family:Arial; font-size:30px; font-weight:bold; text-align:left; line-height: 150%;\">"
							+ "Message from Safehouse Website"
				    + "</h1></td></tr></table></td></tr><tr><td align=\"center\" valign=\"top\"><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" id=\"template_body\"><tr><td valign=\"top\" style=\" background-color: #fdfdfd; border-radius:6px !important; \"><table border=\"0\" cellpadding=\"20\" cellspacing=\"0\" width=\"100%\"><tr><td valign=\"top\"><div style=\" color: #737373; font-family:Arial; font-size:14px; line-height:150%; text-align:left; \">"
//				    		+ "<p>This E-mail is intended for " + recipient + "</p> "
//				    		+ "<p>You have requested to reset your password. Here is your reset code for your password: <strong>" + reset_code + "</strong></p>"
				    		+ "<p>Email has been sent by: </p>"
                                                + "<p>Name: " + contactusName + "</p> "
                                                + "<p>Email: " + contactusEmail + "</p> "
                                                + "<p>Subject: " + contactusSubject + "</p> "
                                                + "<p>Message: " + contactusMessage + "</p> "
				    + "</div></td></tr></table></td></tr></table></td></tr><tr><td align=\"center\" valign=\"top\"><table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"600\" id=\"template_footer\" style=\"border-top:0; -webkit-border-radius:6px;\"><tr><td valign=\"top\"><table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"100%\"><tr><td colspan=\"2\" valign=\"middle\" id=\"credit\" style=\" border:0; color: #99b1c7; font-family: Arial; font-size:12px; line-height:125%; text-align:center; \">"
		    				+ "<p><img src=\"cid:" + cid + "\" align=\"middle\"/></p>"		
		    				+ "<p>Safehouse Technology | Mobile technology for delivering efficient health and social care services</p>"
			        + "</td></tr></table></td></tr></table></td></tr></table></td></tr></table></div></body></html>";
         
        textPart.setText(html, "US-ASCII", "html");
        
        // Set text message part
        content.addBodyPart(textPart);
        
        // Part two is attach safehouse image
        MimeBodyPart imagePart = new MimeBodyPart();
     
        imagePart.attachFile(context.getRealPath("/assets/img/safehouse-trademark-3D-192.png"));
        
        imagePart.setContentID("<" + cid + ">");
        imagePart.setDisposition(MimeBodyPart.INLINE);

        content.addBodyPart(imagePart);
        
        // Send the complete message parts
        message.setContent(content);
         
        return message;
    }
   
   
    /**
     * One possible way to generate very-likely-unique content IDs.
     * @return A content id that uses the hostname, the current time, and a sequence number
     * to avoid collision.
     */
    private String getContentId() 
    {
        int c = getSeq();
        return c + "." + System.currentTimeMillis() + "@" + hostname;
    }
    static int seq = 0;
    /**
     * Sequence goes from 0 to 100K, then starts up at 0 again.  This is large enough, 
     * and saves
     * @return
     */
    private synchronized int getSeq() {
        return (seq++) % 100000;
    }

   
}
