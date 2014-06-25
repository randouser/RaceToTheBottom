StompService = {
     client:null
    ,setConnected:function(connected){
        document.getElementById('connect').disabled = connected;
        document.getElementById('disconnect').disabled = !connected;
        document.getElementById('conversationDiv').style.visibility = connected ? 'visible' : 'hidden';
        document.getElementById('response').innerHTML = '';
    }
    ,connect:function(token){
        var that = this;
        var connectSuccess = function(frame){
            that.setConnected(true);
            console.log('Connected: ' + frame);

            that.client.subscribe('/topic/greetings', function(frame){
                this.showGreeting(JSON.parse(frame.body));
            });

            that.client.subscribe('/queue/'+token+'/invite', function(frame){
                var joinInfo = JSON.parse(frame.body);
                console.log(frame);
                var r = confirm("You've been invited to a game\nDo you accept?");
                if (r == true) {
                    var user = UserProxy.user;
                    that.sendMessage('joinGame',{
                         userEmail:user.email
                        ,userToken:user.token
                        ,gameType:'player'
                        ,gameId:joinInfo.gameId
                        ,emailToNotify:joinInfo.inviteeEmail
                    });
                } else {
                   alert("Invitation Canceled");
                }


            });

            that.client.subscribe('/queue/'+token+'/message', function(frame){
                console.log(frame);
                alert(frame);
            });

            that.client.subscribe('/queue/'+token+'/getTurn', function(frame){
                console.log(frame);
                var turnMessage = JSON.parse(frame.body);
                GameProxy.getTurn(turnMessage);

            });
        };
        var connectFailure = function(error){
            console.log('Connection failure:');
            console.log(error);
        };

        var socket = new SockJS('/gameserver/connect');
        this.client = Stomp.over(socket);
        this.client.connect({},connectSuccess,connectFailure);
    }
    ,disconnect:function(){
        this.client.disconnect();
        this.setConnected(false);
        console.log("Disconnected");
    }
    ,sendMessage:function(destination,jsonMessage){
        this.client.send('/app/'+destination, {}, JSON.stringify(jsonMessage));
    }
    ,showGreeting:function(message){
        var response = document.getElementById('response');
        var p = document.createElement('p');
        p.style.wordWrap = 'break-word';
        p.innerHTML = JSON.stringify(message);
        response.appendChild(p);
    }

};

