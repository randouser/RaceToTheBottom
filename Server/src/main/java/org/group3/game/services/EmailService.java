package org.group3.game.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private MailSender mailSender;



    public void sendMail(String from, String to, String subject, String msg) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(msg);
        mailSender.send(message);
    }


    public void runMail(){

        sendMail("Race To The Bottom Info",
                "xenogyst@gmail.com",
                "Testing123",
                "Testing only \n\n Hello Spring Email Sender");
    }


}
