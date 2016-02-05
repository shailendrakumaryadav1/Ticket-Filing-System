package controllers;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoURI;

public class MongoDBConnection
{
    private static MongoClient mongoClient = null;
    /*
    private static String database = "mydb";
    private static String host = "localhost";
    private static int port = 27017;
    */
    private static DB db=null;
private static String uriString = "mongodb://user:password@ds059155.mongolab.com:59155/heroku_dk3nm3r5";
        //"mongodb://user:password@ds059155.mongolab.com:59155/heroku_dk3nm3r5";
//Play.application().configuration().getString("play.crypto.secret");
    public static DB connectToMongo() throws Exception
    {
        if (null != db)
        {
            return db;
        }
/*
MongoURI mongoURI = new MongoURI(uriString);
db = mongoURI.connectDB();
        System.setProperty("java.net.preferIPv4Stack" , "true");
*/
//System.setProperty("java.net.preferIPv4Stack" , "true");
        MongoClientURI mongoclientURI = new MongoClientURI(uriString);

        mongoClient = new MongoClient(mongoclientURI);
        db= mongoClient.getDB(mongoclientURI.getDatabase());
        System.out.println("\n\n\ndb initialised\n\n\n");
        db.authenticate(mongoclientURI.getUsername(), mongoclientURI.getPassword());
        return db;
    }
}
