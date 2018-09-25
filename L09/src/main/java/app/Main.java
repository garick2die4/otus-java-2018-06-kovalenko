package app;

import java.lang.System;

import core.ClassReader;

public class Main
{
    public static void main(String... args) throws InterruptedException, IllegalArgumentException, IllegalAccessException
    {
    	TestSerialize o1 = new TestSerialize("aaa", "bbb");
    	TestSerialize2 o2 = new TestSerialize2("ccc", 100, o1);
    	String s = ClassReader.save(o2);
    	System.out.println(s);
    }
    

}