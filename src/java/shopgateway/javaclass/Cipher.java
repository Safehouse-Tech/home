/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shopgateway.javaclass;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import shopgateway.config.CONFIG;

/**
 *
 * @author gagan
 */
public class Cipher {
    
    
    public static String generateDigest(String password) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException 
    {
        byte[] salt = getSalt();
        byte[] psw = getPasswordHash(password, salt);
        
        return getHexString(psw);
    }
    
    
    private static byte[] getSalt() throws NoSuchAlgorithmException
    {		
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(CONFIG.CIPHERKEY.getBytes());
        byte[] bytes = md.digest(CONFIG.CIPHERKEY.getBytes());

        return bytes;
    }
    
    private static byte[] getPasswordHash(String password, byte[] salt) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException 
    {
    	int iterationCount = 3810;
    	int keyLength = 256;
    	
        char[] pswchars = password.toCharArray();      
        byte[] _salt = (salt == null) ? getSalt() : salt;
         
        PBEKeySpec spec = new PBEKeySpec(pswchars, _salt, iterationCount, keyLength);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        
        return hash;
    }
    
    private static String getHexString(byte[] bytes) throws NoSuchAlgorithmException
    {
        BigInteger bi = new BigInteger(1, bytes);
        String hexstring = bi.toString(16);
        int paddingLength = (bytes.length * 2) - hexstring.length();
        
        return (paddingLength > 0) ? String.format("%0" + paddingLength + "d", 0) + hexstring : hexstring;
    }
    
}
