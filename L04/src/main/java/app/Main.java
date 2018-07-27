package app;

import java.lang.System;

import core.TestResult;
import core.TestResultType;
import core.TestResults;
import core.TestsStarter;

public class Main
{
    public static void main(String... args) throws InterruptedException
    {
    	runTests("tests.SomeTest");
    	
    	runTests("tests.SomeTest2");
    }
    
    private static void runTests(String className) 
    {
    	System.out.println(className);
    	
    	TestsStarter loader = new TestsStarter();
    	printResults(className, loader.runTestsForClass(className));
    	
    	System.out.println("-------------------------");
    }
    
    private static void printResults(String testName, TestResults results)
    {
    	for (TestResult res : results.results())
    	{
    		if (res.type().equals(TestResultType.OK))
    		{
    			System.out.format("test '%s', result: %s\n", res.name(), getResultTypeAsString(res.type()));
    		}
    		else
    		{
    			
    			System.out.format("test '%s', result: %s, message: %n\n", res.name(), getResultTypeAsString(res.type()), res.message());
    		}
    	}
    }

    private static String getResultTypeAsString(TestResultType type)
    {
    	switch (type)
    	{
		case OK:
			return "OK";
		case FAILED:
			return "FAILED";
		case ERROR:
			return "ERROR";
		default:
			return "UnknownResultType";
		}
    }
}