package myarray;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

@SuppressWarnings("unchecked")
public class MyArrayList<T> implements List<T>
{
	private T[] data;
	private static final int DEFAULT_CAPACITY = 10;
	private int capacity = 0;
	private int size = 0;
	
	public MyArrayList()
	{
		this(DEFAULT_CAPACITY);
	}
	
	public MyArrayList(int capacity)
	{
		data = (T[]) new Object[capacity];
		this.capacity = capacity;
	}
	
	private void expand()
	{
		capacity *= 2;
		
		T[] ndata = (T[]) new Object[capacity];
		for (int i = 0; i < size; i++)
		{
			ndata[i] = data[i];
		}
		data = ndata;
	}
	
	@Override
	public boolean add(T e)
	{
		if (size >= capacity)
			expand();

		data[size++] = e;
		return true;
	}

	@Override
	public void add(int index, T element)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(Collection<? extends T> c)
	{
		T[] d = (T[])c.toArray();
		for (int i = 0; i < d.length; i++)
			add(d[i]);
		
		return true;
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean contains(Object o)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsAll(Collection<?> c)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public T get(int index)
	{
		if (index < 0 || index > size)
			throw new IndexOutOfBoundsException();
		
		return data[index];
	}

	@Override
	public int indexOf(Object o)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isEmpty()
	{
		return size == 0;
	}

	@Override
	public Iterator<T> iterator()
	{
		return new MyListIterator(0);
	}

	@Override
	public int lastIndexOf(Object o)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<T> listIterator()
	{
		return new MyListIterator(0);
	}

	@Override
	public ListIterator<T> listIterator(int index)
	{
		if (index < 0 || index > size)
			throw new IndexOutOfBoundsException();
		
		return new MyListIterator(index);
	}

	@Override
	public boolean remove(Object o)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public T remove(int index)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> c)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public T set(int index, T element)
	{
		if (index >= size)
			throw new IndexOutOfBoundsException();
		
		T prevElem = data[index];
		data[index] = element;
		return prevElem;
	}

	@Override
	public int size()
	{
		return size;
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Object[] toArray()
	{
		T [] d = (T[]) new Object[size];
		for (int i = 0; i < size; i++)
			d[i] = data[i];
		return d;
	}

	@Override
	public <T> T[] toArray(T[] a)
	{
		throw new UnsupportedOperationException();
	}
	
	private class MyListIterator implements ListIterator<T>
	{
		int cursorPos = -1;
		int lastIndex = -1;
		
		MyListIterator(int index)
		{
			cursorPos = index - 1;
		}
		
		@Override
		public boolean hasNext()
		{
			return cursorPos != (size - 1);
		}

		@Override
		public T next()
		{
			if (cursorPos > (size - 1))
				throw new NoSuchElementException();
			lastIndex = ++cursorPos;
			return get(lastIndex);
		}

		@Override
		public boolean hasPrevious()
		{
			return cursorPos != -1;
		}

		@Override
		public T previous()
		{
			if (cursorPos == -1)
				throw new NoSuchElementException();
			lastIndex = cursorPos;
			return get(cursorPos--);
		}

		@Override
		public int nextIndex()
		{
			return cursorPos + 1;
		}

		@Override
		public int previousIndex()
		{
			return cursorPos;
		}

		@Override
		public void remove()
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public void set(T e)
		{
			MyArrayList.this.set(lastIndex, e);
			
		}

		@Override
		public void add(T e)
		{
			throw new UnsupportedOperationException();
		}
		
	}
}
