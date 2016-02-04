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
private static String uriString = "mongodb://user:password@ds055535.mongolab.com:55535/heroku_4qtvsnsj";

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
        //System.out.println("\n\n\ndb initialised\n\n\n");
//System.out.println(db.authenticate("\n\n\n"+mongoclientURI.getUsername(), mongoclientURI.getPassword()) + "\n\n\n");

        return db;
    }
}
