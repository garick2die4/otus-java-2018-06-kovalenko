package myarray;
import java.util.AbstractList;

public class MyArrayList<T> extends AbstractList<T>
{

    public void add(int index, T element) {
        throw new UnsupportedOperationException();
    }

	@Override
	public T get(int index) {
		// TODO Auto-generated method stub
		return null;
	}

    public T remove(int index) {
        throw new UnsupportedOperationException();
    }
    
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

}
