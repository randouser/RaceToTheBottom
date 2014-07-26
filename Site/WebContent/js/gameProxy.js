GameProxy = {
     games:{}

    ,startGame: function(emailToNotify,gameType){
        var user = UserProxy.user;
        StompService.sendMessage('startGame',{userEmail:user.email,userToken:user.token,emailToNotify:emailToNotify,gameType:gameType,gameId:null});
    }

    ,takeTurn:function(turnType){
        jQuery('#waitingScreen').fadeIn();

        var gameId = jQuery('#gameStageWrapper').data('gameId');
        var game = this.games[gameId];
        var cardsSelected = game.cardsSelected;

        var burnTurn = (turnType === 'burnTurnButton') || (cardsSelected.length == 0 && !game.debate);
        var surrender = (turnType === 'surrenderButton');


        game.clear();
        game.userTurn = false;

        var rowMessage = 'waiting for turn';
        jQuery('#gamerow_' + gameId).children('td').html(gameId + ': '+ game.gameName + ' - '+rowMessage);


        var user = UserProxy.user;
        StompService.sendMessage('takeTurn',{userEmail:user.email,userToken:user.token,gameId:gameId,cardsPlayed:cardsSelected, burnTurn:burnTurn,debateScore:game.debateScore,surrender:surrender});
    }

    ,getGameStart:function(gameId,gameName){

        //set during when game is accepted from invite
        var rowMessage = this.games.hasOwnProperty(gameId) ? "Waiting for turn" : "Waiting for Accept";

        //we make it null to indicate that it exists without getting the game state.
        this.games[gameId] = null;
        var newRow = '<tr id="gamerow_'+gameId+'" class="gameRow pending"><td>'+gameName+' - '+rowMessage+'</td></tr>';
        jQuery('#gamesInProgressTable').append(newRow);
    }
    
    ,displayLeaderBoard:function(leaderBoardUsers)
    {
    
    	var i = 0;
    	
    	for (; i < leaderBoardUsers.length; i++){
    		
    		var leadUser = leaderBoardUsers[i];
    		
    		this.activateLeaderboardRow(leadUser.id, leadUser.name, leadUser.email, leadUser.wins)
    		
    	}
    	
    	
    }
    
    ,displayLobbyGames:function(turnMessages){
        var i = 0;
        for(;i < turnMessages.length; ++i){
            var m = turnMessages[i];

            //update DOM table
            this.activateGameRow(m.gameId, m.gameName, m.userTurn, m.inProgress);

            //update games collection
            this.games[m.gameId] = new Game(m);


        }
    }
    
    ,getTurn:function(turnMessage){
        var that = this;
        var gameStage = jQuery('#gameStageWrapper');
        var curGameId = gameStage.data('gameId');

        var game = new Game(turnMessage);

        if(this.games[game.gameId] === null){ //new game, note that null is intentional
            this.activateGameRow(game.gameId,turnMessage.gameName,turnMessage.userTurn,turnMessage.inProgress);
        }else{
            var rowMessage = turnMessage.userTurn? 'turn ready': 'waiting for turn';
            jQuery('#gamerow_' + game.gameId).children('td').html(game.gameId + ': '+ game.gameName + ' - '+rowMessage);
        }

        //only display game stuff if data is for current displayed game
        if(curGameId === turnMessage.gameId && gameStage.is(':visible')){
            game.displayCards();
            game.displayDistricts();
            game.displayResources();
            game.displayPlayerItems();
            jQuery('#waitingScreen').hide();

            //display the log cards.  We pass in the debate stuff as a callback that runs when displayLog is done.
            that.toggleButtonHandlers(false);
            game.displayLog(function(){
                if(turnMessage.debate){
                    DebateGame.startDebate();
                }else{
                    that.toggleButtonHandlers(true);
                }
            });
        }

        this.games[game.gameId] = game;


    }
    ,displayGame:function(gameId){
        var game = this.games[gameId];
        var gameStage = jQuery('#gameStageWrapper');


        gameStage.data('gameId',game.gameId);
        game.displayCards();
        game.displayDistricts();
        game.displayResources();
        game.displayPlayerItems();

        jQuery('#waitingScreen').toggle(!game.userTurn);

        if(game.debate){
            this.toggleButtonHandlers(false);
            DebateGame.startDebate();
        }else{
            DebateGame.deactivate();
            this.toggleButtonHandlers(true);
        }


        jQuery('#lobbyWrapper').hide();
        gameStage.fadeIn();
    }

    ,endDebate:function(score){
        this.toggleButtonHandlers(true);
        var gameId = jQuery('#gameStageWrapper').data('gameId');
        this.games[gameId].debate = false;
        this.games[gameId].debateScore = score;
        this.takeTurn('debate');


    }
    
    ,activateLeaderboardRow:function(userId, userName, userEmail, wins){
    	 
    	var that = this;
    	
    	var row = jQuery('#leadrow_' + userId);
    	
    	//check if user isn't listed
    	if (row.length == 0){
    		
    		var newRow = '<tr id="leadrow_'+ userId+'" class ="leadShown active"><td>'+ 'ID: ' + userId+' UserName: ' + userName + ' Email: ' + userEmail + ' Wins: ' + wins+'</td></tr>';
    		row = jQuery('#leaderBoardTable').append(newRow).find('#leadrow_' + userId);
    		row
				.addClass('ready')
				.click({userId:userId},function(e){
					jQuery('#emailToNotifyInput').val(userEmail);		
			
				})
    		
    	}
    	
    }

    ,activateGameRow:function(gameId,gameName,isUserTurn,isInProgress){
        var that = this;
        var rowMessage = !isInProgress? 'waiting for accept': isUserTurn? 'turn ready': 'waiting for turn';
        var row = jQuery('#gamerow_' + gameId);

        //if it's not there we make a new one
        if(row.length === 0){
            var newRow = '<tr id="gamerow_'+ gameId+'" class="gameRow ready"><td>'+ gameName+' - '+rowMessage+'</td></tr>';
            row = jQuery('#gamesInProgressTable').append(newRow).find('#gamerow_' + gameId);
        }
        if(isInProgress){
            //turn on features
            row
                .removeClass('pending')
                .addClass('ready')
                .click({gameId:gameId},function(e){
                    that.displayGame(e.data.gameId);
                    window.location.hash = 'game';
                })

        } else{
            row.addClass('pending');
        }

        row.children('td').html(gameId + ': '+ gameName + ' - '+rowMessage);

    }
    


    ,toggleButtonHandlers:function(enabled){
        var buttons = jQuery('#widgetWrapper').find('.turnButton');
        buttons.off('click').prop('disabled',true);

        if(enabled){
            buttons
                .prop('disabled',false)
                .click(function(){
                    if(this.id === 'surrenderButton'){
                        var accept = window.confirm("Are you sure you wish to surrender?");
                        if(!accept){return;}
                    }
                    GameProxy.takeTurn(this.id);
                });
        }

    }


    ,endGame:function(endGameMessage){
        var gameId = endGameMessage.gameId;
        var gameName = endGameMessage.gameName;
        var winnerEmail = endGameMessage.winnerEmail;

        alert("Game '" + gameName +"' has ended with " + winnerEmail + " as the victor!");
        delete this.games[endGameMessage.gameId];
        jQuery('#gamerow_' + gameId).remove();


        var gameStage = jQuery('#gameStageWrapper');
        var curGameId = gameStage.data('gameId');

        if(gameId === curGameId && gameStage.is(':visible')){
            window.location.hash = 'lobby';
        }


        //TODO request score update

    }



};


