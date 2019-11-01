/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/* global dataForm */

var Project1 = ( function() {

    return {

        init: function() {
            
            $("#version").html( "jQuery Version: " + $().jquery );

        },
        
        submitSessionForm: function() {

            var sessionid = $("#sessionid").val();
   
            $.ajax({

                url: 'registration',
                method: 'GET',
                data: $('#searchform').serialize(),

                success: function(response) {

                    $("#table").html(response);

                }

            });

            return false;

        },
        
        submitNewAttendeeForm: function() {

            var firstname = $("#firstname").val();
            var lastname = $("#lastname").val();
            var displayname = $("#displayname").val();
            var sessionid = $("#sessionid").val();
            
            
            
            $.ajax({

                url: 'registration',
                method: 'POST',
                data: $("#registration").serialize(),
                dataType: "json",

                success: function(response) {

                    $("#responseArea").html("Congratulations! You have successfully registered as: " + response.displayname + "<br> <br>Your registration code is: <strong>" + response.code +"</strong>");

                }

            });

        }

    };

}());