package service;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;


public class FileServiceFactory {
    private static final String awsRegion = "us-west-2";

    public static FileService custom(String bucket, String awsServiceEndpoint) {
        return new FileServiceImpl(bucket, getS3(awsServiceEndpoint));
    }

    private static AmazonS3 getS3(String awsServiceEndpoint) {
        AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard();
        builder.setEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(awsServiceEndpoint, awsRegion));
        builder.enablePathStyleAccess();
        return builder.build();
    }
}