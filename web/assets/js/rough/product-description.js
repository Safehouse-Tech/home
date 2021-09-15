/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global decodeURIComponent */

// http://localhost:8080/viper/api_data?eui=E5:81:79:46:8D:16&parameters=temperature,humidity,noise,light,voc,eco2&technology_type=bluetooth&token=fKozYbHaa8bU3HPaktx5xn13l3aII0zi



var queryString = new Array();

var product_id, product_name;

var products_values= localStorage.getItem('products_values');
var product_values = JSON.parse(products_values);

/*
$(function () 
{
    if (queryString.length ===0) {
        if (window.location.search.length > 1) 
        {
            var params = window.location.search.split("=");

            var param = decodeURIComponent(params[1]).split(",");

            product_id      = param[0];
            product_name    = param[1];

        }
    }      
});
*/

var login = 0;

$(document).ready(function(){
    
    console.log("product_values.image_path: "+ product_values.image_path);
    
    $("#product_id").text(product_values.product_id);
    $("#product_name").text(product_values.product_name);
    $("#product_price").text(product_values.price);
    
    $("#product_technology").text(product_values.technology);
    
    $("#product_extra").text(product_values.extra);
    
    
    var allimages = product_values.image_path+'';
    
    var images = allimages.split(",");
    
    $("#image-slider").empty();
    
    for (var i=0;i<images.length;i++)
    {
        $("#image-slider").append('<div class="swiper-slide">\n\
                                        <img class="descriptionimageheight " src="'+images[i]+'" alt="">\n\
                                    </div>');
    }
    
    
    for (i=1;i<=100;i++)
    {
        $("#select_quantity").append($('<option></option>').val(i).html(i));
    }
    
    var path = "product-description/"+ product_values.description_path ;
    $("#product_description_div").load(path); 
    
});

function addtobasket()
{
     $("#staticBackdrop").modal('show'); 
     
     $("#staticBackdrop_body").load("login.html?nav=shop"); 
     
//    if(login===0)
//    {
//       $("#staticBackdrop").modal('show'); 
//       login=1;
//    }
//    else{
//       // window.location.replace('basket.html');
//    }
    
}
