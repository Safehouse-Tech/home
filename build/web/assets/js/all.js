/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global  AOS, fetch*/

var sessionDetails = new Object();

var sensorDescription = new Object();
var personDetails = new Object();
var basketSession = new Object();

$(document).ready(function () {

    $("#headerdiv").load("header.html");

    $("#calltoaction").load("call-to-action.html");

    $("#footerdiv").load("footer.html");

    //  AOS.init();  

    AOS.init({
        duration: 1200
    });

});


function logOut()
{
    window.localStorage.clear(); //clear all localstorage
//    window.localStorage.removeItem("my_item_key"); //remove one item

    window.location.href = 'index.html';
}

function firstLoad()
{
    var firstTime = localStorage.getItem("first_time");
    if (!firstTime)
    {
        // first time loaded!
        localStorage.setItem("first_time", "1");
    }
}

function showPassword(inputID)
{
    var x = document.getElementById(inputID);
    x.type === "password" ? x.type = "text" : x.type = "password";
}

async function customerLogin(email, password)
{
    console.log("email, " + email, password);
    
    await fetch('/home/usercredentials?key=BzJKl8b4UQ76nLw&method=1002&email=' + email + '&password=' + password + ' ')
    .then(function (response)
    {
        if (response.status !== 200 && response.ok !== true)
        {
            console.log('Looks like there was a problem. Status Code: ' + response.status, response.ok);
            return;
        }

        // Examine the text in the response
        response.json().then(function (data)
        {
            //console.log(data);
            if (data.status !== '200')
            {
                console.log('Looks like there was a problem. Status Code: ' + response.status);
                $(".pos-demo").notify(
                        "Invalid username or password",
                        {
                            className: "error",
                            position: "right"
                        });
                return;
            }

            sessionDetails.personDetails = data.extra.personDetails;
            sessionDetails.previousOrders = data.extra.previousOrders;
            sessionDetails.basketSession = data.extra.basketSession;

            localStorage.setItem('sessionDetails', JSON.stringify(sessionDetails));

            var lastURL = document.referrer;

            lastURL.includes("product-description.html") ? window.location.href = 'shop.html' : window.location.href = 'index.html';// history.back();

        });
    })
    .catch(function (err) {
        console.log('Fetch Error :-S', err);
    });

}

/*************************************  Product Description Functions   *****************************************************/


function shopProduct(product_id, product_name, price, image_path, description_path, technology, extra)
{
    sensorDescription.product_id = product_id;
    sensorDescription.product_name = product_name;
    sensorDescription.price = price;
    sensorDescription.image_path = image_path;
    sensorDescription.description_path = description_path;
    sensorDescription.technology = technology;
    sensorDescription.extra = extra;

    if (!localStorage.hasOwnProperty('sessionDetails')){
        sessionDetails.sensorDescription = sensorDescription;
        localStorage.setItem('sessionDetails', JSON.stringify(sessionDetails));
    } 
    else{
        sessionDetails = JSON.parse(localStorage.getItem('sessionDetails'));
        sessionDetails.sensorDescription = sensorDescription;            //   console.log('sessionDetails', JSON.stringify(sessionDetails));
        localStorage.setItem('sessionDetails', JSON.stringify(sessionDetails));
    }
    window.location.href = 'product-description.html';
}


function productDescription()
{
    sessionDetails = JSON.parse(localStorage.getItem('sessionDetails'));

//    console.log("sessionDetails12", sessionDetails);

    sensorDescription = sessionDetails.sensorDescription;

    $("#product_id").text(sensorDescription.product_id);
    $("#product_name").text(sensorDescription.product_name);
    $("#product_price").text(sensorDescription.price);

    $("#product_technology").text(sensorDescription.technology);

    $("#product_extra").text(sensorDescription.extra);


    var allimages = sensorDescription.image_path + '';

    var images = allimages.split(",");

    $("#image-slider").empty();

    for (var i = 0; i < images.length; i++)
    {
        $("#image-slider").append('<div class="swiper-slide">\n\
                                        <img class="descriptionimageheight " src="' + images[i] + '" alt="">\n\
                                    </div>');
    }


    for (i = 1; i <= 100; i++)
    {
        $("#select_quantity").append($('<option></option>').val(i).html(i));
    }

    var path = "product-description/" + sensorDescription.description_path;
    $("#product_description_div").load(path);

}

