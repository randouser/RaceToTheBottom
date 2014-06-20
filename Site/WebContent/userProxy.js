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
    ,login:function(serializedForm){
        var onError = function(detail){
            alert('error in request');
            console.log(detail);
        };
        var onSuccess = function(data){
            console.log(data);

            if(data.user){
                StompService.connect(data.user.token);
            }

        };


        jQuery.ajax({
            type: "GET",
            url: '/gameserver/user/login',
            data: serializedForm,
            success: onSuccess,
            error:onError
        });
    }

    ,setPersistentValue:function(key, value){
        document.cookie = key + '=' +value;

    }
    ,getPersistentValue:function(key) {
        var cookieMatch = document.cookie.match(new RegExp(key + '\\s*=\\s*([^;]+)'));
        if (cookieMatch) {
            return cookieMatch[1];
        } else {
            return null;
        }
    }

};