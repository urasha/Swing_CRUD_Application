package collection;

import data.Person;
import db.DatabaseCollectionHandler;
import exceptions.FileExceptionMessages;

import java.util.LinkedHashMap;

/**
 * Performs all actions with the collection
 */
public class CollectionManager {
    private LinkedHashMap<String, Person> collection = new LinkedHashMap<>();
    private static CollectionManager instance;

    private CollectionManager() {
    }

    public static synchronized CollectionManager getInstance() {
        if (instance == null) {
            instance = new CollectionManager();
        }
        return instance;
    }

    /**
     * @return Collection instance
     */
    public LinkedHashMap<String, Person> getCollection() {
        return collection;
    }

    public void setCollection(LinkedHashMap<String, Person> collection) {
        this.collection = new LinkedHashMap<>(collection);
    }

    public void tryCreateCollection() {
        try {
            collection = DatabaseCollectionHandler.loadCollection();
        } catch (Exception e) {
            System.out.println(FileExceptionMessages.EXCEPTIONS.get(e.getClass()));
            System.exit(-1);
        }
    }
}
