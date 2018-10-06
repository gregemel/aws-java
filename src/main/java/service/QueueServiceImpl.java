package service;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.*;
import model.QueueMessage;

import java.util.ArrayList;
import java.util.List;


public class QueueServiceImpl implements QueueService {

    private AmazonSQS sqsClient;

    QueueServiceImpl(String sqsEndpoint, String awsRegion) {
        AwsClientBuilder.EndpointConfiguration config = new AwsClientBuilder.EndpointConfiguration(sqsEndpoint, awsRegion);
        sqsClient = AmazonSQSClientBuilder.standard().withEndpointConfiguration(config).build();
    }

    public void create(String queueName) {
        try {
            sqsClient.createQueue(new CreateQueueRequest(queueName)
                    .addAttributesEntry("DelaySeconds", "60")
                    .addAttributesEntry("MessageRetentionPeriod", "86400"));
        } catch (AmazonSQSException e) {
            if (!e.getErrorCode().equals("QueueAlreadyExists")) {
                throw e;
            }
        }
    }

    public void send(String queueName, String message) {
        String queueUrl = sqsClient.getQueueUrl(queueName).getQueueUrl();

        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(message)
                .withDelaySeconds(1);
        sqsClient.sendMessage(send_msg_request);
    }

    public QueueMessage receive(String queueName) {
        String queueUrl = sqsClient.getQueueUrl(queueName).getQueueUrl();

        List<Message> messages = sqsClient.receiveMessage(queueUrl).getMessages();

        if (messages!=null && messages.size() > 0) {
            return new QueueMessage(messages.get(0).getBody(), messages.get(0).getReceiptHandle());
        } else {
            return new QueueMessage("", "");
        }
    }

    public void delete(String queueName, String messageId) {
        String queueUrl = sqsClient.getQueueUrl(queueName).getQueueUrl();
        sqsClient.deleteMessage(queueUrl, messageId);
    }

    public List<String> list() {
        List<String> list = new ArrayList<>();

        ListQueuesResult lq_result = sqsClient.listQueues();
        for (String url : lq_result.getQueueUrls()) {
            list.add(url);
        }

        return list;
    }
}