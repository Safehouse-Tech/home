/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$(document).ready( function () 
{
    addTablerow();
  
    $('#example').on('click', '.remove', function () 
    {
        var table = $('#example').DataTable();
            table
            .row($(this).parents('tr'))
            .remove()
            .draw();
    });
  /*  
    $('#example').on('input', '.form-control', function() 
    {

        var prevtotalItems = $('#totalItems').text();
        
        var table = $('#example').DataTable();
        
        var index = table.cell( this.parentNode ).index();
        var total_items = 0;

        $( table.column( index.column ).nodes() ).find( 'input' ).each( function () 
        {
            total_items += this.value.replace( ',', '' ) * 1;
            
        });
        
//        console.log("index: "+JSON.stringify(index ));  console.log("index: "+index.row);
        
        var difffItems = ( parseInt(total_items) - parseInt(prevtotalItems) ) ;     // console.log("difffItems: "+ difffItems);
        
        var difffItems = difffItems.toString().replace(/[^\w\s]/gi, '');
        
        var price = parseInt($('td:eq(4)', index.row).text());
                    
        var newPrice = '£ ' + parseFloat(difffItems) * parseFloat(price);
        
        console.log("newPrice: "+ newPrice);
        
        $('td:eq(3)', index.row).text(newPrice);

         
        $('#totalItems').text(total_items);
     });
    */
    // on value change also change the total amount for that product
    
} );


function onlyNumberKey(evt) 
{    
    // Only ASCII character in that range allowed
    var ASCIICode = (evt.which) ? evt.which : evt.keyCode
    if (ASCIICode > 31 && (ASCIICode < 48 || ASCIICode > 57))
        return false;
    return true;
}

function addTablerow()
{
    
    var tablerowdata = new Array();
    
    
    
    for (var i=0; i<4;i++)
    {
        var rowdata = new Array();
        
        rowdata.push("image "+ (i+1));
        rowdata.push("name "+ (i+1));
        rowdata.push('<input class="form-control" type="text" onkeypress="return onlyNumberKey(event)"  min="1" value="'+(i+2)+'">');
        rowdata.push('£ '+ (i+50));
        
        rowdata.push(i+1);
        rowdata.push('<button class="btn btn-link btn-sm remove" type="button"><i class="fas fa-times text-danger fa-lg" style="cursor:pointer"></i></button>');
    
        /*dt.row.add([
            "image "+i+1,
            "name "+i+1,
            "quantity "+i+1,
             i+1,
            '<button class="btn btn-link btn-sm remove" type="button"><i class="fas fa-times text-danger fa-lg" style="cursor:pointer"></i></button>'
//            "<button onclick=\"(function(){open_properties_profile(\'"+client_id+"','"+property_type+"','"+property_addr+"\')})()\" \n\
//                class='btn btn-link btn-sm' type='button'>View</button>"

        ]).draw(false);
        
        */
       
       tablerowdata.push(rowdata);
    }
    
    
    
    $('#example').DataTable({
        data: tablerowdata,
        "searching": false,
        "paging":   false,
        "bInfo" : false,
        "bSort" : false,
        "responsive": true,
        "language": {
            "emptyTable": "Basket is empty, please proceed to shop"
        },
        "footerCallback": function (row, data, start, end, display) 
        {                
    //Get data here 
//                console.log(data);
                //Do whatever you want. Example:
                var totalAmount = 0;

                for (var i = 0; i < data.length; i++) 
                {
                    var price = data[i][3].split('£ ');
                    
                    totalAmount += parseFloat(price[1]);

                }
                $('#priceamt').text(totalAmount);         
       },
       "rowCallback": function (row, data) 
       {
            var element = $(row).find('.form-control');
            element.on("input", function () 
            {
                
                var prevtotalItems = $('#totalItems').text();
                
                var table = $('#example').DataTable();
        
                var index = table.cell( this.parentNode ).index();
                var total_items = 0;

                $( table.column( index.column ).nodes() ).find( 'input' ).each( function () 
                {
                    total_items += this.value.replace( ',', '' ) * 1;

                });
                
                $('#totalItems').text(total_items);
                
                /*
                var difffItems = ( parseInt(total_items) - parseInt(prevtotalItems) ) ;     // console.log("difffItems: "+ difffItems);
        
                var difffItems = difffItems.toString().replace(/[^\w\s]/gi, '');

                var price = parseInt($('td:eq(4)', index.row).text());

                var newPrice = '£ ' + parseFloat(difffItems) * parseFloat(price);

                console.log("newPrice: "+ newPrice);

                $('td:eq(3)', index.row).text(newPrice);
                
                var totalAmount = 0;

                console.log("wecv: "+data[3]);
 */
            });
                
                
                
        } 
    });
    
    var total_items = 0;  
    $.each( $('#example').DataTable().column(2).data(), function(i, cell)
    {
        var cellObject = $.parseHTML(cell);
        total_items += parseInt(cellObject[0].value);
    });
    
    $('#totalItems').text(total_items);
    
   


}

/*
 
 var table = $('#table_name').DataTable({
  destroy: true,
  "Processing": true,
  "ajax": {
    "url": url,
    "dataSrc": "",
    "type": "POST",
    "data": {
      "action": "my_action",
      "table_name": 'my_table',
      "permission": permission
    },
  },
  "deferRender": true,
  "columns": [{
      "data": null
    },
    {
      "data": "column1"
    },
    {
      "data": "column2"
    },
    {
      "data": "column3"
    },
    {
      "data": "column4"
    },
    {
      "data": "column5"
    },
    {
      "data": "column6"
    },
    {
      "data": null
    },

  ],
  columnDefs: [{
      "searchable": false,
      "orderable": false,
      "targets": 0
    },
    {
      targets: [5],
      render: function(data, type, row) {
        if (row.column5 == 0) return 'something';
        else if (row.column5 == 1) return 'something else';
      }
    },
    {
      targets: [6],
      render: function(data, type, row) {
        if (row.column6 == 0) return 'Passiv';
        else if (row.column6 == 1) return 'Aktiv';
      }
    },
    {
      targets: [7],
      render: function(data, type, row) {
        if (permission == 1 || permission == 2) {
          var button_start = "<div class=\"btn-group dropup\" width=\"100%\" align=\"center\"><button type=\"button\" class=\"btn btn-default dropdown-toggle btn-for-action\" data-toggle=\"dropdown\" aria-expanded=\"false\"><span class=\"caret\"></span><span class=\"sr-only\">Toggle Dropdown</span></button><ul class=\"dropdown-menu \"  role=\"menu\"><li><a href='?action=my_action&id=" + row.id + "' target='_blank'>More</a></li>";

          if (row.column4 == 0 && permission == 2 && row.column6 == 1) {

            var button_mid = button_start.concat("<li><a href='#' onclick='my_function(" + row.id + ")'>Addon</a></li>");
            var button_end = button_mid.concat("<ul><div>");

          } else {
            var button_end = button_start.concat("<ul><div>");
          }

          return button_end;
        }
      }
    },
  ],
  "order": [
    [1, "desc"]
  ]

});

table.on('order.dt search.dt', function() {
  table.column(0, {
    search: 'applied',
    order: 'applied'
  }).nodes().each(function(cell, i) {
    console.log(cell);
    cell.innerHTML = i + 1;
  });
}).draw();

 
 
 */