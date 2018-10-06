package model;

//no lombok or getters/setters; just properties.
public class QueueMessage {
    public String payload;
    public String messageId;
    public QueueMessage(String payload, String messageId) {
        this.payload = payload;
        this.messageId = messageId;
    }
}
