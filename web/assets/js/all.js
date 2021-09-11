/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global  AOS, fetch*/

var sessionDetails = new Object();

var sensorDescription = new Object();
var personDetails = new Object();

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


async function customerLogin()
{
    await fetch('/home/usercredentials?key=BzJKl8b4UQ76nLw&method=1002&email=gaganpreet.singh@safehouse.technology&password=111')
            .then(function (response)
            {
                if (response.status !== 200 && response.ok !== true)
                {
                    console.log('Looks like there was a problem. Status Code: ' + response.status);
                    return;
                }

                // Examine the text in the response
                response.json().then(function (data)
                {
                    console.log(data);
                    if (data.status !== '200')
                    {
                        console.log('Looks like there was a problem. Status Code: ' + response.status);
                        return;
                    }

                    console.log(data.extra.personDetails.person_name);

                    sessionDetails.personDetails = data.extra.personDetails;
                    sessionDetails.previousOrders = data.extra.previousOrders;

                    localStorage.setItem('sessionDetails', JSON.stringify(sessionDetails));

                    var lastURL = document.referrer;

                    lastURL.includes("product-description.html") ? window.location.href = 'shop.html' : history.back();

                });
            })
            .catch(function (err) {
                console.log('Fetch Error :-S', err);
            });

}


function shopProduct(product_id, product_name, price, image_path, description_path, technology, extra)
{
    sensorDescription.product_id = product_id;
    sensorDescription.product_name = product_name;
    sensorDescription.price = price;
    sensorDescription.image_path = image_path;
    sensorDescription.description_path = description_path;
    sensorDescription.technology = technology;
    sensorDescription.extra = extra;

    if (!localStorage.hasOwnProperty('sessionDetails'))
    {
        sessionDetails.sensorDescription = sensorDescription;
        localStorage.setItem('sessionDetails', JSON.stringify(sessionDetails));
    } else

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

function addtobasket()
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
        } else {
            console.log("adding product to cart session");

            $(".pos-demo").notify(
                    "Item added to basket ",
                    {
                        className: "success",
                        position: "right"
                    });
        }
    }

}

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


    if($("#existingAddress").children('h4').length > 0){
        
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
