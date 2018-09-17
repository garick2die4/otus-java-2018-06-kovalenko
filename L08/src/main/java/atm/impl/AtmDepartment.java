package atm.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import atm.core.IAtm;
import atm.core.IAtmDepartment;

public class AtmDepartment implements IAtmDepartment
{
	private final List<IAtm> atms = new ArrayList<>();
	
	public AtmDepartment(List<IAtm> atms)
	{
		Collections.copy(this.atms, atms);
	}

	@Override
	public int getBalance()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void refill()
	{
		// TODO Auto-generated method stub
		
	}
}
