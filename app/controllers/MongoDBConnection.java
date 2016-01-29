package controllers;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class MongoDBConnection
{
    private static MongoClient mongoClient = null;
    private static String database = "mydb";
    private static String host = "localhost";
    private static int port = 27017;

    public static DB connectToMongo() throws Exception
    {
        if (null != mongoClient)
        {
            return mongoClient.getDB(database);
        }
        mongoClient = new MongoClient(host, port);
        return mongoClient.getDB(database);
    }
}
