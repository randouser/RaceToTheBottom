/**
 * Created by nperkins on 7/29/14.
 */
PanelController = {
    lastHash:''
    ,registerRegex: /register(?:\?gameId=(\d+)&invitee=(.+))?/
    ,gameRegex:/game_(\d+)/

    ,onHashChange:function(e){
        var panelId = window.location.hash.substr(1);
        //hide all the display panels
        var panels = jQuery('.displayPanel').hide();

        //filter to the correct panel.  We separate panels by user and non user access
        var panelEle;
        var defaultPanel;

        var registerMatch = panelId.match(PanelController.registerRegex);
        if(registerMatch){
            panelId = 'register';
            UserProxy.inviteeCreds.gameId = registerMatch[1];
            UserProxy.inviteeCreds.inviteeEmail = registerMatch[2];
        }

        //if user isn't 'logged in', only allow access to non-user panels
        if(!UserProxy.user){
            defaultPanel = '#loginPanel';
            jQuery('#logoutButton').hide();
            jQuery('#adminPanelWrapper').hide();
            panelEle = panels.filter('.nonUserPanel').filter('#'+panelId+'Panel');

        }else{
            defaultPanel = '#lobbyPanel';
            jQuery('#logoutButton').show();
            if(UserProxy.user.admin === true){
                jQuery('#adminPanelWrapper').show();
            }

            var gameMatch = panelId.match(PanelController.gameRegex);//can't use 'this'

            var gameId = gameMatch? parseInt(gameMatch[1]) : null;
            if(gameMatch && GameProxy.doesGameExist(gameId)){
                GameProxy.displayGame(gameId);
                panelId = 'game';
            }
            panelEle = panels.filter('.userPanel').filter('#'+panelId+'Panel');

        }

        //show chosen the panel
        if(panelEle.length > 0){
            panelEle.fadeIn();
        }else{
            panels.filter(defaultPanel).show();
        }

    }

    ,onPageLoad:function(){
        var isCookie = UserProxy.loadUserCookie();

        //Check if a cookie exists, attempt to login with it.
        if(isCookie){
            UserProxy.loginWithCookie();
        }else{
            PanelController.onHashChange(null);
        }

    }

    ,goToLobbyPanel:function(){
        this.loadHash('lobby')
    }
    ,goToGame:function(gameId){
        this.loadHash('game_'+gameId);
    }
    ,goToRegisterPanel:function(){
        this.loadHash('register');
    }
    ,goToLoginPanel:function(){
        this.loadHash('');
    }
    ,reloadPage:function(){
        jQuery('#logoutButton').hide();
        jQuery('#adminPanelWrapper').hide();
        window.location.reload(true);
    }
    ,loadHash:function(hash){
        //if the hash is the current hash, force the change
        if(('#'+hash) === window.location.hash){
            this.onHashChange(null);
        }else{
            window.location.hash = hash;
        }
    }

};


//load the hashChange function
jQuery(window).on('hashchange', PanelController.onHashChange);

//load the onPageLoad function
jQuery(function(){PanelController.onPageLoad();});

//attempt to cleanly close websocket port when window closes
window.onbeforeunload = function(e) {
    StompService.disconnect();
};
