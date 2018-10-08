package service;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.*;


public class TopicServiceImpl implements TopicService {

    public TopicServiceImpl(String endpoint, String region) {
        this.endpoint = endpoint;
        this.region = region;
        snsClient = AmazonSNSClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region))
                .build();
    }

    private AmazonSNS snsClient;
    private String endpoint;
    private String region;

    public String create(String topicName) {

        //create a new SNS topic
        CreateTopicRequest request = new CreateTopicRequest(topicName);
        CreateTopicResult result = snsClient.createTopic(request);

        //print TopicArn
        System.out.println(result);

        //get request id for CreateTopicRequest from SNS metadata
        System.out.println("CreateTopicRequest - " + snsClient.getCachedResponseMetadata(request));

        return result.getTopicArn();
    }


    public String subscribe(String snsTopicArn, String sqsQueueArn) {

        //todo: add Policies here

        SubscribeResult subscribeResult = snsClient.subscribe(new SubscribeRequest()
                        .withEndpoint(sqsQueueArn)
                        .withProtocol("sqs")
                        .withTopicArn(snsTopicArn));

        return subscribeResult.getSubscriptionArn();
    }


    public void publish(String topicArn, String msg) {

        PublishResult publishResult = snsClient.publish(new PublishRequest(topicArn, msg));

        System.out.println("MessageId - " + publishResult.getMessageId());
    }
}