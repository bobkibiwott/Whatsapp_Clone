package com.codept.whatsappclone;

public class messageClass {

    private String message,receiver,sender,messageID;
    private long timestamp;
    private String sent,seen,received;


    public messageClass() {
    }

    public messageClass(String message, String receiver, String sender, String messageID, long timestamp, String sent, String seen, String received) {
        this.message = message;
        this.receiver = receiver;
        this.sender = sender;
        this.messageID = messageID;
        this.timestamp = timestamp;
        this.sent = sent;
        this.seen = seen;
        this.received = received;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSent() {
        return sent;
    }

    public void setSent(String sent) {
        this.sent = sent;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public String getReceived() {
        return received;
    }

    public void setReceived(String received) {
        this.received = received;
    }
}
