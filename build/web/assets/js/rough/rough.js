/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */





for (var key in obj)
{
  var value = obj[key];
  console.log("<br> - " + key + ": " + value);
}

// start saving local staorage

 
function onlyNumberKey(evt) 
{    
    // Only ASCII character in that range allowed
    var ASCIICode = (evt.which) ? evt.which : evt.keyCode
    if (ASCIICode > 31 && (ASCIICode < 48 || ASCIICode > 57))
        return false;
    return true;
}


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

     