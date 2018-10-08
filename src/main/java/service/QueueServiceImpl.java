package service;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.*;
import model.QueueMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.*;


public class QueueServiceImpl implements QueueService {

    private AmazonSQS sqsClient;

    public QueueServiceImpl(String sqsEndpoint, String awsRegion) {
        AwsClientBuilder.EndpointConfiguration config = new AwsClientBuilder.EndpointConfiguration(sqsEndpoint, awsRegion);
        sqsClient = AmazonSQSClientBuilder.standard().withEndpointConfiguration(config).build();
    }

    public String create(String queueName) {
        try {
            CreateQueueResult queue = sqsClient.createQueue(new CreateQueueRequest(queueName)
                    .addAttributesEntry("DelaySeconds", "1")
                    .addAttributesEntry("MessageRetentionPeriod", "86400"));

            Map<String, String> queueAttributes = sqsClient.getQueueAttributes(new GetQueueAttributesRequest(queue.getQueueUrl())
                    .withAttributeNames(QueueAttributeName.QueueArn.toString())).getAttributes();

            return queueAttributes.get(QueueAttributeName.QueueArn.toString());

        } catch (AmazonSQSException e) {
            if (!e.getErrorCode().equals("QueueAlreadyExists")) {
                throw e;
            }
        }

        return "";
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
            String json = messages.get(0).getBody();
            System.out.println("received: " + json);

            try {
                JSONObject obj = new JSONObject(json);
                String message = obj.getString("Message");
                return new QueueMessage(message, messages.get(0).getReceiptHandle());
            } catch (JSONException ex) {
                System.out.println("hmm, must not be json: " + ex);
                return new QueueMessage(messages.get(0).getBody(), messages.get(0).getReceiptHandle());
            }
        } else {
            //for now, returning empty for no message
            System.out.println("nope, nothing");
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