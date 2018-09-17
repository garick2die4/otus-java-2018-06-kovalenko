package atm.impl;

import java.util.ArrayList;
import java.util.List;

import atm.core.AtmCreationParam;
import atm.core.IAtm;
import atm.core.IAtmBox;
import atm.core.IAtmFactory;

public class AtmFactory implements IAtmFactory
{
	@Override
	public IAtm create(AtmCreationParam[] params)
	{
		List<IAtmBox> boxes = new ArrayList<>();
		for (AtmCreationParam param : params)
			boxes.add(new AtmBox(param));
		return new Atm(boxes);
	}
}