// ========== CardView Objects
function CardView(card){
    var that = this;
    this.card = card;

    this.element = document.createElement('div');
    this.element.className = 'card ' + card.type;

    //TODO dynamically change icon based on cardId
    this.element.innerHTML =
    '<div class="cardTitle"><span class="cardTitleText">'+card.name+'</span></div>'+
    '<div class="cardImgContain"><span class="imgCell"><img src="../images/star.png" /></span></div>'+
    '<div class="cardBody"><span>&#9733;</span><span class="damageText">'+card.bodyText+'</span><span>&#9733;</span></div>'+
    '<div class="cardCost"><span>$'+card.moneyCost+'</span>&nbsp;<span>'+card.workerCost+'W</span></div>'+
    '<div class="cardSubtext"><span>'+card.subtext+'</span></div>';



    
}



// ========== DistrictView Objects
function DistrictView(district,index,isCurDistrict){
    this.district = district;
    this.className = district.type;
    this.bodyText = 'p1Score: '+district.playerOneScore + '<br />' + 'p2Score: ' + district.playerTwoScore;
    this.index = index;


    this.element = document.createElement('div');
    this.element.className = 'district';
    var imgIndex = index + 1;
    var dHtml =
    '<div class="districtTextWrapper d'+imgIndex+'">'+
    '<div class="name"><span class="nameText">'+this.index+' - '+district.type+'</span></div>'+
    '<div class="body"><span class="bodyText">'+this.bodyText+'</span></div>';

    if(district.winnerEmail){
        dHtml += '<div class="turn"><span class="turnText">Winner: '+district.winnerEmail+'</span></div>';
    }else if(isCurDistrict){
        var turnText  = (district.turn < 10) ? (district.turn + 1) : 'Debate';
        dHtml += '<div class="turn"><span class="turnText">Turn: '+turnText+'</span></div>';
    }

    dHtml += '</div><img src="images/districts/d'+imgIndex+'_'+district.color+'.png" />';

    this.element.innerHTML = dHtml;

    this.nameElement = this.element.getElementsByClassName('nameText')[0];
    this.bodyElement = this.element.getElementsByClassName('bodyText')[0];



    //TODO clean this up so we have separate elements for each score
    this.update = function(){
        this.bodyElement.innerHTML = 'p1Score: Updating'+ '<br />' + 'p2Score: Updating';
    }
}





