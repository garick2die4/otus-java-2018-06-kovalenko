package atm.tests;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import atm.core.Banknote;
import atm.core.IAtm;
import atm.core.IAtmBox;
import atm.core.NoSuchBoxExistsException;
import atm.core.UnsufficientBanknoteCountException;
import atm.core.UnsufficientMoneyException;
import atm.impl.AtmBox;
import atm.impl.AtmFactory;

public class AtmTests
{
	/**
	 * Проверка создания ячейки
	 */
	@Test
	public void test_atm_box_empty()
	{
		// Act
		IAtmBox box = new AtmBox(Banknote.FIVE);
		IAtmBox box2 = new AtmBox(Banknote.FIVE, 3);
		
		// Assert
		Assert.assertEquals(Banknote.FIVE, box.banknote());
		Assert.assertEquals(0, box.banknoteCount());
		Assert.assertEquals(3, box2.banknoteCount());
	}

	/**
	 * Проверка снятия денег из ячейки
	 */
	@Test
	public void test_atm_box_with_money() throws Exception
	{
		// Prepare
		IAtmBox box = new AtmBox(Banknote.FIVE, 5);
		
		// Act
		box.get(5);
		
		// Assert
		Assert.assertEquals(0, box.banknoteCount());
	}
	

	/**
	 * Проверка добавления денег в ячейку
	 */
	@Test
	public void test_atm_box_add_money()
	{
		// Prepare
		IAtmBox box = new AtmBox(Banknote.FIVE);
		
		// Act
		box.add(10);

		// Assert
		Assert.assertEquals(10, box.banknoteCount());
	}
	

	/**
	 * Проверка исключения при получении слишком большого количества денег из ячейки
	 */
	@Test
	public void test_atm_box_unsufficient_money_exception()
	{
		// Prepare
		IAtmBox box = new AtmBox(Banknote.FIVE, 5);
		
		// Act
		try
		{
			box.get(10);
			Assert.fail("Expected UnsufficientBanknoteCount to be thrown");
		}
		catch(UnsufficientBanknoteCountException e)
		{
		}
		
		Assert.assertEquals(5, box.banknoteCount());
	}

	/**
	 * Проверка создания банкомата 
	 */
	@Test
	public void test_create_atm() throws Exception
	{
		IAtm atm = new AtmFactory().create(Banknote.values());
		
		// Assert
		Map<Banknote, Integer> balance = atm.getBalance();
		Assert.assertEquals(0, (int)balance.get(Banknote.ONE));
		Assert.assertEquals(0, (int)balance.get(Banknote.FIVE));
		Assert.assertEquals(0, (int)balance.get(Banknote.TEN));
		Assert.assertEquals(0, (int)balance.get(Banknote.FIFTY));
		Assert.assertEquals(0, (int) balance.get(Banknote.HUNDRED));
	}
	
	/**
	 * Проверка пополения ячеек банкомата 
	 */
	@Test
	public void test_put_banknotes() throws Exception
	{
		// Prepare
		IAtm atm = new AtmFactory().create(Banknote.values());
		
		// Act
		atm.putMoney(Banknote.ONE, 1);
		atm.putMoney(Banknote.FIVE, 2);
		atm.putMoney(Banknote.TEN, 3);
		atm.putMoney(Banknote.FIFTY, 4);
		atm.putMoney(Banknote.HUNDRED, 5);
		
		// Assert
		Map<Banknote, Integer> balance = atm.getBalance();
		Assert.assertEquals(1, (int)balance.get(Banknote.ONE));
		Assert.assertEquals(2, (int)balance.get(Banknote.FIVE));
		Assert.assertEquals(3, (int)balance.get(Banknote.TEN));
		Assert.assertEquals(4, (int)balance.get(Banknote.FIFTY));
		Assert.assertEquals(5, (int) balance.get(Banknote.HUNDRED));
	}

	/**
	 * Проверка невозможности положить деньги, если нет ячейки с номиналом
	 */
	@Test
	public void test_get_money_no_such_box_exception()
	{
		Banknote[] notes = {Banknote.ONE, Banknote.FIVE};
		IAtm atm = new AtmFactory().create(notes);
		// Act
		try
		{
			atm.putMoney(Banknote.HUNDRED, 1);
			Assert.fail("Expected NoSuchBoxExists to be thrown");
		}
		catch(NoSuchBoxExistsException e)
		{
		}

	}
	
