UserProxy = {
    user:null
    ,inviteeCreds:{gameId:null,inviteeEmail:null}

    ,onError:function(detail){
        alert('error in request');
        console.log(detail);
    }
    ,onSuccess:function(data){
        var user = data.user;
        //important that we don't use 'this' as these functions become closures
        var that = UserProxy;
        that.user = user;

        if (user){
            that.setCookie("user",JSON.stringify(user));

            StompService.connect(user.token,that.inviteeCreds.gameId,that.inviteeCreds.inviteeEmail,user.admin);

            PanelController.goToLobbyPanel();

            that.inviteeCreds.gameId = null;
            that.inviteeCreds.inviteeEmail = null;
            that.updateUserPanel();


        }else{
            alert(data.message);
        }
    }


    ,registerUser:function(serializedForm){
       var that = this;
        jQuery.ajax({
            type: "POST",
            url: '/gameserver/user/register',
            data: serializedForm,
            success: that.onSuccess,
            error:that.onError
        });

    }

    ,login:function(serializedForm){
        var that = this;
        jQuery.ajax({
            type: "GET",
            url: '/gameserver/user/login',
            data: serializedForm,
            success: that.onSuccess,
            error:that.onError
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
        jQuery.ajax({
            type: "GET",
            url: '/gameserver/user/loginWithToken',
            data: 'email='+encodeURIComponent(UserProxy.user.email)+'&token='+UserProxy.user.token,
            success: that.onSuccess,
            error:that.error
        });

    }

    ,logout:function(){
        document.cookie = 'user' + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
        window.location.hash = '';
        var callback = function(){
            PanelController.reloadPage();
        };
        StompService.disconnect(callback);

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

    ,updateUserPanel:function(){
        var user = this.user;
        jQuery('#userEmailSpan').text(user.email);
        jQuery('#userNameSpan').text(user.name);
        jQuery('#userWinsSpan').text(user.wins);
        jQuery('#userLossesSpan').text(user.losses);
        jQuery('#emailOnTurnCheck').prop('checked',user.sendEmailOnTurn);
    }

    ,updateEmailNotification:function(isEnabled){
        var user = UserProxy.user;
        StompService.sendMessage('updateSendEmailOnTurn',{sendEmailOnTurn:isEnabled,userEmail:user.email,userToken:user.token});
    }

};