/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var products_values  = localStorage.getItem('products_values');
var product_values = JSON.parse(products_values);


$(document).ready( function() 
{    

    console.log("product_values: ", product_values);
    var currentURL = window.location.href;           //console.log("currentURL: "+ currentURL);

    if(currentURL.includes("login") ) //|| pagehref[1].includes("starter")
    {
        $("#pagecontainer").addClass('vh-100');
    }
    else {
        $("#pagecontainer").removeClass('vh-100');
    }

       
    $("#loginForm").submit(function()
    {

        $("#loginName").text("Gagan");
        return false;
    });
    
         
});