	/**
	 * Проверка получения денег номиналом 1
	 */
	@Test
	public void test_get_money_1() throws Exception
	{
		// Prepare
		IAtm atm = new AtmFactory().create(Banknote.values());
		atm.putMoney(Banknote.ONE, 5);

		// Act
		Map<Banknote, Integer> money = atm.getMoney(3);
		
		// Assert
		Assert.assertEquals(1, money.size());
		Assert.assertEquals(3, (int)money.get(Banknote.ONE));
		
		Map<Banknote, Integer> balance = atm.getBalance();
		Assert.assertEquals(2, (int)balance.get(Banknote.ONE));
		Assert.assertEquals(0, (int)balance.get(Banknote.FIVE));
		Assert.assertEquals(0, (int)balance.get(Banknote.TEN));
		Assert.assertEquals(0, (int)balance.get(Banknote.FIFTY));
		Assert.assertEquals(0, (int)balance.get(Banknote.HUNDRED));
	}

	/**
	 * Проверка получения денег минимально возможным количеством банкнот
	 */
	@Test
	public void test_get_money_min_banknote_count() throws Exception
	{
		// Prepare
		IAtm atm = new AtmFactory().create(Banknote.values());
		atm.putMoney(Banknote.ONE, 5);
		atm.putMoney(Banknote.FIVE, 5);

		// Act
		Map<Banknote, Integer> money1 = atm.getMoney(5);
		Map<Banknote, Integer> money2 = atm.getMoney(10);

		// Assert
		Assert.assertEquals(1, money1.size());
		Assert.assertEquals(1, (int)money1.get(Banknote.FIVE));
		
		Assert.assertEquals(1, money2.size());
		Assert.assertEquals(2, (int)money2.get(Banknote.FIVE));
			
		Map<Banknote, Integer> balance = atm.getBalance();
		Assert.assertEquals(2, (int)balance.get(Banknote.FIVE));
	}

	/**
	 * Проверка получения денег номиналом 1, когда не хватает 5
	 */
	@Test
	public void test_get_money_when_nominal_unsufficient() throws Exception
	{
		// Prepare
		IAtm atm = new AtmFactory().create(Banknote.values());
		atm.putMoney(Banknote.ONE, 15);
		atm.putMoney(Banknote.FIVE, 2);

		// Act
		Map<Banknote, Integer> money = atm.getMoney(15);

		// Assert
		Assert.assertEquals(2, money.size());
		Assert.assertEquals(5, (int)money.get(Banknote.ONE));
		Assert.assertEquals(2, (int)money.get(Banknote.FIVE));
	}
	
	/**
	 * Проверка выдачи разными номиналами 
	 */
	@Test
	public void test_get_money_mix_3() throws Exception
	{
		// Prepare
		IAtm atm = new AtmFactory().create(Banknote.values());
		atm.putMoney(Banknote.ONE, 6);
		atm.putMoney(Banknote.FIVE, 5);
		atm.putMoney(Banknote.TEN, 4);
		atm.putMoney(Banknote.FIFTY, 3);
		atm.putMoney(Banknote.HUNDRED, 1);

		// Act
		Map<Banknote, Integer> money = atm.getMoney(188);
		
		// Assert
		Assert.assertEquals(5, money.size());
		Assert.assertEquals(3, (int)money.get(Banknote.ONE));
		Assert.assertEquals(1, (int)money.get(Banknote.FIVE));
		Assert.assertEquals(3, (int)money.get(Banknote.TEN));
		Assert.assertEquals(1, (int)money.get(Banknote.FIFTY));
		Assert.assertEquals(1, (int)money.get(Banknote.HUNDRED));
		
		Map<Banknote, Integer> balance = atm.getBalance();
		Assert.assertEquals(3, (int)balance.get(Banknote.ONE));
		Assert.assertEquals(4, (int)balance.get(Banknote.FIVE));
		Assert.assertEquals(1, (int)balance.get(Banknote.TEN));
		Assert.assertEquals(2, (int)balance.get(Banknote.FIFTY));
		Assert.assertEquals(0, (int)balance.get(Banknote.HUNDRED));
	}
	
	/**
	 * Проверка исключения о недостаточности денег 
	 */
	@Test
	public void test_get_money_exception2() throws Exception
	{ 
		// Prepare
		IAtm atm = new AtmFactory().create(Banknote.values());
		atm.putMoney(Banknote.ONE, 5);
		atm.putMoney(Banknote.FIVE, 5);
		
		// Act
		try
		{
			atm.getMoney(50);
			Assert.fail("Expected UnsufficientMoney to be thrown");
		}
		catch(UnsufficientMoneyException e)
		{
		}

	}
}
