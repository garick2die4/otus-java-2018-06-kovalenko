package atm.impl;

import java.util.List;
import java.util.Map;

import atm.core.Banknote;
import atm.core.IAtm;
import atm.core.IAtmDepartment;
import atm.core.NoSuchBoxExistsException;

public class AtmDepartment implements IAtmDepartment
{
	private final List<IAtm> atms;
	
	public AtmDepartment(List<IAtm> atms)
	{
		this.atms = atms;
	}

	@Override
	public int getBalance()
	{
		int sum = 0;
		for (IAtm atm : atms)
		{
			Map<Banknote, Integer> balance = atm.getBalance();
			for (Map.Entry<Banknote, Integer> entry : balance.entrySet())
				sum += entry.getKey().nominal() * entry.getValue();
		}
		return sum;
	}

	@Override
	public void refill()
	{
		try
		{
			for (IAtm atm : atms)
			{
				Map<Banknote, Integer> balance = atm.getBalance();
				
				for (Map.Entry<Banknote, Integer> entry : balance.entrySet())
					atm.refill(entry.getKey());
			}
		}
		catch (NoSuchBoxExistsException e)
		{
		}
	}
}
