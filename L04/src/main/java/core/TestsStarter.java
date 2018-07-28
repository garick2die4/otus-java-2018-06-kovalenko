package core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import annot.After;
import annot.Before;
import annot.Test;

public class TestsStarter
{
	public static TestResults run(Class<?> testClass)
	{
		Method beforeMethod = null;
		Method afterMethod = null;
		List<Method> testMethods = new ArrayList<>();

		for (Method method : testClass.getMethods())
		{
			if (method.getAnnotation(Before.class) != null && beforeMethod == null)
			{
				beforeMethod = method;
			}
			else if (method.getAnnotation(After.class) != null && afterMethod == null)
			{
				afterMethod = method;
				
			}
			else if (method.getAnnotation(Test.class) != null)
			{
				testMethods.add(method);
			}
		}
			
		TestResults results = new TestResults();
		
		for (Method testMethod : testMethods)
		{
			try
			{
				Object testObj = testClass.newInstance();
				
				if (beforeMethod != null)
					beforeMethod.invoke(testObj);
				
				testMethod.invoke(testObj);
				
				if (afterMethod != null)
					afterMethod.invoke(testObj);
				
				results.add(testMethod.getName(), TestResultType.OK);
			}
			catch (IllegalAccessException | IllegalArgumentException | InstantiationException e)
			{
				results.add(testMethod.getName(), TestResultType.ERROR, e.getMessage());
			}
			catch (InvocationTargetException e)
			{
				if (e.getTargetException() instanceof AssertionError)
				{
					AssertionError err = (AssertionError)e.getTargetException();
					results.add(testMethod.getName(), TestResultType.FAILED, err.getMessage());
				}
				else
				{
					results.add(testMethod.getName(), TestResultType.ERROR, e.getMessage());
				}
			}
		}

		return results;
	}
}
