/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var sessionDetails= new Object();

var products_values = new Object();
var product_values;

var sensorDescription = new Object();
var loginDetails = new Object();

$(document).ready(function(){
    
});



function shopProduct(product_id, product_name, price, image_path, description_path, technology, extra)
{

//    products_values = {"product_id": product_id, "product_name": product_name, "price": price, 'image_path': image_path,
//                            "description_path": description_path, "technology": technology, "extra": extra}; 

 //   console.log('products_values', products_values);
 
    sensorDescription.product_id = product_id;
    sensorDescription.product_name = product_name;
    sensorDescription.price = price;
    sensorDescription.image_path = image_path;
    sensorDescription.description_path = description_path;
    sensorDescription.technology = technology;
    sensorDescription.extra = extra;
 

    sessionDetails.sensorDescription = sensorDescription;               //console.log('sessionDetails', JSON.stringify(sessionDetails));
    

    localStorage.setItem('sessionDetails', JSON.stringify(sessionDetails)); 
   

    window.location.href = 'product-description.html';


}


function productDescription()
{
    sessionDetails = JSON.parse(localStorage.getItem('sessionDetails'));
    
    sensorDescription = sessionDetails.sensorDescription;
    
    $("#product_id").text(sensorDescription.product_id);
    $("#product_name").text(sensorDescription.product_name);
    $("#product_price").text(sensorDescription.price);
    
    $("#product_technology").text(sensorDescription.technology);
    
    $("#product_extra").text(sensorDescription.extra);
    
    
    var allimages = sensorDescription.image_path+'';
    
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
    
    var path = "product-description/"+ sensorDescription.description_path ;
    $("#product_description_div").load(path); 
    
}

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

function customerLogin()
{
    
    loginDetails.person_name = "Gagan";
    
    $("#loginName").text("Gagan");
    
    
    sessionDetails = JSON.parse(localStorage.getItem('sessionDetails'));
    sessionDetails.loginDetails = loginDetails;
    localStorage.setItem('sessionDetails', JSON.stringify(sessionDetails)); 
    
    
    console.log('sessionDetails', JSON.stringify(JSON.parse(localStorage.getItem('sessionDetails'))));
    
 };