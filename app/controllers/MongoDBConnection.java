package controllers;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import play.Play;

public class MongoDBConnection
{
    private static MongoClient mongoClient = null;
    private static DB db=null;
    private static String uriString = Play.application().configuration().getString("mongodb.uri");
    public static DB connectToMongo() throws Exception
    {
        if (null != db)
        {
            return db;
        }
        MongoClientURI mongoclientURI = new MongoClientURI(uriString);
        mongoClient = new MongoClient(mongoclientURI);
        db= mongoClient.getDB(mongoclientURI.getDatabase());
        db.authenticate(mongoclientURI.getUsername(), mongoclientURI.getPassword());
        return db;
    }
}
