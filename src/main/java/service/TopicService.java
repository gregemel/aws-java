package service;

public interface TopicService {
    String create(String topicName);
    String subscribe(String snsTopicArn, String sqsQueueArn);
    void publish(String topicArn, String msg);
}
