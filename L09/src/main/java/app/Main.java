package app;

import java.lang.System;

import core.ClassReader;

public class Main
{
    public static void main(String... args) throws InterruptedException, IllegalArgumentException, IllegalAccessException
    {
    	TestSerialize o1 = new TestSerialize("asdasd", "sdfsdfs");
    	StringBuilder stringBuilder = new StringBuilder();
    	ClassReader.save(stringBuilder, o1);
    	String s = stringBuilder.toString();
    }
    

}