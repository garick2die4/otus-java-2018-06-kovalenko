package atm.impl;

import atm.core.AtmBoxMemento;
import atm.core.AtmCreationParam;
import atm.core.Banknote;
import atm.core.IAtmBox;
import atm.core.UnsufficientBanknoteCountException;

public class AtmBox implements IAtmBox
{
	private final Banknote banknote;
	private int count;
	
	public AtmBox(AtmCreationParam param)
	{
		this.banknote = param.banknote();
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
	public void get(int count) throws UnsufficientBanknoteCountException
	{
		if (this.count - count < 0)
			throw new UnsufficientBanknoteCountException(banknote);
		this.count -= count;
	}

	@Override
	public AtmBoxMemento save()
	{
		return new AtmBoxMemento(count);
	}
	
	@Override
	public void load(AtmBoxMemento state)
	{
		count = state.getState();
	}

}
