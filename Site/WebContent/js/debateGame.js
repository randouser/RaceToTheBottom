window.requestAnimationFrame = window.requestAnimationFrame || window.mozRequestAnimationFrame ||
    window.webkitRequestAnimationFrame || window.msRequestAnimationFrame;

var DebateGame = {
    score: 0
    ,isDone: false
    ,obstTotal: 10
    ,debater:null
    ,scoreElement: null
    ,scrollStage: null
    ,scrollWrapper:null
    ,debaterElement:null

    /**
     * Initializes Game object, shows the debateGame panel.
     */
    ,startDebate: function(){
        var that = this;
        this.scoreElement = document.getElementById('scoreField');
        this.debaterElement = document.getElementById('debater');
        this.scrollStage = document.getElementById('scrollStage');
        this.scrollWrapper = document.getElementById('scrollWrapper');

        this.score = 0;
        this.scoreElement.innerHTML = this.score;

        this.isDone = false;
        this.obstTotal = 10;
        this.debater = new Debater(this.debaterElement);

        jQuery(this.scrollWrapper).show();
        this.scrollWrapper.className = 'animated bounceInRight';

        jQuery(this.scrollStage).off('click').click(function(){
            if(!that.debater.inAir){
                that.debater.jump();
            }
        });

        jQuery('#scrollOverlay').off('click').click(function(){
            jQuery(this).hide();
            var finishCallback = function(){
                --that.obstTotal;
                if(that.obstTotal === 0){
                    that.endDebate()
                }
            };
            var scoreCallback = function(){
                that.nudgeScore();
            };
            that.generateObstacles(that.obstTotal,scoreCallback,finishCallback);
        });
    }

    /**
     * Ends the game, hides the debatePanel. Notifies GameProxy of score.
     */
    ,endDebate: function(){
        var that = this;
        this.scrollWrapper.className = 'animated bounceOutRight';
        setTimeout(function(){
            jQuery(that.scrollWrapper).hide();
            jQuery('#scrollOverlay').show();
        },1000);
        GameProxy.endDebate(this.score);
    }

    /**
     * For turning the game screen off between switching games.
     */
    ,deactivate: function(){
        jQuery('#scrollWrapper').hide();
        jQuery('#scrollOverlay').show();
    }

    /**
     * Game loop, makes the obstacles at random intervals
     */
    ,generateObstacles: function(number,scoreCallback,finishCallback){

        var that = this;
        var item = this.topics[Math.floor(Math.random()*this.topics.length)];
        var speed = Math.floor(Math.random()*8) + 1;
        var obst1 = new Obstacle(item,speed,this.debaterElement,scoreCallback,finishCallback);
        this.scrollStage.appendChild(obst1.element);
        obst1.start();

        if(number === 1){
            console.log('setting isDone to true');
            this.isDone = true;
            return;
        }
        setTimeout(function(){
            that.generateObstacles(number - 1,scoreCallback,finishCallback);
        },Math.floor(Math.random()*3000) + 200);
    }

    /**
     * Increases the score and the score view.
     */
    ,nudgeScore:function(){
        this.score = this.score + 1;
        this.scoreElement.innerHTML = this.score;
    }

    /**
     * List of obstacles names
     */
    ,topics:[
        "Abortion"
        ,"Government Spending"
        ,"Climate Change"
        ,"Fossil Fuels"
        ,"Terrorism"
        ,"Nuclear Power"
        ,"War"
        ,"Poverty"
        ,"Healthcare"
        ,"Gun Control"
        ,"Affirmative Action"
        ,"NATO"
        ,"Socialism"
        ,"Gay Rights"
        ,"Cuba"
        ,"Racism"
        ,"Feminism"
        ,"Free Trade"
        ,"Patriot Act"
        ,"NSA Surveillance"
        ,"Separation of Church and State"
        ,"Human Cloning"
        ,"School Nutrition"
        ,"Public School Funding"
        ,"Disruptive Family Members"
        ,"Past ties with Radicals"
        ,"Recycling"
        ,"Drug Laws"
        ,"Marijuana"
        ,"P2P File Sharing"
        ,"Women's Rights"
        ,"Past drug use"
        ,"Dixie Chicks"
    ]

};




/*Debater constructor */
function Debater(element){
    //in px and ms
    this.JUMP_HEIGHT = 120;
    this.JUMP_TIME = 1000;
    this.TICK_DELTA = Math.PI / 50;
    this.inAir = false;

    this.element = element;

    this.jump = function(){
        this.inAir = true;
        this.rec_jump(0,this.TICK_DELTA);
    };
    this.rec_jump = function(tick,delta){
        var that = this;
        if(tick === this.JUMP_TIME){
            this.inAir = false;
            return;
        }
        this.element.style.bottom = this.JUMP_HEIGHT * this.timing(delta) + 'px';

        window.requestAnimationFrame(function(){that.rec_jump(tick+20,delta+that.TICK_DELTA)});

    };

    this.timing = function(delta){
        return Math.sin(delta);

    };
}
/*Obstacle constructor */
function Obstacle(text,speed,collisionEle,scoreCallback,finishCallback){
    this.element = document.createElement('div');
    this.element.innerHTML = '<span>'+text+'</span>';
    this.element.className = 'obstacle';
    this.element.style.backgroundColor = '#'+(Math.random()*0xFFFFFF<<0).toString(16);
    this.MAX_DIST = 700; //in px
    this.COLLISION_RANGE = 500;
    this.LOC_DELTA = speed;
    this.collisionEle = collisionEle;


    this.start = function(){
        this.rec_start(0,this.TICK_DELTA);
    };
    this.rec_start = function(delta){
        var that = this;
        if(delta >= this.MAX_DIST){
            this.remove();
            return;
        }
        this.element.style.right = delta + 'px';

        //slight optimization, only check collision when near the object
        if(delta > this.COLLISION_RANGE && this.overlaps(this.element,this.collisionEle)){
            scoreCallback();
        }
        window.requestAnimationFrame(function(){that.rec_start(delta + that.LOC_DELTA)});


    };
    this.remove = function(){
        this.element.parentNode.removeChild(this.element);
        finishCallback();
    }

}

/**
 * Manages collision detection
 */
Obstacle.prototype.overlaps = (function () {
    function getPositions( ele ) {
        var left,top, width, height;
        left = ele.offsetLeft;
        top = ele.offsetTop;
        width = ele.clientWidth;
        height = ele.clientHeight;
        return [ [ left, left + width ], [ top, top + height ] ];
    }

    function comparePositions( p1, p2 ) {
        var r1, r2;
        r1 = p1[0] < p2[0] ? p1 : p2;
        r2 = p1[0] < p2[0] ? p2 : p1;
        return r1[1] > r2[0] || r1[0] === r2[0];
    }

    return function ( a, b ) {
        var pos1 = getPositions( a ),
            pos2 = getPositions( b );
        return comparePositions( pos1[0], pos2[0] ) && comparePositions( pos1[1], pos2[1] );
    };
})();



