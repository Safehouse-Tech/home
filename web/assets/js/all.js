/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global  AOS, fetch*/

var sessionDetails = new Object();

var sensorDescription = new Object();
var personDetails = new Array();
var basketSession = new Object();

var basketItems = new Object();

$(document).ready(function () {

    $("#headerdiv").load("header.html");

    $("#calltoaction").load("call-to-action.html");

    $("#footerdiv").load("footer.html");

    //  AOS.init();  

    AOS.init({
        duration: 1200
    });

});

$(function () {
    $('[data-bs-toggle="tooltip"]').tooltip()
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
    $('#overlay1').show();
    await fetch('/home/usercredentials?key=BzJKl8b4UQ76nLw&method=1002&email=' + email + '&password=' + password + ' ')
            .then(function (response)
            {
                if (response.status !== 200 && response.ok !== true)
                {
                    console.log('Looks like there was a problem. Status Code: ' + response.status, response.ok);

                    $('#overlay1').hide();
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

                    if (localStorage.hasOwnProperty('sessionDetails'))
                    {
                        sessionDetails = JSON.parse(localStorage.getItem('sessionDetails'));
                        console.log("sessionDetails12", sessionDetails);
                    }

                    sessionDetails.personDetails = data.extra.personDetails;
                    sessionDetails.previousOrders = data.extra.previousOrders;
                    sessionDetails.basketSession = data.extra.basketSession;

                    localStorage.setItem('sessionDetails', JSON.stringify(sessionDetails));

                    $('#overlay1').hide();
                    //var lastURL = document.referrer;  // console.log("lastURL", lastURL);
                    var lastURL = window.location.href;
//                    lastURL.includes("product-description.html") ? window.location.href = 'shop.html' : window.location.href = 'index.html';// history.back();

                    window.location.href = lastURL.includes("product-description.html") ?
                            'product-description.html' : 'index.html';// history.back();

                });
            })
            .catch(function (err) {
                console.log('Fetch Error :-S', err);
                $('#overlay1').hide();
            });

}

/*************************************  Product Description Functions   *****************************************************/


function shopProduct(product_id, product_name, price, image_path, description_path, technology, extra, price_id)
{
    sensorDescription.product_id = product_id;
    sensorDescription.product_name = product_name;
    sensorDescription.price = price;
    sensorDescription.image_path = image_path;
    sensorDescription.description_path = description_path;
    sensorDescription.technology = technology;
    sensorDescription.extra = extra;
    sensorDescription.price_id = price_id;

    if (!localStorage.hasOwnProperty('sessionDetails')) {
        sessionDetails.sensorDescription = sensorDescription;
        localStorage.setItem('sessionDetails', JSON.stringify(sessionDetails));
    } else {
        sessionDetails = JSON.parse(localStorage.getItem('sessionDetails'));
        sessionDetails.sensorDescription = sensorDescription;            //   console.log('sessionDetails', JSON.stringify(sessionDetails));
        localStorage.setItem('sessionDetails', JSON.stringify(sessionDetails));
    }
    window.location.href = 'product-description.html';
}


