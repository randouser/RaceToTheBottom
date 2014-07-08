package org.group3.game.controllers;

import org.group3.game.messageWrappers.AdminMessage;
import org.group3.game.model.game.GameService;
import org.group3.game.model.user.User;
import org.group3.game.model.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * Created by nperkins on 7/7/14.
 */
@Controller
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    private final SimpMessagingTemplate messagingTemplate;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    public AdminController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Autowired
    UserService userService;

    @Autowired
    GameService gameService;


    @MessageMapping("/admin_removeUserByEmail")
    public void deleteUserByEmail(AdminMessage message) throws Exception {
        logger.info("Attempting to delete user " + message.getValue());
        User user = assertAdmin(message.getUserEmail(),message.getUserToken());

        userService.deleteUserByEmail(message.getValue());

        //tell the user that the game has been created
        messagingTemplate.convertAndSend("/queue/"+user.getToken()+"/admin","User " + message.getValue() + " has been removed");


    }


    @MessageMapping("/admin_removeGameById")
    public void deleteGameById(AdminMessage message) throws Exception {
        logger.info("Attempting to delete game " + message.getValue());

        User user = assertAdmin(message.getUserEmail(),message.getUserToken());

        gameService.deleteGameById(Integer.parseInt(message.getValue()));

        //tell the user that the game has been created
        messagingTemplate.convertAndSend("/queue/"+user.getToken()+"/admin","Game " + message.getValue()+ " has been removed");


    }




    private User assertAdmin(String userEmail, String userToken){
        User user = userService.getUserByEmailToken(userEmail,userToken);

        if(user.isAdmin()){
            return user;
        }else{
            throw new IllegalArgumentException("Error: User '" + user.getEmail() + "' does not have admin rights");
        }
    }





}
