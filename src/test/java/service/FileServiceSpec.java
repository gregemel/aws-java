package service;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class FileServiceSpec {

    //integration tests assume a live s3 endpoint or localstack instance
    private static final String S3_SERVICE_ENDPOINT = "http://localhost:4572/";

    private static final String BUCKET = "test-bucket";
    private static final String KEY = "path/to/file.json";

    @Test
    public void testFileServiceAgainstLocalStack() throws IOException {
        FileService fileService = FileServiceFactory.custom(BUCKET, S3_SERVICE_ENDPOINT);
        fileService.createBucket(BUCKET);
        fileService.put(KEY, "{\"created\":true}\n");
        String content = fileService.get(KEY);
        assertEquals("{\"created\":true}\n", content);
    }
}
