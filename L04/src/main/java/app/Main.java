package app;

import java.lang.System;

public class Main
{
    public static void main(String... args) throws InterruptedException
    {
    	TestsLoader loader = new TestsLoader();
    	loader.runTests("tests.SomeTest");
    	loader.runTests("tests.SomeTest2");
    }

}