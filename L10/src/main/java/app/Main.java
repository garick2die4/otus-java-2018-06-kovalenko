package app;

import java.lang.System;
import java.util.List;

import my.orm.JsonObjectSerializer;
import ru.otus.base.DBService;
import ru.otus.base.UsersDataSet;
import serialization.internal.SerializationErrorException;

public class Main
{
    public static void main(String... args) throws InterruptedException, IllegalArgumentException, IllegalAccessException
    {
        try (DBService dbService = getDbService()) {
            System.out.println(dbService.getMetaData());
            
            dbService.prepareTables();
            
            dbService.addUsers("tully", "sully");
            
            int id = 1;
            System.out.println(String.format("UserName with id = %d : %s", id, dbService.getUserName(id)));
            
            List<String> names = dbService.getAllNames();
            System.out.println("All names: " + names.toString());
            
            List<UsersDataSet> users = dbService.getAllUsers();
            
            System.out.println("All users: " + users.toString());
            dbService.deleteTables();
        }

    }

    private static DBService getDbService()
    {
    	return new DBService();
    }
}