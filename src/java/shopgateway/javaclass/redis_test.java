/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shopgateway.javaclass;

import redis.clients.jedis.Jedis;

/**
 *
 * @author gagan
 */
public class redis_test {
    
    
    public static void main(String args[])
    {
        // on server change to private IP  : 10.0.0.191
        // for testing use public IP : 63.33.164.36
        Jedis jedis = new Jedis("3.250.215.199");
        
        
//        jedis.set("uid102", "{ loggedIn: true, cartItems: [{ pid: 1, qty 2 }] }");
//        String value = jedis.get("uid102");
//        
        System.out.println("value:"+ jedis.isConnected());
        
        
//        jedis.expire("uid102", 10);
        
        // uid should be person ID:  and look for expiry
        
//        jedis.del("test-session1");
        
//        String value1 = jedis.get("test-session1");
//        
//        System.out.println("value1:"+ value1);
        
//         Jedis jedis = new Jedis("34.254.191.66"); 
//      System.out.println("Connection to server sucessfully"); 
//      //check whether server is running or not 
//      System.out.println("Server is running: "+jedis.ping()); 
    }
}
