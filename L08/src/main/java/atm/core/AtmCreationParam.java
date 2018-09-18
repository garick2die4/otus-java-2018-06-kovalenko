package atm.core;

public final class AtmCreationParam
{
	public static final int DEFAULT_COUNT = 100;
	
	private final Banknote banknote;
	private final int count;

	public AtmCreationParam(Banknote banknote)
	{
		this(banknote, DEFAULT_COUNT);
	}

	public AtmCreationParam(Banknote banknote, int count)
	{
		this.banknote = banknote;
		this.count = count;
	}
	
	public Banknote banknote()
	{
		return banknote;
	}
	
	public int count()
	{
		return count;
	}
}
