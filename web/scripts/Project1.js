/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var Project1 = ( function() {

    return {

        init: function() {
            
            $("#version").html( "jQuery Version: " + $().jquery );

        },
        
        submitSessionForm: function() {

            var sessionID = $("#sessionid").val();
   
            $.ajax({

                url: 'registration?ID=' + sessionID,
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
            var sessionID = $("#sessionid").val();
            
            var dataForm{
                firstname: firstname,
                lastname: lastname,
                displayname: displayname,
                sessionID: sessionid
            }
            
            $.ajax({

                url: 'registration',
                method: 'POST',
                data: dataForm,
                dataType: "json",

                success: function(response) {

                    $("#responseArea").html("Congratulations! You have successfully registered as: " + response.displayname + "<br> <br>Your registration code is: <strong>" + response.code +"</strong>");

                }

            });

        }

    };

}());