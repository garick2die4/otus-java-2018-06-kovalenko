package myarray.tests;

import static org.junit.Assert.*;

import java.lang.reflect.Array;
import java.util.Arrays;

import org.junit.Test;

import myarray.MyArrayList;

public class MyArrayListTests
{

	@Test
	public void test()
	{
		MyArrayList<Integer> list = new MyArrayList<>();
		list.add(10);
		list.add(12);
		assertTrue(list.size() == 2);
	}

}
