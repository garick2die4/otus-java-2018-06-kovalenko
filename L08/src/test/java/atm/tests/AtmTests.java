package atm.tests;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import atm.core.AtmCreationParam;
import atm.core.Banknote;
import atm.core.IAtm;
import atm.core.NoSuchBoxExistsException;
import atm.core.UnsufficientMoneyException;
import atm.impl.AtmFactory;

public class AtmTests
{

	/**
	 * Проверка создания банкомата 
	 */
	@Test
	public void test_create_atm() throws Exception
	{
		AtmCreationParam[] params = {
			new AtmCreationParam(Banknote.ONE, 0),
			new AtmCreationParam(Banknote.FIVE, 0),
			new AtmCreationParam(Banknote.TEN, 0),
			new AtmCreationParam(Banknote.FIFTY, 0),
			new AtmCreationParam(Banknote.HUNDRED, 0)
		};
		IAtm atm = new AtmFactory().create(params);
		
		// Assert
		Map<Banknote, Integer> balance = atm.getBalance();
		Assert.assertEquals(0, (int)balance.get(Banknote.ONE));
		Assert.assertEquals(0, (int)balance.get(Banknote.FIVE));
		Assert.assertEquals(0, (int)balance.get(Banknote.TEN));
		Assert.assertEquals(0, (int)balance.get(Banknote.FIFTY));
		Assert.assertEquals(0, (int) balance.get(Banknote.HUNDRED));
		Assert.assertEquals(0, atm.getTotalBalance());
	}
	
	/**
	 * Проверка получения банкнот
	 */
	@Test
	public void test_get_banknotes() throws Exception
	{
		// Prepare
		AtmCreationParam[] params = {
			new AtmCreationParam(Banknote.ONE, 1),
			new AtmCreationParam(Banknote.HUNDRED, 5),
			new AtmCreationParam(Banknote.FIFTY, 4),
			new AtmCreationParam(Banknote.FIVE, 2)
		};
		IAtm atm = new AtmFactory().create(params);

		List<Banknote> banknotes = atm.getBanknotes();
		Banknote[] expected = {Banknote.ONE, Banknote.FIVE, Banknote.FIFTY, Banknote.HUNDRED};
		Assert.assertArrayEquals(expected, banknotes.toArray());
		
	}
	
	/**
	 * Проверка пополения ячеек банкомата 
	 */
	@Test
	public void test_put_banknotes() throws Exception
	{
		// Prepare
		AtmCreationParam[] params = {
			new AtmCreationParam(Banknote.ONE, 1),
			new AtmCreationParam(Banknote.FIVE, 2),
			new AtmCreationParam(Banknote.TEN, 3),
			new AtmCreationParam(Banknote.FIFTY, 4),
			new AtmCreationParam(Banknote.HUNDRED, 5)
		};
		IAtm atm = new AtmFactory().create(params);
		atm.getMoney(166);
		
		// Act
		atm.refill(Banknote.ONE);
		atm.refill(Banknote.FIVE);
		atm.refill(Banknote.TEN);
		atm.refill(Banknote.FIFTY);
		atm.refill(Banknote.HUNDRED);
		
		// Assert
		Map<Banknote, Integer> balance = atm.getBalance();
		Assert.assertEquals(1, (int)balance.get(Banknote.ONE));
		Assert.assertEquals(2, (int)balance.get(Banknote.FIVE));
		Assert.assertEquals(3, (int)balance.get(Banknote.TEN));
		Assert.assertEquals(4, (int)balance.get(Banknote.FIFTY));
		Assert.assertEquals(5, (int) balance.get(Banknote.HUNDRED));
		Assert.assertEquals(741, atm.getTotalBalance());
	}

	/**
	 * Проверка невозможности положить деньги, если нет ячейки с номиналом
	 */
	@Test
	public void test_get_money_no_such_box_exception()
	{
		AtmCreationParam[] params = {
			new AtmCreationParam(Banknote.ONE, 0),
			new AtmCreationParam(Banknote.FIVE, 0)
		};
		
		IAtm atm = new AtmFactory().create(params);
		// Act
		try
		{
			atm.refill(Banknote.HUNDRED);
			Assert.fail("Expected NoSuchBoxExistsException to be thrown");
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
		AtmCreationParam[] params = {
				new AtmCreationParam(Banknote.ONE, 5),
				new AtmCreationParam(Banknote.FIVE, 0),
				new AtmCreationParam(Banknote.TEN, 0),
				new AtmCreationParam(Banknote.FIFTY, 0),
				new AtmCreationParam(Banknote.HUNDRED, 0)
		};		
		IAtm atm = new AtmFactory().create(params);

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
		AtmCreationParam[] params = {
			new AtmCreationParam(Banknote.ONE, 5),
			new AtmCreationParam(Banknote.FIVE, 5),
		};			
		IAtm atm = new AtmFactory().create(params);

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
		AtmCreationParam[] params = {
				new AtmCreationParam(Banknote.ONE, 15),
				new AtmCreationParam(Banknote.FIVE, 2)
		};		
		IAtm atm = new AtmFactory().create(params);

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
		AtmCreationParam[] params = {
				new AtmCreationParam(Banknote.ONE, 6),
				new AtmCreationParam(Banknote.FIVE, 5),
				new AtmCreationParam(Banknote.TEN, 4),
				new AtmCreationParam(Banknote.FIFTY, 3),
				new AtmCreationParam(Banknote.HUNDRED, 1)
		};			
		IAtm atm = new AtmFactory().create(params);

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
		AtmCreationParam[] params = {
				new AtmCreationParam(Banknote.ONE, 5),
				new AtmCreationParam(Banknote.FIVE, 5)
		};			
		IAtm atm = new AtmFactory().create(params);
		
		// Act
		try
		{
			atm.getMoney(50);
			Assert.fail("Expected UnsufficientMoneyException to be thrown");
		}
		catch(UnsufficientMoneyException e)
		{
		}

	}
}
