package service;

import model.QueueMessage;
import org.junit.Test;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class TopicServiceSpec {


    //use the abstracted interface to build a queue that subscribes to a sns topic
    //show it works by publishing to the topic, then receiving from the queue


    @Test
    public void publishMessageToTopShouldHaveMessageInSubscribingQueue() throws InterruptedException {

        TopicService target = new TopicServiceImpl("http://localhost:4575", "us-west-2");

        QueueService qs = new QueueServiceImpl("http://localhost:4576", "us-west-2");

        String queueName = "queue-" + new Date().getTime();
        String topicName = "topic-" + new Date().getTime();
        String data = "data-" + new Date().getTime();

        //queueId
        String queueId = qs.create(queueName);

        //pubsub
        String topic = target.create(topicName);

        //subscription
        String subscription = target.subscribe(topic, queueId);

        //publish
        target.publish(topic, data);

        TimeUnit.SECONDS.sleep(2);

        //receive
        QueueMessage receivedMessage = qs.receive(queueName);

        assertEquals(data, receivedMessage.payload);

        qs.delete(queueName, receivedMessage.messageId);
    }
}


