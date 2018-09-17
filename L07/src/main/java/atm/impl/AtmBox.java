package atm.impl;

import atm.core.Banknote;
import atm.core.IAtmBox;
import atm.core.UnsufficientBanknoteCountException;

public class AtmBox implements IAtmBox
{
	private final Banknote banknote;
	private int count;
	
	public AtmBox(Banknote banknote)
	{
		this.banknote = banknote;
		this.count = 0;
	}
	
	public AtmBox(Banknote banknote, int count)
	{
		this.banknote = banknote;
		this.count = count;
	}
	
	@Override
	public Banknote banknote()
	{
		return banknote;
	}

	@Override
	public int banknoteCount()
	{
		return count;
	}

	@Override
	public void get(int count) throws UnsufficientBanknoteCountException
	{
		if (this.count - count < 0)
			throw new UnsufficientBanknoteCountException(banknote);
		this.count -= count;
	}

	@Override
	public void add(int count)
	{
		this.count += count;
	}

}
