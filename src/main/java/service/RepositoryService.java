package service;

import model.MessageItem;

public interface RepositoryService {

    void save(MessageItem item);

    MessageItem load(int hashKey);

    void createTable();
}
