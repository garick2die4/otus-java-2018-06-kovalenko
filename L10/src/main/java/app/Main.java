package app;

import java.lang.System;

import my.orm.IDBService;

public class Main
{
    public static void main(String... args) throws Exception
    {
        try (IDBService<UserDataSet> dbService = getDbService("dbexample", "2die4", "12345"))
        {
            System.out.println(dbService.getMetaData());
            
            dbService.prepareTables();
            
            dbService.save(new UserDataSet("Vasya", (short)30));
            
            dbService.save(new UserDataSet("Petya", (short)41));
            
            UserDataSet user1 = dbService.load(1);
            System.out.println(String.format("User: id = %d, name = %s, age = %d",
        		user1.getId(),
        		user1.getName(),
        		user1.getAge()));
            
            UserDataSet user2 = dbService.load(2);
            System.out.println(String.format("User: id = %d, name = %s, age = %d",
        		user2.getId(),
        		user2.getName(),
        		user2.getAge()));
            
            dbService.deleteTable();
        }
    }

    private static IDBService<UserDataSet> getDbService(String dbName, String userName, String password)
    {
    	return new DBService<>(ConnectionHelper.getConnection(dbName, userName, password), UserDataSet.class);
    }
}