GameProxy = {
     games:{}
    ,startGame: function(emailToNotify){
        var user = UserProxy.user;
        StompService.sendMessage('startGame',{userEmail:user.email,userToken:user.token,emailToNotify:emailToNotify,gameType:'player',gameId:null});
    }
    ,takeTurn:function(){
        var gameId = jQuery('#gameStageWrapper').data('gameId');
        jQuery('#waitingScreen').fadeIn();
        var cardsSelected = this.games[gameId].cardsSelected;
        this.games[gameId].clear();
        var user = UserProxy.user;
        StompService.sendMessage('takeTurn',{userEmail:user.email,userToken:user.token,gameId:gameId,cardsPlayed:cardsSelected});
    }
    ,getTurn:function(turnMessage){
        var gameStage = jQuery('#gameStageWrapper');
        var curGameId = gameStage.data('gameId');

        if(curGameId === undefined){
            curGameId = turnMessage.gameId;
            gameStage.fadeIn();
            gameStage.data('gameId',curGameId);
        }

        var game = new Game(turnMessage);

        //only display game stuff if data is for current displayed game
        if(curGameId === turnMessage.gameId){
            game.displayCards();
            game.displayDistricts();
            game.displayResources();
            jQuery('#waitingScreen').fadeOut();
        }
        this.games[game.gameId] = game;


    }


};


// ========== CardView Objects
function CardView(card){
    var that = this;
    this.card = card;

    this.element = document.createElement('div');
    this.element.className = card.type + ' card';
    
    this.element.innerHTML = 
    '<div class="name"><span class="nameText">'+card.name+'</span></div>'+
    '<div class="body"><span class="bodyText">'+card.bodyText+'</span></div>'+
    '<div class="subtext"><span class="subtextText">'+'subtext?'+'</span></div>';

    this.nameElement = this.element.getElementsByClassName('nameText')[0];
    this.bodyElement = this.element.getElementsByClassName('bodyText')[0];
    this.subtextElement = this.element.getElementsByClassName('subtextText')[0];


//    this.element.addEventListener('click',this.clickHandler,false);
    
}
//// superclass method
//CardView.prototype.clickHandler = function(e,cardView) {
//    this.classList.toggle('cardToggle');
//};


// ========== DistrictView Objects
function DistrictView(district,index){
    this.district = district;
    this.className = district.type;
    this.bodyText = 'p1Score: '+district.playerOneScore + '<br />' + 'p2Score: ' + district.playerTwoScore;
    this.index = index;


    this.element = document.createElement('div');
    this.element.className = this.className + ' district';
    
    this.element.innerHTML = 
    '<div class="name"><span class="nameText">'+this.index+' - '+district.type+'</span></div>'+
    '<div class="body"><span class="bodyText">'+this.bodyText+'</span></div>';

    this.nameElement = this.element.getElementsByClassName('nameText')[0];
    this.bodyElement = this.element.getElementsByClassName('bodyText')[0];



    //TODO clean this up so we have separate elements for each score
    this.update = function(){
        this.bodyElement.innerHTML = 'p1Score: Updating'+ '<br />' + 'p2Score: Updating';
    }
}
//// superclass method
//DistrictView.prototype.clickHandler = function() {
//    this.classList.toggle('districtToggle');
//};




// ========== Game Object
function Game(turnMessage){
    var that = this;
    this.gameId = turnMessage.gameId;
    this.hand = turnMessage.hand;
    this.maxWorkers = turnMessage.maxWorkers;
    this.maxMoney = turnMessage.maxMoney;
    this.districts = turnMessage.districts;
    this.districtPointer = turnMessage.districtPointer;
    this.playerIndex = turnMessage.playerIndex;

    this.districtViews = [];

    this.money =this.maxMoney;
    this.workers = this.maxWorkers;
    this.cardsSelected = [];

    this.displayDistricts = function(){
        var districtStage = document.getElementById('districtStage');
        var i = 0;
        this.empty(districtStage);
        for(; i < this.districts.length; ++i){
            var districtV = new DistrictView(this.districts[i],i);
            if(i === this.districtPointer){
                districtV.element.classList.add('districtToggle');
            }
            this.districtViews.push(districtV);
            districtStage.appendChild(districtV.element);
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


}
// superclass method
//Game.prototype.clickHandler = function() {
//    this.classList.toggle('districtToggle');
//};



//function main(){
//    var cardStage = document.getElementById('cardStage');
//    var districtStage = document.getElementById('districtStage');
//
//    var i = 0;
//    for(; i < 5; ++i){
//        var card = new CardView1();
//        cardList.push(card);
//        cardStage.appendChild(card.element);
//
//    }
//
//    i = 0;
//    for(; i < 5; ++i){
//        var district = new DistrictView1();
//        districtList.push(district);
//        districtStage.appendChild(district.element);
//    }
//
//}
//
//
//main();













