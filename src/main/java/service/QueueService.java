package service;

import model.QueueMessage;

import java.util.List;


public interface QueueService {

    void create(String queueName);

    void send(String queueName, String message);

    QueueMessage receive(String queueName);

    void delete(String queueName, String messageId);

    List<String> list();
}