async function addtobasket()
{
    if (!localStorage.hasOwnProperty('sessionDetails'))
    {
        $("#staticBackdrop").modal('show');
        $("#staticBackdrop_body").load("login.html?nav=shop");
    } else
    {
        sessionDetails = JSON.parse(localStorage.getItem('sessionDetails'));

        if (!sessionDetails.hasOwnProperty('personDetails'))
        {
            $("#staticBackdrop").modal('show');
            $("#staticBackdrop_body").load("login.html?nav=shop");
        } else
        {
            console.log("adding product to cart session");

            var product_id = $("#product_id").text();
            var product_name = $("#product_name").text();
            var product_quantity = parseInt($('#select_quantity').find(":selected").text());
            var price_id = $("#price_id").text();
            var price = parseFloat(($("#product_price").text()).toString().replace(/^\D+/g, ''));
            var total_price = price * product_quantity;

            var productObject = {
                product_name: product_name,
                quantity: product_quantity,
                price_id: price_id,
                price: price,
                total_price: total_price
            };

            basketSession = sessionDetails.basketSession;
            if (!basketSession.hasOwnProperty('basketItems')){
                var basketItems = {};
                basketItems[product_id] = productObject;
                basketSession.basketItems = basketItems;
            } 
            else {
                basketSession.basketItems[product_id] = productObject;
            }

            if (!basketSession.hasOwnProperty('totalItems'))
            {
                basketSession.totalItems = product_quantity;
            } else {
                
                var totalItems = 0 ;
                // each object quantity calculate
                Object.keys(basketSession.basketItems).forEach(function(key) {

                    //console.log(key, basketSession.basketItems[key].quantity);
                    totalItems = totalItems +  basketSession.basketItems[key].quantity ;

                });
                
                basketSession.totalItems = totalItems ;
            }

         
            var person_id = sessionDetails.personDetails.person_id;
            var basket_id = basketSession.basketId;
            var basketItems = JSON.stringify(basketSession.basketItems);
            
            // updatebasket(person_id, basket_id, basketItems)
         ///*   
            await fetch('/home/basketSession?key=BzJKl8b4UQ76nLw&method=3002&person_id=' +person_id+ '&basket_id=' +basket_id+ '&basketItems=' +basketItems , {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(function (response)
            {
                if (response.status !== 200 && response.ok !== true)
                {
                    console.log('Looks like there was a problem. Status Code: ' + response.status, response.ok);
                    return;
                }

                response.json().then(function (data)
                {
                    console.log("data", data);
                    var notifyText, notifyStatus ;
                    
                    if(data.extra === "basket items updated")
                    {
                        notifyText = "Item added to basket" ;
                        notifyStatus = "success";

                        $("#basketalert_num").text(basketSession.totalItems);

                        sessionDetails.basketSession = basketSession;

                        console.log("sessionDetails", sessionDetails);

                        localStorage.setItem('sessionDetails', JSON.stringify(sessionDetails));
                    }

                    else {
                        notifyText = "Item not added to basket" ;
                        notifyStatus = "error";
                    }

                    $(".pos-demo").notify(
                        notifyText,
                        {
                            className: notifyStatus,
                            position: "right"
                        });
            
                    
                });
            })
            .catch(function (err) {
                console.log('Fetch Error :-S', err);
            });
         //*/   
            
           
        }
    }

}

/*
 * 
 *const secondFunction = async () => {
  const result = await firstFunction()
  // do something else here after firstFunction completes
}
 */

 /*
async function updatebasket(person_id, basket_id, basketItems)
{
    var result;
    await fetch('/home/basketSession?key=BzJKl8b4UQ76nLw&method=3002&person_id=' +person_id+ '&basket_id=' +basket_id+ '&basketItems=' +basketItems , {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(function (response)
    {
        if (response.status !== 200 && response.ok !== true)
        {
            console.log('Looks like there was a problem. Status Code: ' + response.status, response.ok);
            return;
        }

        response.json().then(function (data)
        {
            console.log("data", data);
            result = data.extra;
            console.log("result: "+ result);
            
        });
    })
    .catch(function (err) {
        console.log('Fetch Error :-S', err);
    });
    
    return result;
}

  */

/*************************************  Basket Page Representation Functions   *****************************************************/

function loadBasketItems()
{
    // load at basket page

    // check best representation with table or cards
}



/*************************************      Person Profile Functions   *****************************************************/

function load_allAdress()
{
    $.getJSON("assets/js/name_address.json", function (json) {
        // console.log(json); // this will show the info it in firebug console

        for (var i = 0; i < json.length; i++)
        {
            var name = json[i].name;
            var address = json[i].property_address;

            var icon = (json[i].property_type === "building") ? "fa-building" : "fa-home";

            $("#existingAddress").append(
                    '<div class="card cardindexpage my-4">\n\
                    <div class="row g-0">\n\
                        <div class="col-md-1 d-flex align-items-center justify-content-center">\n\
                            <i class="fas ' + icon + '  fa-4x text-warning"></i>\n\
                        </div> \n\
                        <div class="col-md-10 "> \n\
                            <div class="card-body ">\n\
                                <h5 class="card-title">' + name + '</h5>\n\
                                <h6 class="text-muted">' + address + '</h6>\n\
                            </div>\n\
                        </div>\n\
                        <div class="col-md-1"> \n\
                            <button type="button" class="remove btn btn-link btn-sm text-danger float-end">Remove</button>\n\
                        </div>\n\
                    </div>\n\
                </div>');
        }

        $(".remove").click(function () {
            $(this).parents(".card").remove();

            $('#existingAddress').is(':empty') ?
                    $('#existingAddress').append(
                    '<h4 class="text-muted text-center">No Installation Address</h4>'
                    )
                    : "";
        });
    });
}


function add_newAddress()
{
    //  $("#newAddress").modal('show');


    if ($("#existingAddress").children('h4').length > 0) {

        $("#existingAddress").empty();
    }


    $("#existingAddress").prepend(
            '<div class="card cardindexpage my-4">\n\
            <div class="row g-0">\n\
                <div class="col-md-1 d-flex align-items-center justify-content-center">\n\
                    <i class="fas fa-house  fa-4x text-warning"></i>\n\
                </div> \n\
                <div class="col-md-10 "> \n\
                    <div class="card-body ">\n\
                        <h5 class="card-title">New Name</h5>\n\
                        <h6 class="text-muted">New Address</h6>\n\
                    </div>\n\
                </div>\n\
                <div class="col-md-1"> \n\
                    <button type="button" class="remove btn btn-link btn-sm text-danger float-end">Remove</button>\n\
                </div>\n\
            </div>\n\
        </div>');

    $(".remove").click(function () {
        $(this).parents(".card").remove();

        $('#existingAddress').is(':empty') ?
                $('#existingAddress').append(
                '<h4 class="text-muted text-center">No Installation Address</h4>'
                )
                : "";
    });

}

//function signupCustomer()
async function signupCustomer()
{
    var fullname = $('#register_fullname').val();
    var email = $('#register_email').val();
    var password = $('#register_password').val();

    var newCustomer = 'fullname=' + fullname + '&email=' + email + '&password' + password;

    await fetch('/home/usercredentials?key=BzJKl8b4UQ76nLw&method=1001&' + newCustomer, {
        method: 'POST',
//        body: JSON.stringify(newCustomer), // string or object
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(function (response)
    {
        if (response.status !== 200 && response.ok !== true)
        {
            console.log('Looks like there was a problem. Status Code: ' + response.status, response.ok);
            return;
        }

        // Examine the text in the response
        response.json().then(function (data)
        {
            console.log("data", data);
            customerLogin(email, password);
        });
    })
    .catch(function (err) {
        console.log('Fetch Error :-S', err);
    });


}