GameProxy = {
    //game row states
     WAIT_TURN:'waiting for turn'
    ,WAIT_ACCEPT:'waiting for accept'
    ,TURN_READY:'turn ready'

    ,games:{}
    ,preTurnAnimating:false
    ,curTurnCallback:null


    ,startGame: function(emailToNotify,gameType){
        var user = UserProxy.user;
        StompService.sendMessage('startGameWrapper',{userEmail:user.email,userToken:user.token,emailToNotify:emailToNotify,gameType:gameType,gameId:null});
    }

    ,takeTurn:function(turnType){
        console.log('%c ****** Take Turn: ' + turnType + ' ******','color:orange;');

        this.toggleButtonHandlers(false);

        var gameId = jQuery('#gamePanel').data('gameId');
        var game = this.games[gameId];
        var cardsSelected = game.cardsSelected;

        var burnTurn = (turnType === 'burnTurnButton') || (cardsSelected.length == 0 && !game.debate);
        var surrender = (turnType === 'surrenderButton');

        if(turnType === 'playCardsButton'){
            console.log('cardsSelected = ' + cardsSelected );
            game.clear();
        }
        game.userTurn = false;
        this.updateGameRow(game.gameId,game.gameName,game.gameType,GameProxy.WAIT_TURN,game.inProgress,game.userTurn);


        var user = UserProxy.user;
        StompService.sendMessage('takeTurn',{userEmail:user.email,userToken:user.token,gameId:gameId,cardsPlayed:cardsSelected, burnTurn:burnTurn,debateScore:game.debateScore,surrender:surrender});
    }

    ,getGameStart:function(gameId,gameName,rowMessage,gameType){
        var state;
        if(rowMessage === 'waitForTurn'){
            state = GameProxy.WAIT_TURN;
        }else if (rowMessage === 'waitForAccept'){
            state = GameProxy.WAIT_ACCEPT;
        }else{
            state = '???';
        }
        this.updateGameRow(gameId,gameName,gameType,state,false,false);
    }

    ,displayLeaderBoard:function(leaderBoardUsers){

    	var leaderBoardTable = jQuery('#leaderBoardTable');

    	//clear table rows excluding the header row
        leaderBoardTable.find('tr').not('.header').remove();

    	var i = 0;
    	for (; i < leaderBoardUsers.length; i++){
    		var leadUser = leaderBoardUsers[i];
    		this.createLeaderboardRow(leadUser.id, leadUser.name, leadUser.email, leadUser.wins)
    	}
    	
    	
    }
    
    ,displayLobbyGames:function(turnMessages){
        var i = 0;
        for(;i < turnMessages.length; ++i){
            var m = turnMessages[i];

            //update DOM table
            this.updateGameRowByTurnMessage(m);

            //update games collection
            this.games[m.gameId] = new Game(m);

        }
    }
    
    ,removeGameFromList:function(gameId){
    	
    	var that = this;
    	
    	var row = jQuery('#gamerow_' + gameId);
    	
    	if (row.length == 0)
    		{
    		return
    		}
    	else{
    		
    		row.remove();
        	delete that.games[gameId];
    	}

    	
    }
    	
    
    ,getTurn:function(turnMessage){
        console.groupCollapsed('%c Get Turn','color:blue;');
            console.log(turnMessage);
        console.groupEnd();
        var that = this;
        var gameStage = jQuery('#gamePanel');
        var curGameId = gameStage.data('gameId');

        var game;
        //if it's a new game
        if(this.games[turnMessage.gameId] === null){
            game = new Game(turnMessage);
            this.updateGameRowByTurnMessage(turnMessage);
            this.games[game.gameId] = game;
        }
        //if it's an existing game that is currently being played
        else if(curGameId === turnMessage.gameId && gameStage.is(':visible')){
            game = this.games[turnMessage.gameId];
            //update diff between old and new game
            game.districts = turnMessage.districts;
            game.debate = turnMessage.debate;
            game.lastTurnLogs = turnMessage.lastTurnLogs;
            game.userTurn = turnMessage.userTurn;

            this.updateGameRowByTurnMessage(turnMessage);

            var curTurnCallback = function(){
                //round changed, reset cards, resources
                if(game.districtPointer !== turnMessage.districtPointer || game.hand.length < 5){
                    game.hand = turnMessage.hand;
                    game.maxMoney = turnMessage.maxMoney;
                    game.maxWorkers = turnMessage.maxWorkers;
                    game.money = game.maxMoney;
                    game.workers = game.maxWorkers;
                    game.districtPointer = turnMessage.districtPointer;
                    game.displayCards(null);
                    game.displayResources();
                }

                console.log('%c curTurnCallback()','color:DodgerBlue;');
                jQuery('#waitingScreen').hide();
                game.displayDistricts();

                //pass callback to displayLog, runs when log is finished animating
                game.displayLog(function(){
                    if(turnMessage.debate){
                        console.log('%c Start debate (no toggle buttons)','color:darksalmon;');
                        DebateGame.startDebate();
                    }else{
                        console.log('%c turnMessage.debate = false, enable buttons','color:darksalmon;');
                        that.toggleButtonHandlers(true);
                    }
                });

            };

            //this crazy thing prevents preturn animations (such as card and resource updates) from being wiped out
            //by the log animations by putting the function into a bin that gets called when the preturn animations end.
            if(this.preTurnAnimating){
                console.log('this.preTurnAnimating, set turn log to wait','color:purple');
                this.curTurnCallback = curTurnCallback;
            }else{
                console.log('this.preTurnAnimating == false, run turn logs now','color:purple');
                //display the log animations immediately if there are no other animations going on
                curTurnCallback();
            }
        }
        //if it's an existing game that isn't open, we just overwrite it
        else{
            game = new Game(turnMessage);
            this.updateGameRowByTurnMessage(turnMessage);
            this.games[game.gameId] = game;
        }



    }

    ,updateGameRowByTurnMessage:function(turnMessage){
        var state;
        if(turnMessage.userTurn){
            state = GameProxy.TURN_READY;
        }else if(turnMessage.inProgress){
            state = GameProxy.WAIT_TURN
        }else{
            state = GameProxy.WAIT_ACCEPT;
        }
        this.updateGameRow(turnMessage.gameId,turnMessage.gameName,turnMessage.gameType,state,turnMessage.inProgress,turnMessage.userTurn);
    }
    ,updateGameRow:function(gameId,gameName,gameType,gameState,isInProgress,isTurnReady){
        var row = jQuery('#gamerow_'+gameId);
        if(row.length === 0){
            var rowHtml = '<tr id="gamerow_'+gameId+'" class="gameRow pending" '+gameType+'>'+
                '<td class="gameListNumber"></td>' +
                '<td class="gameName">'+gameName+' ('+gameId+')'+'</td>' +
                '<td class="gameType">'+gameType+'</td>' +
                '<td class="gameState">'+gameState+'</td>' +
                '</tr>';
            row = jQuery(rowHtml);
            jQuery('#gamesInProgressTable').append(row);
        }else{
            var gameTds = row.children('td');
            gameTds.filter('.gameName').text(gameName);
            gameTds.filter('.gameType').text(gameType);
            gameTds.filter('.gameState').text(gameState);
        }


        //the logic is such that we avoid adding click handlers on things that have them
        if(isInProgress && row.hasClass('pending')){
            //turn on features
            row
                .off('click')
                .removeClass('pending')
                .addClass('ready')
                .click({gameId:gameId},function(e){
                    PanelController.goToGame(e.data.gameId);
                })

        } else if(!isInProgress){
            row.addClass('pending');
            
            var GameTds = row.children('td');
            
            var msgGameType = GameTds.filter('.gameType').text();
            
            if (GameTds.filter('.gameState').text() == GameProxy.WAIT_ACCEPT)
            	{
            	
        
            	
            	//Game is not in the game array at this point...
            	//and all the messagewrappers this scope would have
            	//access to only have a concatened "game name"
            	//with the player's emails...
            	//so the emailToNotify in the StartGameMessage is null
            	
            		row
            			.click({gameId:gameId},function(e){
            				
            				var r = confirm("Cancel invite?");
            				
            				if (r == true){
	            				var user = UserProxy.user;
	            				StompService.sendMessage('cancelInvite', {
	            					userEmail:user.email
	                        		,userToken:user.token
	                        		,gameType:msgGameType
	                        		,gameId:gameId
	                        		,emailToNotify:null
	            				});
            				} else{}
            				
            				
            			});
            	
            	}
        }

        //make it animate when the turn is ready
        row.toggleClass('animated rubberBand turnReady',isTurnReady);

    }


    ,finishLogAnimation:function(){
        console.log('%c finishLogAnimation()', 'color:green;');
        jQuery('#waitingScreen').fadeIn();
        if(this.curTurnCallback){
            console.log('%c TurnCallback exists, executing', 'color:green;');
            this.curTurnCallback();
            this.curTurnCallback = null;

        }

    }

    /**
     * Updates the last actions of the player while he/she waits for next turn.
     */
    ,preTurnUpdate:function(preTurnMessage){
        console.groupCollapsed('%cPreTurnUpdate()','color:Magenta ;');
            console.log(preTurnMessage);
        console.groupEnd();
        this.preTurnAnimating = true;
        var that = this;
        var gameId = preTurnMessage.gameId;
        var game = this.games[gameId];

        //maxWorkers & money
        var diffWorkers = preTurnMessage.maxWorkers - game.maxWorkers;
        if(diffWorkers !== 0){
            console.log('%cDiffWorkers !== 0','color:red;');
            game.maxWorkers += diffWorkers;
            game.workers+= diffWorkers;

            console.log('updating active workers to ' +game.workers );
            document.getElementById('workerTotal').innerHTML = game.maxWorkers;
            document.getElementById('workerCount').innerHTML = game.workers;

        }

        var diffMoney = preTurnMessage.maxMoney - game.maxMoney;
        if(diffMoney !== 0){
            console.log('%cDiffmoney !== 0','color:red;');
            game.maxMoney += diffMoney;
            game.money+= diffMoney;

            console.log('updating active money to ' +game.money );
            document.getElementById('moneyTotal').innerHTML = game.maxMoney;
            document.getElementById('moneyCount').innerHTML = game.money;
        }

        //damage/burn notifications, the last log is our turn
        var log = preTurnMessage.lastTurnLogs ? preTurnMessage.lastTurnLogs[0] : null;
        if(log && log.burnTurnLog){
            console.log('%c log.burnTunLog','color:red;');
            var message = document.createElement('div');
            message.className = 'animated rollIn resourceMessage';
            message.innerHTML = '<span>+'+log.moneyAdded+'</span><br><span style="color:red;">+'+log.workersAdded+'</span>';
            document.getElementById('widgetWrapper').appendChild(message);
            setTimeout(function(){
                message.className = 'animated fadeOutDown resourceMessage';
                setTimeout(function(){
                    message.parentNode.removeChild(message);
                    that.preTurnAnimating = false;
                    that.finishLogAnimation();
                },1000);
            },2000);

        }else if(log && log.cardsLog){
            console.log('%c log.cardsLog','color:red;');
            var message = document.createElement('div');
            message.className = 'animated zoomInDown cardsMessage';
            message.innerHTML = '<span>-'+log.cardsDamage+'</span>';
            document.getElementById('opponent').appendChild(message);
            setTimeout(function(){
                message.className = 'animated fadeOutDown cardsMessage';
                setTimeout(function(){
                    message.parentNode.removeChild(message);
                    that.preTurnAnimating = false;
                    that.finishLogAnimation();
                },1000);
            },2000);
        }else{
            this.preTurnAnimating = false;
            this.finishLogAnimation();
        }

        game.displayCards(preTurnMessage.lastCardsDrawn);

        game.currentDistrict.update(preTurnMessage.currentDistrict);



    }


    //generally called from panelController
    ,displayGame:function(gameId){
        var game = this.games[gameId];
        var gameStage = jQuery('#gamePanel');


        gameStage.data('gameId',game.gameId);
        game.displayCards(null);
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

    }

    ,endDebate:function(score){
        console.log('%c End debate, enable buttons', 'color:Fuchsia;');
        var gameId = jQuery('#gamePanel').data('gameId');
        this.games[gameId].debate = false;
        this.games[gameId].debateScore = score;
        this.takeTurn('debate');


    }
    
    ,createLeaderboardRow:function(userId, userName, userEmail, wins){
        var rowHtml =
            '<tr id="leadrow_'+userId+'" class="leadShown active ready" title="Click to select '+userEmail+' as a player to invite">'+
                '<td class="leadNumber"></td>'+
                '<td class="leadEmail">'+userEmail+'</td>' +
                '<td class="leadName">'+userName+'</td>' +
                '<td class="leadWins">'+wins+'</td>' +
            '</tr>';

        jQuery(rowHtml)
            .appendTo('#leaderBoardTable')
            .click(function(){
                jQuery('#emailToNotifyInput').val(userEmail);
            });
    		
    }



    ,toggleButtonHandlers:function(enabled){
        console.log('%c button handlers toggled to:' + enabled,'background-color:green;');
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
                    console.log(this.id + ' clicked');
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


        var gameStage = jQuery('#gamePanel');
        var curGameId = gameStage.data('gameId');

        UserProxy.user.wins = endGameMessage.winsForUser;
        UserProxy.user.losses = endGameMessage.lossesForUser;
        UserProxy.updateUserPanel();

        if(gameId === curGameId && gameStage.is(':visible')){
            PanelController.goToLobbyPanel();
        }


    }

    ,doesGameExist:function(gameId){
        return gameId && this.games[gameId] != undefined;
    }



};


