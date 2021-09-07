/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$(document).ready(function(){
    
   // console.log("product_values.image_path: "+ product_values.image_path);
    
    $("#signInform").submit(function()
    {
        checkSignin();
        
        return false;
    });
    
    $("#signupForm").submit(function()
    {
        createNewaccount();
        
        return false;
    });
    
    
//    $("#signupNewaccount").hide();
});

function checkSignin()
{
    var signinEmail = $("#signinEmail").val();
    var signinPassword = $("#signinPassword").val();
    
    
    if(signinEmail==="gaganpreet.singh@safehouse.technology" && signinPassword==="111")
    {
        console.log("success");
        
        
        
        $("#askCustomerlogin").empty();
        
        $("#askCustomerlogin").append("<h5 class='text-success'>Welcome Back Gagan !!!</h5>");
        
        $("#billingName").val('Gaganpreet Singh');
        $("#billingEmail").val('gaganpreet.singh@safehouse.technology');
        $("#billingPhnumber").val('07440733880');
        $("#billingCountry").val('United Kingdom');
        $("#billingAddress1").val('Ftat 11, Ravenscourt');
        $("#billingAddress2").val('108-114 Richmond Road');
        $("#billingAddress3").val('');
        $("#billingCity").val('Cardiff');
        $("#billingCounty").val('Cardiff');
        $("#billingPostcode").val('CF24 3BW');
        
        
        $("#signupNewaccount").hide();
        
    }
    else 
    {
        $("#signupNewaccount").show();
        swal({
            
            html: "Invalid Username or password <br> Please try again or create a new account",
            type: "info",
            showCancelButton: true,
            cancelButtonText: "Dismiss",
            showConfirmButton: false
        });
    }
    
}

function createNewaccount()
{
    swal({ 
        html: "New account created <br> Please proceed with your order",
        type: "success",
        showCancelButton: true,
        cancelButtonText: "Dismiss",
        showConfirmButton: false
    });
}

function buyNow()
{
    let timerInterval
    Swal.fire({
//      title: 'Redirecting!',
      html: '<div class="p-4"><h4 class="text-success">Redirecting !!!</h4>\n\
                <hr>\n\
            You are being redirecting to our payment page. <br>please dont close the window </div>',
      timer: 7000,
      timerProgressBar: true,
      didOpen: () => {
        Swal.showLoading()
        const b = Swal.getHtmlContainer().querySelector('b')
        timerInterval = setInterval(() => {
          b.textContent = Swal.getTimerLeft()
        }, 100)
      },
      willClose: () => {
        clearInterval(timerInterval)
      }
    }).then((result) => {
      /* Read more about handling dismissals below */
      if (result.isConfirmed) {
    Swal.fire(
      'Deleted!',
      'Your file has been deleted.',
      'success'
    )
  }
    })
    
    
}