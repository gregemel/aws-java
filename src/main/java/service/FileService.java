package service;

import java.io.IOException;


public interface FileService {

    String get(String key) throws IOException;

    void put(String key, String data);

    void createBucket(String bucket);
}