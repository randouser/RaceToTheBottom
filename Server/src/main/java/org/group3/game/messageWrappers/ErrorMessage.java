package org.group3.game.messageWrappers;

/**
 * Created by nperkins on 7/6/14.
 */
public class ErrorMessage {

    String type = "error";
    String message;


    public ErrorMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
