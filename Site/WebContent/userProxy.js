UserProxy = {
    registerUser:function(serializedForm){
        var onError = function(detail){
            alert('error in request');
            console.log(detail);
        };
        var onSuccess = function(data){
            alert('success in request');
            console.log(data);
        };

        jQuery.ajax({
            type: "POST",
            url: '/gameserver/user/register',
            data: serializedForm,
            success: onSuccess,
            error:onError
        });



    }



};