function productDescription()
{
    sessionDetails = JSON.parse(localStorage.getItem('sessionDetails'));

    console.log("sessionDetails123", sessionDetails);

    sensorDescription = sessionDetails.sensorDescription;

    $("#product_id").text(sensorDescription.product_id);
    $("#product_name").text(sensorDescription.product_name);
    $("#product_price").text(sensorDescription.price);
    $("#product_technology").text(sensorDescription.technology);
    $("#product_extra").text(sensorDescription.extra);

    $("#price_id").text(sensorDescription.price_id);


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
    // need to add total items to database

    $('#overlay1').show();

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
            var total_price = Number.parseFloat(price * product_quantity).toFixed(2);

            var allimages = sessionDetails.sensorDescription.image_path + '';
            var images = allimages.split(",");

            var product_image = images[0];

            var productObject = {
                product_image: product_image,
                product_name: product_name,
                quantity: product_quantity,
                price_id: price_id,
                price: price,
                total_price: total_price
            };

            basketSession = sessionDetails.basketSession;
            if (!basketSession.hasOwnProperty('basketItems')) {
                var basketItems = {};
                basketItems[product_id] = productObject;
                basketSession.basketItems = basketItems;
            } else {
                basketSession.basketItems[product_id] = productObject;
            }

            if (!basketSession.hasOwnProperty('totalItems'))
            {
                basketSession.totalItems = product_quantity;
            } else
            {
                var totalItems = 0;
                // each object quantity calculate
                Object.keys(basketSession.basketItems).forEach(function (key) {
                    totalItems = totalItems + basketSession.basketItems[key].quantity;   //console.log(key, basketSession.basketItems[key].quantity);
                });

                basketSession.totalItems = totalItems;
            }

// if we pass basketSession i.e global object to function value will be the one with the change.

            $('#overlay1').hide();

            updatebasket();

            /* 
             var person_id = sessionDetails.personDetails.person_id;
             var basket_id = basketSession.basketId;
             var basketItems = JSON.stringify(basketSession.basketItems);
             
             // updatebasket(person_id, basket_id, basketItems)
             
             await fetch('/home/basketSession?key=BzJKl8b4UQ76nLw&method=3002&person_id=' + person_id + '&basket_id=' + basket_id + '&basketItems=' + basketItems, {
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
             var notifyText, notifyStatus;
             
             
             if (data.extra === "basket items updated")
             {
             notifyText = "Item added to basket";
             notifyStatus = "success";
             
             $("#basketalert_num").text(basketSession.totalItems);
             
             sessionDetails.basketSession = basketSession;
             
             console.log("sessionDetails", sessionDetails);
             
             localStorage.setItem('sessionDetails', JSON.stringify(sessionDetails));
             
             } else {
             notifyText = "Item not added to basket";
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
             */


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


async function updatebasket(removeProduct_id)   //(person_id, basket_id, basketItems)
{
    $('#overlay1').show();

    sessionDetails = JSON.parse(localStorage.getItem('sessionDetails'));

    var person_id = sessionDetails.personDetails.person_id;
    var basket_id = basketSession.basketId;

    if (typeof removeProduct_id !== 'undefined')
    {
        //var totalItems = 0;
        basketSession.totalItems = basketSession.totalItems - basketSession.basketItems[removeProduct_id].quantity;
        delete basketSession.basketItems[removeProduct_id];

        // console.log("basketSession", basketSession);
    }

    var totalItems = basketSession.totalItems;
    var basketItems = JSON.stringify(basketSession.basketItems);        //console.log("basketItems", basketItems);

    var result;
    await fetch('/home/basketsession?key=BzJKl8b4UQ76nLw&method=3002&person_id=' + person_id + '&basket_id=' +
            basket_id + '&basketItems=' + basketItems + '&totalItems=' + totalItems, {
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

                    $('#overlay1').hide();
                    return;
                }

                response.json().then(function (data)
                {
                    result = data.extra;        //console.log("result: " + result);

                    var notifyText = "Item not added to basket",
                            notifyStatus = "error",
                            notifyPosition = "right";

                    if (data.extra === "basket items updated")
                    {
                        if (window.location.href.includes("basket.html"))
                        {
                            notifyText = "Item removed from basket";
                            notifyStatus = "info";
                            notifyPosition = "top center";

                        } else {
                            notifyText = "Item added to basket";
                            notifyStatus = "success";
                        }
                    }

                    $(".pos-demo").notify(
                            notifyText,
                            {
                                className: notifyStatus,
                                position: notifyPosition
                            });

                    $("#basketalert_num").text(basketSession.totalItems);

                    sessionDetails.basketSession = basketSession;
//                    console.log("sessionDetails", sessionDetails);

                    localStorage.setItem('sessionDetails', JSON.stringify(sessionDetails));

                    $('#overlay1').hide();
                });
            })
            .catch(function (err) {
                console.log('Fetch Error :-S', err);
                $('#overlay1').hide();
            });



}



/*************************************  Basket Page Representation Functions   *****************************************************/



