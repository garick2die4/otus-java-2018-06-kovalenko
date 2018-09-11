package atm.impl;

import java.util.ArrayList;
import java.util.List;

import atm.core.Banknote;
import atm.core.IAtm;
import atm.core.IAtmBox;
import atm.core.IAtmFactory;

public class AtmFactory implements IAtmFactory
{
	@Override
	public IAtm create(Banknote[] array)
	{
		List<IAtmBox> boxes = new ArrayList<>();
		for (Banknote note : array)
			boxes.add(new AtmBox(note));
		return new Atm(boxes);
	}

}
