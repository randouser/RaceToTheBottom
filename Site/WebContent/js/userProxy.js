UserProxy = {
    user:null
    ,inviteeCreds:{gameId:null,inviteeEmail:null}
    ,registerUser:function(serializedForm){
       var that = this;
        var onError = function(detail){
            alert('error in request');
            console.log(detail);
        };
        var onSuccess = function(data){
            var user = data.user;
            that.user = user;
            //TODO this code and login code probably need to be merged/refactored
            var loginMessage = jQuery('.loginHide').hide();
            StompService.connect(user.token,that.inviteeCreds.gameId,that.inviteeCreds.inviteeEmail);
            that.setCookie("user",JSON.stringify(user));
            loginMessage.fadeIn().children().html('You are now connected to server as:<strong>' + user.email + "</strong> in <i>/queue/"+user.token+"</i>");
            window.location.hash='lobby';
            jQuery('#loginWrapper').hide();
            jQuery('#registerWrapper').hide();
            jQuery('#lobbyWrapper').fadeIn();
            jQuery('#logoutButton').show();
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
                if(user.admin === true){
                    jQuery('#adminPanelWrapper').show();
                }
                var loginMessage = jQuery('.loginHide').hide();
                StompService.connect(user.token,null,null,user.admin);
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
    ,loadUserCookie:function(){

        var cookieUser = JSON.parse(this.getCookie('user'));
        if(cookieUser){
            this.user = cookieUser;
            return true;
        }else{
            return false;
        }
    }
    //assumes cookie exists and is loaded
    ,loginWithCookie:function(){

        var that = this;
        var onError = function(detail){
            alert('error in request: '+detail);
            console.log(detail);
        };
        var onSuccess = function(data){
            var user = data.user;
            that.user = user;
            if(user){
                if(user.admin === true){
                    jQuery('#adminPanelWrapper').show();
                }
                var loginMessage = jQuery('.loginHide').hide();
                StompService.connect(user.token,null,null,user.admin);
                that.setCookie("user",JSON.stringify(user));
                loginMessage.fadeIn().children().html('You are now connected to server as:<strong>' + user.email + "</strong> in <i>/queue/"+user.token+"</i>");
                jQuery('#lobbyWrapper').fadeIn();
                window.location.hash='lobby';
                jQuery('#loginWrapper').hide();
                jQuery('#logoutButton').show();
            }else{
                alert(data.message);
                that.logout();
            }

        };

        jQuery.ajax({
            type: "GET",
            url: '/gameserver/user/loginWithToken',
            data: 'email='+encodeURIComponent(UserProxy.user.email)+'&token='+UserProxy.user.token,
            success: onSuccess,
            error:onError
        });

    }
    ,logout:function(){
        document.cookie = 'user' + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
        window.location.hash = '';
        StompService.disconnect();
        jQuery('#logoutButton').hide();
        window.location.reload(true);
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