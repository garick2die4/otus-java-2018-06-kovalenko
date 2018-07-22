package myarray.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import myarray.MyArrayList;

public class MyArrayListTests
{

	@Test
	public void test_create_1()
	{
		MyArrayList<Integer> arr = new MyArrayList<>();
		arr.add(1);
		arr.add(2);
		
		assertTrue(arr.size() == 2);
		assertTrue(arr.get(0) == 1);
		assertTrue(arr.get(1) == 2);
	}
	
	@Test
	public void test_create_2()
	{
		MyArrayList<Integer> arr = new MyArrayList<>(2);
		arr.add(1);
		arr.add(2);
		
		assertTrue(arr.size() == 2);
		assertTrue(arr.get(0) == 1);
		assertTrue(arr.get(1) == 2);
		
		arr.add(3);
		arr.add(4);

		assertTrue(arr.size() == 4);
		assertTrue(arr.get(0) == 1);
		assertTrue(arr.get(1) == 2);
		assertTrue(arr.get(2) == 3);
		assertTrue(arr.get(3) == 4);
	}
	
	@Test
	public void test_iter()
	{
		MyArrayList<Integer> arr = new MyArrayList<>();
		arr.add(1);
		arr.add(2);
		arr.add(3);
		arr.add(4);
		arr.add(5);
		
		int sum = 0;
		for (Integer i : arr)
		{
			sum += i;
		}
		
		assertTrue(sum == 15);
	}
	
	@Test
	public void test_add_all()
	{
		MyArrayList<Integer> arr = new MyArrayList<>();
		arr.add(1);
		arr.add(2);
		Collections.addAll(arr, 3, 4, 5);
		
		assertTrue(arr.size() == 5);
		assertTrue(arr.get(0) == 1);
		assertTrue(arr.get(1) == 2);
		assertTrue(arr.get(2) == 3);
		assertTrue(arr.get(3) == 4);
		assertTrue(arr.get(4) == 5);
	}

	@Test
	public void test_copy_to_my()
	{
		MyArrayList<Integer> arr = new MyArrayList<>();
		Collections.addAll(arr, 0, 0, 0, 0, 0);
		
		List<Integer> src = Arrays.asList(1, 2, 3, 4, 5);
		Collections.copy(arr, src);
		
		assertTrue(arr.size() == 5);
		assertTrue(arr.get(0) == 1);
		assertTrue(arr.get(1) == 2);
		assertTrue(arr.get(2) == 3);
		assertTrue(arr.get(3) == 4);
		assertTrue(arr.get(4) == 5);	

	}

	@Test
	public void test_copy_from_my()
	{	
		List<Integer> arr = new ArrayList<>();
		Collections.addAll(arr, 0, 0, 0, 0, 0);
		
		MyArrayList<Integer> src = new MyArrayList<>();
		Collections.addAll(src, 1,2, 3, 4, 5);
		
		Collections.copy(arr, src);
	}
	
	@Test
	public void test_sort()
	{
		MyArrayList<Integer> arr = new MyArrayList<>();
		arr.add(3);
		arr.add(2);
		arr.add(5);
		arr.add(1);
		arr.add(4);
		
		Collections.sort(arr);
		
		assertTrue(arr.get(0) == 1);
		assertTrue(arr.get(1) == 2);
		assertTrue(arr.get(2) == 3);
		assertTrue(arr.get(3) == 4);
		assertTrue(arr.get(4) == 5);	
	}
}
