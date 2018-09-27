package app;

import java.lang.System;

import serialization.JsonObjectSerializer;
import serialization.internal.SerializationErrorException;

public class Main
{
    public static void main(String... args) throws InterruptedException, IllegalArgumentException, IllegalAccessException
    {
    	TestObjectWithPrimitiveTypes o1 = new TestObjectWithPrimitiveTypes("aaa", 100, true, 1.5);
    	save(o1);
    	
    	TestObjectWithAnotherObject o2 = new TestObjectWithAnotherObject("ccc", o1);
    	save(o2);
    	
    	TestObjectWithList<String> o3 = new TestObjectWithList<String>("str1", "str2", "str3");
    	save(o3);
    	
    	TestObjectWithMap<KeyType, TestObjectMapValue> o4 = new TestObjectWithMap<KeyType, TestObjectMapValue>();
    	o4.add(new KeyType("a"), new TestObjectMapValue("s1", 10));
    	o4.add(new KeyType("b"), new TestObjectMapValue("s2", 100));
    	o4.add(new KeyType("c"), new TestObjectMapValue("s3", 1000));
    	save(o4);
    }
    	
	private static void save(Object obj)
	{
		try
		{
	    	String s = JsonObjectSerializer.save(obj);
			System.out.println(s);
		}
		catch (SerializationErrorException e)
		{
			e.printStackTrace();
		}
	}
}