// ========== CardView Objects
function CardView(card){
    var that = this;
    this.card = card;
    this.selected = false;

    this.element = document.createElement('div');
    this.element.className = 'card ' + card.type;

    //TODO dynamically change icon based on cardId
    this.element.innerHTML =
    '<div class="cardTitle"><span class="cardTitleText">'+card.name+'</span></div>'+
    '<div class="cardImgContain"><span class="imgCell"><img src="../images/cards/'+card.tag+'.png" /></span></div>'+
    '<div class="cardBody"><span>&#9733;</span><span class="damageText">'+card.bodyText+'</span><span>&#9733;</span></div>'+
    '<div class="cardCost"><span>$'+card.moneyCost+'</span>&nbsp;<span>'+card.workerCost+'W</span></div>'+
    '<div class="cardSubtext"><span>'+card.subtext+'</span></div>';



    
}



// ========== DistrictView Objects
function DistrictView(district,index,isCurDistrict){
    this.district = district;
    this.className = district.type;
    this.index = index;


    this.element = document.createElement('div');
    this.element.className = 'district';
    var imgIndex = index + 1;
    var dHtml =
    '<div class="districtTextWrapper d'+imgIndex+'">'+
    '<div class="name"><span class="nameText">'+this.index+' - '+district.type+'</span></div>'+
    '<div class="p1Score"><span>Blue: </span><span class="p1ScoreText">'+district.playerOneScore+'</span></div>'+
    '<div class="p2Score"><span>Red:</span><span class="p2ScoreText">'+district.playerTwoScore+'</span></div>';

    if(district.winnerEmail){
        dHtml += '<div class="turn"><span class="turnTextEmail">Winner: '+district.winnerEmail+'</span></div>';
    }else if(isCurDistrict){
        var turnText  = (district.turn < 10) ? (district.turn + 1) : 'Debate';
        dHtml += '<div class="turn"><span class="turnText">Turn: '+turnText+'</span></div>';
    }

    dHtml += '</div><img src="images/districts/d'+imgIndex+'_'+district.color+'.png" />';

    this.element.innerHTML = dHtml;
    this.p1ScoreText = this.element.getElementsByClassName('p1ScoreText')[0];
    this.p2ScoreText = this.element.getElementsByClassName('p2ScoreText')[0];





    this.update = function(district){
        if(district){
            this.district = district;
            this.p1ScoreText.innerHTML = district.playerOneScore;
            this.p2ScoreText.innerHTML = district.playerTwoScore;
        }else{
            this.p1ScoreText.innerHTML = 'Updating...';
            this.p2ScoreText.innerHTML = 'Updating...';
        }

    }
}