function loadBasketTable()
{
    sessionDetails = JSON.parse(localStorage.getItem('sessionDetails'));

    basketSession = sessionDetails.basketSession;   //    basketItems = sessionDetails.basketSession.basketItems;

    var tablerowdata = new Array();

    Object.keys(basketSession.basketItems).forEach(function (key) {

//        console.log(key, basketSession.basketItems[key].quantity);

        var rowdata = new Array();
        rowdata.push('<img src="' + basketSession.basketItems[key].product_image + '" class="img-fluid " alt="">');
        rowdata.push(key);
        rowdata.push(basketSession.basketItems[key].product_name);
        rowdata.push(basketSession.basketItems[key].quantity);
        rowdata.push('£ ' + basketSession.basketItems[key].total_price);
        rowdata.push('<button class="btn btn-link btn-sm remove" type="button"><i class="fas fa-times text-danger fa-lg" style="cursor:pointer"></i></button>');

        tablerowdata.push(rowdata);
    });


    $('#basketTable').DataTable({
        data: tablerowdata,
        "searching": false,
        "paging": false,
        "bInfo": false,
        "bSort": false,
        "responsive": true,
        "language": {
            "emptyTable": "Basket is empty, please proceed to shop"
        },
        'columns': [null, {'visible': false}, null, null, null, null],
        "columnDefs": [
            {"className": "dt-center", "targets": "_all"}
        ],
        "footerCallback": function (row, data, start, end, display)
        {
//                console.log(data);
            var totalAmount = 0;
            var totatItems = 0;
            var final_price = 0;

            for (var i = 0; i < data.length; i++)
            {
                totatItems += data[i][3];

                var price = data[i][4].split('£ ');
                totalAmount += parseFloat(price[1]);

            }
            $('#total_itemsLeftCard').text(totatItems);
            $('#total_itemsRightCard').text(totatItems);

            totalAmount = Number.parseFloat(totalAmount).toFixed(2);

            $('#priceamt').text(totalAmount);

            var shippingCharges = $('#shippingcharges').find(":selected").text().replace(/^\D+/g, '');

            final_price = Number(shippingCharges) + Number(totalAmount);

            final_price = Number.parseFloat(final_price).toFixed(2);

            $('#finalpriceamt').text(final_price);

        },
        "rowCallback": function (row, data)
        {


        }
    });

    //sessionDetails.basketSession.totalItems === 0 ? $('#checkoutButton').prop('disabled', true) : $('#checkoutButton').prop('disabled', false);

    sessionDetails.basketSession.totalItems === 0 ? $('#checkoutDiv').hide() : $('#checkoutDiv').show();

}


async function basketCheckout()
{
    $('#overlay1').show();

    sessionDetails = JSON.parse(localStorage.getItem('sessionDetails'));

    var person_id = sessionDetails.personDetails.person_id;
    var shippingChargesCode = $("#shippingcharges option:selected").val();        //console.log("shippingChargesCode", shippingChargesCode);

    basketItems = JSON.stringify(sessionDetails.basketSession.basketItems);

    await fetch('/home/basketcheckout?key=BzJKl8b4UQ76nLw&method=1001&person_id=' + person_id + '&basketItems=' + basketItems + '&shippingCode=' + shippingChargesCode)
            .then(function (response)
            {
                if (response.status !== 200 && response.ok !== true)
                {
                    console.log('Looks like there was a problem. Status Code: ' + response.status, response.ok);

                    $('#overlay1').hide();
                    return;
                }

                // Examine the text in the response
                response.json().then(function (data)
                {
                    console.log("data", data);

                    var checkoutSession = new Object();

                    checkoutSession = data.extra;

                    sessionDetails.checkoutSession = checkoutSession;
                    //console.log("sessionDetails", sessionDetails);

                    localStorage.setItem('sessionDetails', JSON.stringify(sessionDetails));

                    sessionDetails = JSON.parse(localStorage.getItem('sessionDetails'));

                    $('#overlay1').hide();

                    window.location.href = data.extra.url;


                });
            })
            .catch(function (err) {
                console.log('Fetch Error :-S', err);
                $('#overlay1').hide();
            });
}

