package app;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import annot.After;
import annot.Before;
import annot.Test;

public class TestsLoader
{
	void runTests(String className)
	{
		Object testObj = null;
		Method beforeMethod = null;
		Method afterMethod = null;
		List<Method> testMethods = new ArrayList<>();
		
		ClassLoader loader = TestsLoader.class.getClassLoader();
		try
		{
			Class<?> testClass = loader.loadClass(className);
			
			testObj = testClass.newInstance();
			
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
		}
		catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InstantiationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (beforeMethod != null)
		{
			try
			{
				beforeMethod.invoke(testObj);
			}
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		for (Method testMethod : testMethods)
		{
			try
			{
				testMethod.invoke(testObj);
				
				System.out.format("Test %s OK\n", testMethod.getName());
			}
			catch (IllegalAccessException | IllegalArgumentException e)
			{
				e.printStackTrace();
			}
			catch (InvocationTargetException e)
			{
				if (e.getTargetException() instanceof AssertionError)
				{
					AssertionError err = (AssertionError)e.getTargetException(); 
					System.out.format("Test %s failed with error: %s\n", testMethod.getName(), err.getMessage());
				}
				else
				{
					e.printStackTrace();
				}
			}

		}
		
		if (afterMethod != null)
		{
			try {
				afterMethod.invoke(testObj);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
