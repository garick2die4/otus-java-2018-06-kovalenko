package atm.impl;

import java.util.List;

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
			sum += atm.getTotalBalance();
		}
		return sum;
	}

	@Override
	public void refill() throws NoSuchBoxExistsException
	{
		for (IAtm atm : atms)
		{
			List<Banknote> banknotes = atm.getBanknotes(); 

			for (Banknote banknote : banknotes)
				atm.refill(banknote);
		}		
	}
}
