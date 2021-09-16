/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



$(document).ready(function(){
    
    //alert('dd');
    //$("#loadMore").hide();
    
    //shop-product-description.html

    
});


function product(product_id, product_name, price, image_path, description_path, technology, extra)
{
    console.log("product_id: "+ product_id);
    
    console.log("image_path: "+ image_path);
    
    var products_values = {"product_id": product_id, "product_name": product_name, "price": price, 'image_path': image_path,
                            "description_path": description_path, "technology": technology, "extra": extra}; 
    
    localStorage.setItem('products_values', JSON.stringify(products_values)); 


//    var url = "shop-product-description.html?description="+ encodeURIComponent(product_id+","+product_name);
//    window.open(url);
    
    window.location.href = 'product-description.html';
    
  //  window.open('shop-product-description.html');
    
}