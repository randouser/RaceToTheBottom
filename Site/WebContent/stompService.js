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

            that.client.subscribe('/topics/greetings', function(greeting){
                this.showGreeting(JSON.parse(greeting.body));
            });

            that.client.subscribe('/queue/'+token, function(greeting){
                alert(greeting.body);
                console.log(greeting);
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
    ,sendMessage:function(jsonMessage){
        this.client.send("/app/hello", {}, JSON.stringify(jsonMessage));
    }
    ,showGreeting:function(message){
        var response = document.getElementById('response');
        var p = document.createElement('p');
        p.style.wordWrap = 'break-word';
        p.innerHTML = JSON.stringify(message);
        response.appendChild(p);
    }

};