/*************************************  Order Confirmation Functions   *****************************************************/

async function updateOrderDetails()
{
    $('#overlay1').show();

    sessionDetails = JSON.parse(localStorage.getItem('sessionDetails'));
    var person_id = sessionDetails.personDetails.person_id;

    var basket_id = sessionDetails.basketSession.basketId;
    // show swal to say payment confirmation 

    var payment_intent = sessionDetails.checkoutSession.payment_intent;
    var checkoutSession_id = sessionDetails.checkoutSession.id;

    await fetch('/home/basketcheckout?key=BzJKl8b4UQ76nLw&method=3002&person_id=' + person_id + '&basket_id=' + basket_id + '&payment_intent=' + payment_intent + '&checkoutSession_id=' + checkoutSession_id, {
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

                    $('#overlay1').hide();
                    return;
                }

                // Examine the text in the response
                response.json().then(function (data)
                {
                    //console.log("data", data);

                    delete sessionDetails.checkoutSession;
                    sessionDetails.basketSession = data.extra.basketSession;
                    sessionDetails.previousOrders = data.extra.previousOrders;

                    localStorage.setItem('sessionDetails', JSON.stringify(sessionDetails));

                    $('#overlay1').hide();

                    let timerInterval;
                    Swal.fire({
                        icon: 'success',
                        title: 'Payment Success!',
                        html: 'Payment receipt will be sent to your email address. <br> \n\
                                Redirecting to your orders page.',
                        timer: 3000,
                        timerProgressBar: true,
                        didOpen: () => {
                            Swal.showLoading();
                            const b = Swal.getHtmlContainer().querySelector('b');
                            timerInterval = setInterval(() => {
                                b.textContent = Swal.getTimerLeft();
                            }, 100);
                        },
                        willClose: () => {
                            clearInterval(timerInterval);
                        }
                    }).then((result) => {
                        if (result.dismiss === Swal.DismissReason.timer) {
                            window.location.href = 'orders.html';
                        }
                    });

                });
            })
            .catch(function (err) {
                console.log('Fetch Error :-S', err);
                $('#overlay1').hide();

                alert("Backend update issue");
            });



}

/*************************************      Load Previous Orders Functions   *****************************************************/

function retrieveAllOrders()
{
    sessionDetails = JSON.parse(localStorage.getItem('sessionDetails'));
    previousOrders = sessionDetails.previousOrders;


    var tablerowdata = new Array();

    previousOrders.forEach((order) => 
    {
        var orderPlacedAt = new Date(order.order_timestamp*1000).toString().substring(0, 31);
        var amount = order.order_amount.substring(0,order.order_amount.length-2)+"."+order.order_amount.substring(order.order_amount.length-2);
        var orderAmount =  amount +" "+ order.payment_currency.toUpperCase();
        var dispatchTo = JSON.parse(order.shipping_address).name;
        
        var rowdata = new Array();
        rowdata.push(orderPlacedAt);
        rowdata.push(order.order_id);
        rowdata.push(order.total_items);
        rowdata.push(orderAmount);
        
        rowdata.push(dispatchTo);
        rowdata.push(order.order_status);
        rowdata.push('<a id="" type="button" class="btn btn-warning btn-sm" href="'+order.order_receipt+'">\n\
                        <i class="fas fa-receipt"></i> Receipt</a>');
        
        
        rowdata.push('<button class="btn btn-primary btn-sm details" type="button">\n\
                        <i class="far fa-folder-open fa-lg" style="cursor:pointer"></i> View details</button>');

        tablerowdata.push(rowdata);
        
    });
    
    
    $('#allOrders').DataTable({
        data: tablerowdata,
        "order": [[ 0, "desc" ]],
        "responsive": true,
        "language": {
            "emptyTable": "No Order Placed"
        },
//        'columns': [null, {'visible': false}, null, null, null, null],
        "columnDefs": [
            {"className": "dt-center", "targets": "_all"}
        ],
        "footerCallback": function (row, data, start, end, display)
        {
//                console.log(data);
            

        },
        "rowCallback": function (row, data)
        {


        }
    });
 
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