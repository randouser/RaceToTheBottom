UserProxy = {
    user:null
    ,registerUser:function(serializedForm){
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
             that.user = user;

            if(user){
                var loginMessage = jQuery('.loginHide').fadeOut();
                StompService.connect(user.token);
                that.setCookie("user",JSON.stringify(user));
                loginMessage.fadeIn().children().html('You are now connected to server as:<strong>' + user.email + "</strong> in <i>/queue/"+user.token+"</i>");
                jQuery('#lobbyWrapper').fadeIn();
                window.location.hash='lobby';
                jQuery('#loginWrapper').hide();
                jQuery('#logoutButton').show();
            }else{
                alert(data.message);
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
    ,loginWithCookie:function(){
        var loginMessage = jQuery('.loginHide').fadeOut();
        var cookieUser = JSON.parse(this.getCookie('user'));
        if(cookieUser){
            this.user = cookieUser;
            StompService.connect(this.user.token);
            loginMessage.fadeIn().children().html('You are now connected to server as:<strong>' + this.user.email + "</strong> in <i>/queue/"+this.user.token+"</i>");
            jQuery('#lobbyWrapper').fadeIn();
            jQuery('#loginWrapper').hide();
            window.location.hash='lobby';
            jQuery('#logoutButton').show();
        }
    }
    ,logout:function(){
        document.cookie = 'user' + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
        window.location.hash = '';
        StompService.disconnect();
        window.location.reload(true);
        jQuery('#logoutButton').hide();
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