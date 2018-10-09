package service;


import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import model.MessageItem;


public class RepositoryServiceImpl implements RepositoryService {

    @Override
    public void save(MessageItem item) {

        DynamoDBMapper mapper = new DynamoDBMapper(getClient());
        mapper.save(item);
    }

    @Override
    public MessageItem load(int hashKey) {

        DynamoDBMapper mapper = new DynamoDBMapper(getClient());
        MessageItem itemRetrieved = mapper.load(MessageItem.class, hashKey);

        System.out.println("Item retrieved:");
        System.out.println(itemRetrieved);

        return itemRetrieved;
    }

    @Override
    public void createTable() {

        DynamoDBMapper mapper = new DynamoDBMapper(getClient());

        CreateTableRequest req = mapper.generateCreateTableRequest(MessageItem.class);
        req.setProvisionedThroughput(new ProvisionedThroughput(5L, 5L));

        getClient().createTable(req);
    }


    private AmazonDynamoDB client = null;

    private AmazonDynamoDB getClient() {

        if(client==null) {
            String sqsEndpoint = "http://localhost:4569";   //4569
            String awsRegion = "us-west-2";

            AwsClientBuilder.EndpointConfiguration config = new AwsClientBuilder.EndpointConfiguration(sqsEndpoint, awsRegion);

            client = AmazonDynamoDBClientBuilder.standard()
                    .withEndpointConfiguration(config)
                    .build();
        }

        return client;
    }
}