// ========== Game Object
function Game(turnMessage){
    var that = this;
    this.gameId = turnMessage.gameId;
    this.gameName = turnMessage.gameName;
    this.hand = turnMessage.hand;
    this.maxWorkers = turnMessage.maxWorkers;
    this.maxMoney = turnMessage.maxMoney;
    this.districts = turnMessage.districts;
    this.districtPointer = turnMessage.districtPointer;
    this.playerIndex = turnMessage.playerIndex;
    this.userTurn = turnMessage.userTurn;
    this.debate = turnMessage.debate;
    this.debateScore = 0;
    this.lastTurnLogs = turnMessage.lastTurnLogs;
    this.opponentName = turnMessage.opponentName;
    this.playerColor = turnMessage.playerColor;
    this.opponentColor = turnMessage.opponentColor;

    this.districtViews = [];

    this.money =this.maxMoney;
    this.workers = this.maxWorkers;
    this.cardsSelected = [];

    this.displayDistricts = function(){
        var districtStage = document.getElementById('districtStage');
        var i = 0;
        this.empty(districtStage);
        var curDistrict;
        for(; i < this.districts.length; ++i){
            var districtV;
            if(i === this.districtPointer){
                districtV = new DistrictView(this.districts[i],i,true);
                curDistrict = districtV.element;
                curDistrict.classList.add('districtToggle');
            }else{
                districtV = new DistrictView(this.districts[i],i,false);
            }
            this.districtViews.push(districtV);
            districtStage.appendChild(districtV.element);
        }

        if(!turnMessage.debate){
            //delay the animation
            setTimeout(function(){
                curDistrict.classList.add('animated','flash');
            },700);
        }


    };

    this.displayCards = function(){
        var cardStage = document.getElementById('cardStage');
        var i = 0;
        this.empty(cardStage);
        for(; i < this.hand.length; ++i){
            var cardView = new CardView(this.hand[i]);
            var aELObj = {handleEvent:function(e){that.cardWatcher(e,this.cView,that);},cView:cardView};
            cardView.element.addEventListener('click',aELObj,false);
            cardStage.appendChild(cardView.element);
        }
    };

    this.displayResources = function(){
        var moneyWrapper = document.getElementById('moneyCount');
        var workerWrapper = document.getElementById('workerCount');
        var workTotalWrap = document.getElementById('workerTotal');
        var moneyTotalWrap = document.getElementById('moneyTotal');

        moneyWrapper.innerHTML = this.money;
        workerWrapper.innerHTML = this.workers;
        workTotalWrap.innerHTML = this.maxWorkers;
        moneyTotalWrap.innerHTML = this.maxMoney;

    };

    this.empty = function(element){
        while (element.hasChildNodes()) {
            element.removeChild(element.firstChild);
        }
    };
    this.cardWatcher = function(e,cardView,gameObj){
        var card = cardView.card;
        var index = gameObj.cardsSelected.indexOf(card);
        if(index < 0){
            if(((gameObj.money - card.moneyCost) < 0) || ((gameObj.workers - card.workerCost) < 0)){return;}
            gameObj.cardsSelected.push(card); //add if it's not there
            gameObj.money -= card.moneyCost;
            gameObj.workers -= card.workerCost;
            cardView.element.classList.add('cardToggle');
        }else{
            gameObj.cardsSelected.splice(index,1);  //remove if it's there
            gameObj.money += card.moneyCost;
            gameObj.workers += card.workerCost;
            cardView.element.classList.remove('cardToggle');
        }
        gameObj.displayResources();

    };
    this.clear = function(){
        var cardStage = document.getElementById('cardStage');
        var collection = cardStage.getElementsByClassName('cardToggle');
        var i = collection.length;
        while(i > 0){
            --i;
            cardStage.removeChild(collection[i]);
        }
        this.money = this.maxMoney;
        this.workers = this.maxWorkers;

        this.districtViews[this.districtPointer].update();


    };

    this.displayLog = function(completeCallback){
        var that = this;
        jQuery('#turnLogWrapper').show();
        var logContainer = document.getElementById('turnLogWrapper');

        var count = 0;
        var finishCallback = function(){
            ++count;
            if(count === that.lastTurnLogs.length){
                jQuery('#turnLogWrapper').hide();
                completeCallback();
            }
        };

        for(var i = 0; i < this.lastTurnLogs.length; ++i){
            var stage = document.createElement('div');
            stage.id = 'turnLogStage' + i;
            logContainer.appendChild(stage);

            var log = this.lastTurnLogs[i];
            if(log.debateLog || log.burnTurnLog){
                this.displayMessageLog(log,stage,finishCallback);
            }else{
                this.displayLogCardsRecursive(0,log.cardsPlayed,stage,finishCallback);
            }
        }

    };
    this.displayMessageLog = function(log,container,callBack){
        var logDiv = document.createElement('div');
        logDiv.className = "debateLog animated zoomIn";
        logDiv.innerHTML = '<span>'+log.logMessage+'</span>';
        container.appendChild(logDiv);
        setTimeout(function(){
            jQuery(container).fadeOut(400,function(){
                jQuery(container).remove();
                callBack();
            });
        },3000);

    };

    /** takes in a raw cardListModel and the number of cards in the list.
     */
    this.displayLogCardsRecursive = function(cardIndex,cardListModel,cardContainer,completeCallback){
        var that = this;
        if(cardIndex < cardListModel.length){

            var cardView = new CardView(cardListModel[cardIndex]);
            cardView.element.classList.add('animated','bounceInDown');
            cardContainer.appendChild(cardView.element);

             setTimeout(function(){
                that.displayLogCardsRecursive(cardIndex + 1,cardListModel,cardContainer,completeCallback);
            },500);
        }else{
            setTimeout(function(){
                jQuery(cardContainer).fadeOut(400,function(){
                    jQuery(cardContainer).remove();
                    completeCallback();
                });
            },3000);


        }
    };

    this.displayPlayerItems = function(){
        jQuery('#opponentName').text(this.opponentName);
        jQuery('#playerName').text(UserProxy.user.name);

        jQuery("#currentPlayerImage").attr('src','images/player_'+ this.playerColor + '.png');
        jQuery("#opponentPlayerImage").attr('src','images/player_'+ this.opponentColor + '.png');
    }


}

//// ======== Alpha GameLog Object
//function TurnLog(logMessage){
//
//	this.cardsPlayed = logMessage.cardsPlayed;
//	this.totalDamage = logMessage.totalDamage;
//	this.logMessage = logMessage.strLogMessage;
//
//}










