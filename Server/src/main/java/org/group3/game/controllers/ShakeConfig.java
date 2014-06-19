package org.group3.game.controllers;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;


@Component
public class ShakeConfig extends DefaultHandshakeHandler{

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, java.util.Map<java.lang.String,java.lang.Object> attributes){
        return new Principal() {
            @Override
            public String getName() {
                return "Moo";
            }
        };
    }
}
