package service;

import model.QueueMessage;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;


public class QueueServiceSpec {

    //integration tests assume a live SQS endpoint or localstack instance
    private QueueServiceImpl target = new QueueServiceImpl("http://localhost:4576", "us-west-2");

    @Test
    public void createQueueAddsQueuesToList() {
        List<String> list = target.list();
        int count = list.size();

        target.create("Queue1" + new Date().getTime());
        list = target.list();
        assertEquals(count + 1, list.size());

        target.create("Queue2" + new Date().getTime());
        target.create("Queue3" + new Date().getTime());
        list = target.list();
        assertEquals(count + 3, list.size());
    }

    @Test
    public void messagesSentToQueueCanBeReceived() throws InterruptedException {
        String queueName = "testQueue" + new Date().getTime();
        String message = "hello world";

        target.create(queueName);
        target.send(queueName, message);
        TimeUnit.SECONDS.sleep(2);
        QueueMessage queuedMessage = target.receive(queueName);

        assertEquals(message, queuedMessage.payload);

        target.delete(queueName, queuedMessage.messageId);
    }
}