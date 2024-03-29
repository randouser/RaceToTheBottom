StompService = {
     client:null
    ,setConnected:function(connected){

    }
    ,connect:function(token,gameId,inviteeEmail,isAdmin){
        var that = this;
        var connectSuccess = function(frame){
            that.setConnected(true);
            console.log('Connected: ' + frame);

            that.client.subscribe('/topic/greetings', function(frame){
                alert("You've received a greeting");
                alert(frame.body);
                console.log(frame.body);
            });

            that.client.subscribe('/queue/'+token+'/invite', function(frame){
                var joinInfo = JSON.parse(frame.body);
                
                var r = confirm("You've been invited to a game\nDo you accept?");
                if (r == true) {
                    GameProxy.games[joinInfo.gameId] = null;
                    var user = UserProxy.user;
                    that.sendMessage('joinGame',{
                         userEmail:user.email
                        ,userToken:user.token
                        ,gameType:'player'
                        ,gameId:joinInfo.gameId
                        ,emailToNotify:joinInfo.inviteeEmail
                    });
                } else {
                   
                	var user = UserProxy.user;
                	that.sendMessage('rejectInvite', {
                		userEmail:user.email
                		,userToken:user.token
                		,gameType:'player'
                		,gameId:joinInfo.gameId
                		,emailToNotify:joinInfo.inviteeEmail
                	});
                	
                	alert("Invitation Canceled");
                   
                }


            });

            that.client.subscribe('/queue/'+token+'/message', function(frame){
                var gameMessage = JSON.parse(frame.body);
                switch(gameMessage.type){
                    case 'gameStart':
                        GameProxy.getGameStart(gameMessage.gameId,gameMessage.gameName,gameMessage.message,gameMessage.gameType);
                        break;
                    case 'error':
                        alert(gameMessage.message);
                        break;
                    case 'gameReject':
                    	GameProxy.removeGameFromList(gameMessage.gameId);
                    	break;
                    case 'gameEnd':
                        GameProxy.endGame(gameMessage);
                        break;
                }
                

            });
            
            that.client.subscribe("/queue/updateLeaderboard", function(frame){
            	
            	var lobbyMessage = JSON.parse(frame.body);
            	
            	var leadUsers = lobbyMessage.leaderBoard;
            	
            	GameProxy.displayLeaderBoard(leadUsers);

            });
            
            
            that.client.subscribe('/queue/'+token+'/lobby', function(frame){
            	
                var lobbyMessage = JSON.parse(frame.body);
                
                var leadUsers = lobbyMessage.leaderBoard;
                
                GameProxy.displayLobbyGames(lobbyMessage.inProgressGames);
                
                GameProxy.displayLeaderBoard(leadUsers);
                
                //TODO Make GameProxy.displayLeaderBoard(leadUsers)
                

                var invites = lobbyMessage.invites;
                var i =0;
                for(;i<invites.length; ++i){
                    var r = confirm("You've been invited to a game\nDo you accept?");
                    if (r == true) {
                        GameProxy.games[invites[i].gameId] = null;
                        var user = UserProxy.user;
                        that.sendMessage('joinGame',{
                            userEmail:user.email
                            ,userToken:user.token
                            ,gameType:'player'
                            ,gameId:invites[i].gameId
                            ,emailToNotify:invites[i].inviteeEmail
                        });
                    } else {
                    	
                    	var user = UserProxy.user;
                    	that.sendMessage('rejectInvite', {
                    		userEmail:user.email
                    		,userToken:user.token
                    		,gameType:'player'
                    		,gameId:invites[i].gameId
                    		,emailToNotify:invites[i].inviteeEmail
                    	});
                    	
                        alert("Invitation Canceled");
                    }
                }

                

            });


            that.client.subscribe('/queue/'+token+'/getTurn', function(frame){
                
                var turnMessage = JSON.parse(frame.body);
                GameProxy.getTurn(turnMessage);

            });

            that.client.subscribe('/queue/'+token+'/preTurn', function(frame){
                
                var preTurnMessage = JSON.parse(frame.body);
                GameProxy.preTurnUpdate(preTurnMessage);

            });


            if(isAdmin){
                that.client.subscribe('/queue/'+token+'/admin', function(frame){
                    var adminMessage = JSON.parse(frame.body);
                    alert(adminMessage);
                });
            }

            //Request Lobby Info
            var user = UserProxy.user;
            that.sendMessage('getLobbyInfo',{userEmail:user.email,userToken:user.token,gameId:gameId,inviteeEmail:inviteeEmail});
        };
        var connectFailure = function(error){
            console.log('Connection failure:');
            console.log(error);
            alert('There was a error in connecting to the server');
        };

        var socket = new SockJS('/gameserver/connect');
        this.client = Stomp.over(socket);
        this.client.connect({},connectSuccess,connectFailure);

    }
    ,disconnect:function(callback){

        if(this.client){
            this.client.disconnect(callback)
        }else if(callback){
            callback();
        }
    }
    ,sendMessage:function(destination,jsonMessage){
        console.groupCollapsed('%c sendMessage()-> '+destination,'color:orange;');
            console.log(jsonMessage);
        console.groupEnd();

        this.client.send('/app/'+destination, {}, JSON.stringify(jsonMessage));
    }
    ,showGreeting:function(message){

    }

};

