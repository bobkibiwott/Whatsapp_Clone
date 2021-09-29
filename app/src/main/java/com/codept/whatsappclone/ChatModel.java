package com.codept.whatsappclone;

public class ChatModel {
    private String UserID,message,sender,receiver,messageID,type;
    private Long timestamp;

    public ChatModel() {
    }

    public ChatModel(String userID, String message, String sender, String receiver, String messageID, String type, Long timestamp) {
        UserID = userID;
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.messageID = messageID;
        this.type = type;
        this.timestamp = timestamp;
    }

    public String getReceiver() {
        return receiver;
    }


    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
