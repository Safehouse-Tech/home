/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global  AOS, fetch*/

var sessionDetails= new Object();

var sensorDescription = new Object();
var personDetails = new Object();

$(document).ready(function(){
    
    $("#headerdiv").load("header.html");
    
    $("#calltoaction").load("call-to-action.html");
    
    $("#footerdiv").load("footer.html");
    
  //  AOS.init();  
  
    AOS.init({
        duration: 1200
    });
    
});


function logOut ()
{
    window.localStorage.clear(); //clear all localstorage
//    window.localStorage.removeItem("my_item_key"); //remove one item

    window.location.href = 'index.html';
}

function firstLoad ()
{
    var firstTime = localStorage.getItem("first_time");
    if(!firstTime) 
    {
    // first time loaded!
        localStorage.setItem("first_time","1");
    }
}


async function customerLogin () 
{
    await fetch('/home/usercredentials?key=BzJKl8b4UQ76nLw&method=1002&email=gaganpreet.singh@safehouse.technology&password=111')
    .then(function(response) 
    {
        if (response.status !== 200 && response.ok!==true) 
        {
            console.log('Looks like there was a problem. Status Code: ' +   response.status);
            return;
        }

      // Examine the text in the response
        response.json().then(function(data) 
        {
            console.log(data);
            if (data.status !== '200' ) 
            {
                console.log('Looks like there was a problem. Status Code: ' +   response.status);
                return;
            }

            console.log(data.extra.personDetails.person_name);

            sessionDetails.personDetails = data.extra.personDetails;
            sessionDetails.previousOrders = data.extra.previousOrders;

            //console.log("sessionDetails", sessionDetails);

            localStorage.setItem('sessionDetails', JSON.stringify(sessionDetails)); 


            //$("#loginName").text(data.extra.personDetails.person_name);

            history.back();
        });
    })
    .catch(function(err) {
        console.log('Fetch Error :-S', err);
    });
    
}


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
    
    if(!localStorage.hasOwnProperty('sessionDetails'))
    {
        sessionDetails.sensorDescription = sensorDescription;     
        localStorage.setItem('sessionDetails', JSON.stringify(sessionDetails)); 
    }
   
    else 
   
    {
        sessionDetails = JSON.parse(localStorage.getItem('sessionDetails'));
        sessionDetails.sensorDescription = sensorDescription;            //   console.log('sessionDetails', JSON.stringify(sessionDetails));
        localStorage.setItem('sessionDetails', JSON.stringify(sessionDetails)); 
    }
   

    window.location.href = 'product-description.html';


}


function productDescription()
{
    sessionDetails = JSON.parse(localStorage.getItem('sessionDetails'));
    
    console.log("sessionDetails12", sessionDetails);
    
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


// start saving local staorage

 

/*
    const userAction = async () => 
    {
//        const response = await fetch('/home/usercredentials?key=BzJKl8b4UQ76nLw&method=1002&email=gaganpreet.singh@safehouse.technology&password=111');
//        const myJson = await response.json(); //extract JSON from the http response
//    // do something with myJson
//        console.log("myJson",myJson);
         const response = await fetch('/home/usercredentials?key=BzJKl8b4UQ76nLw&method=1002&email=gaganpreet.singh@safehouse.technology&password=11')
            .then(response => {
                console.log('success!');
                console.log(response.status, response.ok); // 404 false 
            })
            .catch(error => {
              console.log('API failure' + error);
            });
       
    
    };
    
   var request = new XMLHttpRequest();

    request.open(
            'GET', 
            '/home/usercredentials?key=BzJKl8b4UQ76nLw&method=1002&email=gaganpreet.singh@safehouse.technology&password=111', 
            true
    );
    
    request.send();

    request.onload = function () {
      
        console.log("response: ", request.status);
      
        console.log("response: ", request.response);
        
        var responseObj = JSON.parse(request.response);
        
        console.log(responseObj.extra.personDetails.person_name);
        
        console.log(responseObj);
        
        console.log("1 end");
    };
     * 
     * 
        const userAction = async () => {
        const response = await fetch('http://example.com/movies.json', {
          method: 'POST',
          body: myBody, // string or object
          headers: {
            'Content-Type': 'application/json'
          }
        });
        const myJson = await response.json(); //extract JSON from the http response
        // do something with myJson
        }

     */