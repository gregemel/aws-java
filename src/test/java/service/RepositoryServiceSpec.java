package service;

import model.MessageItem;
import org.junit.Test;
import static org.junit.Assert.assertEquals;


public class RepositoryServiceSpec {


    @Test
    public void testRepo() {

        RepositoryService target = new RepositoryServiceImpl();


        //target.createTable();

        MessageItem item = new MessageItem();
        item.setId(105);
        item.setTitle("Book 102 Title");
        item.setISBN("222-2222222222");

        target.save(item);

        MessageItem loadedItem = target.load(item.getId());

        assertEquals(item.getTitle(), loadedItem.getTitle());
        assertEquals(item.getISBN(), loadedItem.getISBN());
    }
}