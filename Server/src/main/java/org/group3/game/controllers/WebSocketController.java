package org.group3.game.controllers;

import org.group3.game.model.Stock;
import org.group3.game.model.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.core.MessagePostProcessor;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.websocket.server.ServerEndpoint;
import java.util.Map;

@Controller
public class WebSocketController {
	
	private static final Logger logger = LoggerFactory.getLogger(WebSocketController.class);
	
	@Autowired
	StockService stockService;



    private final SimpMessagingTemplate messagingTemplate;
    @Autowired
    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }



    @MessageMapping("/hello")
//    @SendTo("/topics/greetings")
    public MessageWrapper greeting(HelloMessage message) throws Exception {

        Stock stock = stockService.findByStockCode("1234");

        System.out.println("*********************");
        System.out.println(messagingTemplate.toString());

        messagingTemplate.convertAndSend("/queue/foo/12345","MessageFoo");

        System.out.println("\n\ndidn't crash222");

        return new MessageWrapper(stock, message.getName());

    }






    private class MessageWrapper{
        private Stock stock;
        private String message;

        private MessageWrapper(Stock stock, String message) {
            this.stock = stock;
            this.message = message;
        }

        public Stock getStock() {
            return stock;
        }

        public void setStock(Stock stock) {
            this.stock = stock;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
