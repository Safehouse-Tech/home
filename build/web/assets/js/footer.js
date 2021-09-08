/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global  AOS*/



//var script = document.createElement('script');
//script.src = 'https://code.jquery.com/jquery-3.4.1.min.js';
//script.type = 'text/javascript';
//document.getElementsByTagName('head')[0].appendChild(script);

$(document).ready( function() 
{    
    $("#headerdiv").load("header.html");
    
    $("#calltoaction").load("call-to-action.html");
    
    $("#footerdiv").load("footer.html");
    
  //  AOS.init();  
  
    AOS.init({
        duration: 1200
    });
    
    
     
});


/*
var products_values  = localStorage.getItem('products_values');
var product_values   = JSON.parse(products_values);

$(document).ready( function() 
{
            
    $("#footerdiv").load("footer.html");
    
    console.log("product_values: "+ JSON.stringify(product_values));
    
    $("#alert_num").text('0') ;
    
    if(product_values!==null)
    {
        if (product_values.hasOwnProperty('basketbadgenum')) 
        {
            $("#alert_num").text(product_values.basketbadgenum) ;
        }
        else 
        {
            $("#alert_num").text('0') ;
        }
    }
    
  //  AOS.init();  
        
});


function addtobasket()
{
    
    var basketbadge = 0 ;
    
    if (product_values.hasOwnProperty('basketbadgenum')) 
    {
        console.log("product_values: "+ product_values.basketbadgenum);
        
        basketbadge = product_values.basketbadgenum + 1 ;
        
        $("#alert_num").text(basketbadge) ;
    }
    else 
    {
        basketbadge = basketbadge + 1 ;
    
        $("#alert_num").text(basketbadge) ;

        product_values.basketbadgenum = basketbadge ;
    }
    
    product_values.basketbadgenum = basketbadge ;
    
//    var updateproduct_values = product_values;
    
//    console.log("product_values: "+ JSON.stringify(updateproduct_values));
    
    localStorage.setItem('products_values', JSON.stringify(product_values)); 
    
    
}

*/