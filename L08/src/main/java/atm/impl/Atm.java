package atm.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import atm.core.AtmBoxMemento;
import atm.core.Banknote;
import atm.core.IAtm;
import atm.core.IAtmBox;
import atm.core.NoSuchBoxExistsException;
import atm.core.UnsufficientBanknoteCountException;
import atm.core.UnsufficientMoneyException;

public final class Atm implements IAtm
{
	private Map<Banknote, IAtmBox> boxes = new HashMap<>();
	private Map<Banknote, AtmBoxMemento> boxStates = new HashMap<>();
	
	public Atm(List<IAtmBox> boxes)
	{
		for (IAtmBox box : boxes)
		{
			this.boxes.put(box.banknote(), box);
			this.boxStates.put(box.banknote(), box.save());
		}
	}
	
	@Override
	public void refill(Banknote banknote) throws NoSuchBoxExistsException
	{
		if (!boxes.containsKey(banknote))
			throw new NoSuchBoxExistsException(banknote);

		IAtmBox box = boxes.get(banknote);
		box.load(boxStates.get(banknote));
	}

	@Override
	public Map<Banknote, Integer> getMoney(int sum) throws UnsufficientMoneyException
	{
		if (sum > totalBalance())
			throw new UnsufficientMoneyException(this);

		Map<Banknote, Integer> result = new HashMap<>();
		
		Banknote[] banknotes = Banknote.values();
		for (int i = banknotes.length - 1; i >= 0; i--)
		{
			Banknote banknote = banknotes[i];
			
			if (!hasBoxWithBanknotes(banknote))
				continue;
			
			int c = Math.round(sum / banknote.nominal());
			if (c == 0)
				continue;
			
			IAtmBox box = boxes.get(banknote);
			
			// не хватает банкнот, берем сколько есть
			if (box.currrentBanknoteCount() < c)
				c = box.currrentBanknoteCount();

			result.put(banknote, c);
			
			sum -= c * banknote.nominal();
			if (sum == 0)
				break;
		}
		
		// не вся сумма выдана
		if (sum != 0)
			throw new UnsufficientMoneyException(this);

		// если хватило денег, реально списываем сумму 
		for (Map.Entry<Banknote, Integer> m : result.entrySet())
		{
			IAtmBox box = boxes.get(m.getKey());
			try
			{
				box.get(m.getValue());
			}
			catch (UnsufficientBanknoteCountException e)
			{
				throw new IllegalStateException();
			}
		}
		return new HashMap<>(result);
	}

	@Override
	public Map<Banknote, Integer> getBalance()
	{
		Map<Banknote, Integer> balance = new HashMap<>();
		for (Map.Entry<Banknote, IAtmBox> entry : boxes.entrySet())
		{
			balance.put(entry.getKey(), entry.getValue().currrentBanknoteCount());
		}
		return balance;
	}

	private boolean hasBoxWithBanknotes(Banknote banknote)
	{
		return boxes.containsKey(banknote) && boxes.get(banknote).currrentBanknoteCount() > 0;
	}
	
	private int totalBalance()
	{
		Map<Banknote, Integer> banknotesBalance = getBalance();
		int sum = 0;
		for (Map.Entry<Banknote, Integer> b : banknotesBalance.entrySet())
		{
			sum += b.getValue() * b.getKey().nominal();
		}
		return sum;
	}
}
