package com.codept.whatsappclone;

public class messageClass {

    private String message,messageStatus,receiver,sender,messageID;
    private long timestamp;


    public messageClass() {
    }

    public messageClass(String message, String messageStatus, String receiver, String sender, String messageID, long timestamp) {
        this.message = message;
        this.messageStatus = messageStatus;
        this.receiver = receiver;
        this.sender = sender;
        this.messageID = messageID;
        this.timestamp = timestamp;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
