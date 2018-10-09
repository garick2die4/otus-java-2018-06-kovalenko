package app;

import java.lang.System;
import java.util.Arrays;

import my.orm.datasets.AddressDataSet;
import my.orm.datasets.PhoneDataSet;
import my.orm.datasets.UserDataSet;
import my.orm.hiber.HibernateDBService;
import my.orm.simple.SimpleDBService;

public class Main
{
    public static void main(String... args) throws Exception
    {
        try (IDBService dbService = createSimpleDbService("dbexample", "2die4", "12345"))
        {
        	runService(dbService);
        }
        
        try (IDBService dbService = createHibernateDbService("dbexample", "2die4", "12345"))
        {
        	runService(dbService);
        }
    }

    static void runService(IDBService dbService) throws DBServiceException
    {
        System.out.println(dbService.getMetaData());
        
        dbService.prepare();
        
        {
        	UserDataSet user1 = new UserDataSet(
    			"Vasya",
    			(short)30,
    			"First Lenin Street",
    			Arrays.asList(new PhoneDataSet("111"), new PhoneDataSet("222"), new PhoneDataSet("333")));
        	dbService.save(user1);
        }
        
        {
        	UserDataSet user2 = new UserDataSet(
    			"Petya",
    			(short)41,
    			"Second Lenin Street",
    			Arrays.asList(new PhoneDataSet("444"), new PhoneDataSet("555")));
        	
        	dbService.save(user2);
        }
        
        UserDataSet user1 = dbService.load(1);
        System.out.println(user1.toString());
        
        UserDataSet user2 = dbService.load(2);
        System.out.println(user2.toString());
        
        dbService.shutdown();
    }
    
    private static IDBService createSimpleDbService(String dbName, String userName, String password)
    {
    	return new SimpleDBService(ConnectionHelper.getConnection(dbName, userName, password));
    }
    
    private static IDBService createHibernateDbService(String dbName, String userName, String password)
    {
    	return new HibernateDBService(dbName, userName, password);
    }

}