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
        var that = this;
        var onError = function(detail){
            alert('Error in login request');
            console.log(detail);
        };
        var onSuccess = function(data){
            console.log(data);
             var user = data.user;

            if(user){
                var loginMessage = jQuery('.loginHide').fadeOut();
                StompService.connect(user.token);
                that.setCookie("user",JSON.stringify(user));
                loginMessage.fadeIn().children().html('You are now connected to server as:<strong>' + user.email + "</strong> in <i>/queue/"+user.token+"</i>");
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

    ,setCookie:function(key, value){
        document.cookie = key + '=' +value;

    }
    ,getCookie:function(key) {
        var cookieMatch = document.cookie.match(new RegExp(key + '\\s*=\\s*([^;]+)'));
        if (cookieMatch) {
            return cookieMatch[1];
        } else {
            return null;
        }
    }

};