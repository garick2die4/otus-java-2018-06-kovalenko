package atm.core;

public final class AtmCreationParam
{
	public static final int DEFAULT_CAPACITY = 100;
	
	private final Banknote banknote;
	private final int capacity;
	private final int count;

	public AtmCreationParam(Banknote banknote)
	{
		this(banknote, DEFAULT_CAPACITY);
	}

	public AtmCreationParam(Banknote banknote, int capacity)
	{
		this(banknote, capacity, capacity);
	}

	public AtmCreationParam(Banknote banknote, int capacity, int count)
	{
		this.banknote = banknote;
		this.capacity = capacity;
		this.count = count;
	}
	
	public Banknote banknote()
	{
		return banknote;
	}
	
	public int capacity()
	{
		return capacity;
	}
	
	public int count()
	{
		return count;
	}
}
