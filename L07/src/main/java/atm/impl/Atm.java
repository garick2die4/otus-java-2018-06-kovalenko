package atm.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import atm.core.Banknote;
import atm.core.IAtm;
import atm.core.IAtmBox;
import atm.core.NoSuchBoxExists;
import atm.core.UnsufficientBanknoteCount;
import atm.core.UnsufficientMoney;

public final class Atm implements IAtm
{
	private Map<Banknote, IAtmBox> boxes = new HashMap<>();
			
	public Atm(List<IAtmBox> boxes)
	{
		for (IAtmBox box : boxes)
		{
			this.boxes.put(box.banknote(), box);	
		}
	}
	
	@Override
	public void putMoney(Banknote banknote, int count) throws NoSuchBoxExists
	{
		if (!boxes.containsKey(banknote))
			throw new NoSuchBoxExists();

		IAtmBox box = boxes.get(banknote);
		box.add(count);
	}

	@Override
	public Map<Banknote, Integer> getMoney(int sum) throws UnsufficientMoney
	{
		if (sum > totalBalance())
			throw new UnsufficientMoney();

		Map<Banknote, Integer> result = new HashMap<>();
		
		Banknote[] banknotes = Banknote.values();
		for (int i = banknotes.length - 1; i >= 0; i--)
		{
			Banknote banknote = banknotes[i];
			
			// нет таких ячеек вообще
			if (!boxes.containsKey(banknote))
				continue;
			
			int c = Math.round(sum / banknote.nominal());
			if (c == 0)
				continue;
			
			IAtmBox box = boxes.get(banknote);
			
			// не хватает банкнот, пропускаем ячейку и пробуем другого номинала
			if (box.banknoteCount() < c)
				continue;

			result.put(banknote, c);
			
			sum -= c * banknote.nominal();
			if (sum == 0)
				break;
		}
		
		// не вся сумма выдана
		if (sum != 0)
			throw new UnsufficientMoney();

		// если хватило денег, реально списываем сумму 
		for (Map.Entry<Banknote, Integer> m : result.entrySet())
		{
			IAtmBox box = boxes.get(m.getKey());
			try
			{
				box.get(m.getValue());
			}
			catch (UnsufficientBanknoteCount e)
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
		// TODO lambda
		for (Map.Entry<Banknote, IAtmBox> entry : boxes.entrySet())
		{
			balance.put(entry.getKey(), entry.getValue().banknoteCount());
		}
		return balance;
	}

	private int totalBalance()
	{
		Map<Banknote, Integer> banknotesBalance = getBalance();
		// TODO lambda
		int sum = 0;
		for (Map.Entry<Banknote, Integer> b : banknotesBalance.entrySet())
		{
			sum += b.getValue() * b.getKey().nominal();
		}
		return sum;
	}
}
