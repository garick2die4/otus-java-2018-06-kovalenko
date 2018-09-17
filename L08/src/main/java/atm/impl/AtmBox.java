package atm.impl;

import atm.core.AtmCreationParam;
import atm.core.Banknote;
import atm.core.IAtmBox;
import atm.core.UnsufficientBanknoteCountException;

public class AtmBox implements IAtmBox
{
	private final Banknote banknote;
	private int count;
	private final int capacity;
	
	public AtmBox(AtmCreationParam param)
	{
		this.banknote = param.banknote();
		this.capacity = param.capacity();
		this.count = param.count();
	}

	@Override
	public Banknote banknote()
	{
		return banknote;
	}

	@Override
	public int currrentBanknoteCount()
	{
		return count;
	}

	@Override
	public int capacity()
	{
		return capacity;
	}
	
	@Override
	public void get(int count) throws UnsufficientBanknoteCountException
	{
		if (this.count - count < 0)
			throw new UnsufficientBanknoteCountException(banknote);
		this.count -= count;
	}

	@Override
	public void refill()
	{
		count = capacity;
	}

}