// ========== Game Object
function Game(turnMessage){
    var that = this;
    this.gameId = turnMessage.gameId;
    this.gameType = turnMessage.gameType;
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
    this.currentDistrict = null;
    this.inProgress = turnMessage.inProgress;

    this.districtViews = [];
    this.cardViews = [];

    this.money =this.maxMoney;
    this.workers = this.maxWorkers;
    this.cardsSelected = [];


    this.displayDistricts = function(){
        var districtStage = document.getElementById('districtStage');
        var zIndex = 10;
        var i = 0;
        this.empty(districtStage);

        for(; i < this.districts.length; ++i){
            var districtV;
            if(i === this.districtPointer){
                districtV = new DistrictView(this.districts[i],i,true);
                this.currentDistrict = districtV;
                this.currentDistrict.element.classList.add('districtToggle');
            }else{
                districtV = new DistrictView(this.districts[i],i,false);
            }
            districtV.element.style.zIndex = --zIndex;
            this.districtViews.push(districtV);
            districtStage.appendChild(districtV.element);
        }

        if(!turnMessage.debate){
            //delay the animation
            setTimeout(function(){
                that.currentDistrict.element.classList.add('animated','flash');
            },700);
        }

        this.updateMiniInfo(this.currentDistrict);


    };

    this.updateMiniInfo = function(curDistrict){
        var district = curDistrict.district;
        var miniInfo = document.getElementById('miniInfo');
        miniInfo.innerHTML =
            'D'+ this.districtPointer +
            ' | Blue: ' + district.playerOneScore +
            ' | Red: ' + district.playerTwoScore +
            ' | Turn: ' + ((district.turn < 10) ? (district.turn + 1) : 'Debate');

    };

    this.displayCards = function(newCards){
        var cardStage = document.getElementById('cardStage');
        console.group('%c DisplayCards, newCards->','color:blue;');
        console.log(newCards);
        console.groupEnd();


        //if we have new cards, we animate them into place
        if(newCards){
            this.displayAnimatedCards(newCards,0,cardStage,true);
        }else{
            this.empty(cardStage);
            this.cardViews = [];
            this.cardsSelected = [];
            this.displayAnimatedCards(this.hand,0,cardStage,false);
        }



    };

    this.displayAnimatedCards = function(cards,index,stage,isNew){
        if(!cards || cards.length === 0){return;}

        var curCard = cards[index];
        if(isNew){
            that.hand.push(curCard);
        }
        var cardView = new CardView(curCard);
        jQuery(cardView.element).click({cardView:cardView,gameObj:that},that.cardWatcher);
        cardView.element.classList.add('animated','bounceInLeft'); //TODO decide if this is the best animation to use
        stage.appendChild(cardView.element);
        that.cardViews.push(cardView);


        if((index + 1) < cards.length){
            setTimeout(function(){
                that.displayAnimatedCards(cards, index + 1,stage,isNew);
            },100);
        }
    };

    this.displayResources = function(){
        console.log('%c Display Resources','color:green;');
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
            if(element.lastChild.className === 'endEmpty'){
                break;
            }else{
                element.removeChild(element.lastChild);
            }
        }
    };
    this.cardWatcher = function(e){
        var cardView = e.data.cardView;
        var gameObj = e.data.gameObj;
        var card = cardView.card;


        if(!cardView.selected){
            if(((gameObj.money - card.moneyCost) < 0) || ((gameObj.workers - card.workerCost) < 0)){return;}
            console.log('Cards selected before click: ' + gameObj.cardsSelected.length);
            cardView.selected = true;
            gameObj.cardsSelected.push(card); //add if it's not there
            
            gameObj.money -= card.moneyCost;
            gameObj.workers -= card.workerCost;
            cardView.element.classList.add('cardToggle');
        }else{
            console.log('Cards selected before click: ' + gameObj.cardsSelected.length);
            cardView.selected = false;
            gameObj.cardsSelected.splice(gameObj.cardsSelected.indexOf(card),1);  //remove if it's there
            
            gameObj.money += card.moneyCost;
            gameObj.workers += card.workerCost;
            cardView.element.classList.remove('cardToggle');
        }
        gameObj.displayResources();
        console.log('Cards selected after click: ' + gameObj.cardsSelected.length);
    };
    this.clear = function(){
        console.log('%c Game.clear()','color:purple;');
        //remove all selected cards from game and DOM
        for(var i = this.cardViews.length - 1 ; i >= 0 ; --i){
            var cardV = this.cardViews[i];
            if(cardV.selected){
                this.cardViews.splice(i,1);
                this.hand.splice(this.hand.indexOf(cardV.card),1);
                this.money =
                cardV.element.parentNode.removeChild(cardV.element);
            }
        }
        this.cardsSelected = [];


        //reset money
        this.money = this.maxMoney;
        this.workers = this.maxWorkers;
        this.displayResources();

        //update districts
        this.districtViews[this.districtPointer].update();



    };

    this.displayLog = function(completeCallback){
        var that = this;
        jQuery('#turnLogWrapper').show();
        var logContainer = document.getElementById('turnLogWrapper');

        var count = 0;
        var finishCallback = function(){
            ++count;
            console.log('%c finishCallback() - count:' + count,'color:DarkViolet ;');
            if(count === that.lastTurnLogs.length){
                jQuery('#turnLogWrapper').hide();
                console.log('%c kickback to curTurnCallback() from displayLog()','color:DarkViolet ;');
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
        if(this.gameType === 'single'){
            jQuery("#opponentPlayerImage").attr('src','images/politibot.png');
        }else{
            jQuery("#opponentPlayerImage").attr('src','images/player_'+ this.opponentColor + '.png');
        }
    }


}












