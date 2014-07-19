package org.group3.game.services;


import org.group3.game.model.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    @Autowired
    private MailSender mailSender;


    //TODO there probably is a better way to get the location of the server
    private static final String SERVER_ADDRESS = "http://192.168.56.102:8080";
//    private static final String SERVER_ADDRESS = "http://www.cornerinternets.com:8080";

    public void sendMail(String from, String to, String subject, String msg) {
        logger.info("Sending email to: " + to + "  from: " + from );
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(msg);
        mailSender.send(message);
    }


    public void sendEmailInvite(User fromUser,String inviteeEmail,int gameId) throws UnsupportedEncodingException {

        String encodedEmail = URLEncoder.encode(inviteeEmail, "UTF-8");


        sendMail("RaceToTheBottomInfo@gmail.com",
                inviteeEmail,
                fromUser.getName() + " invites you to a game!",
                "Hello " + inviteeEmail + ",\n" + fromUser.getName() + " has started a game with RaceToTheBottom and would like you to join." +
                        "\nFollow this link to join:\n" +
                        SERVER_ADDRESS + "/#register?gameId=" + gameId + "&invitee=" + encodedEmail);
    }


}
