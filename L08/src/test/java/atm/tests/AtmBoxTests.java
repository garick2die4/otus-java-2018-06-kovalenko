package atm.tests;

import org.junit.Assert;
import org.junit.Test;

import atm.core.AtmBoxMemento;
import atm.core.AtmCreationParam;
import atm.core.Banknote;
import atm.core.IAtmBox;
import atm.core.UnsufficientBanknoteCountException;
import atm.impl.AtmBox;

public class AtmBoxTests
{
	/**
	 * Проверка создания ячейки
	 */
	@Test
	public void test_atm_box_empty()
	{
		// Act
		IAtmBox box = new AtmBox(new AtmCreationParam(Banknote.FIVE));
		IAtmBox box2 = new AtmBox(new AtmCreationParam(Banknote.FIVE, 3));
		
		// Assert
		Assert.assertEquals(Banknote.FIVE, box.banknote());
		Assert.assertEquals(AtmCreationParam.DEFAULT_COUNT, box.currrentBanknoteCount());
		Assert.assertEquals(3, box2.currrentBanknoteCount());
	}

	/**
	 * Проверка снятия денег из ячейки
	 */
	@Test
	public void test_atm_box_with_money() throws Exception
	{
		// Prepare
		IAtmBox box = new AtmBox(new AtmCreationParam(Banknote.FIVE, 5));
		
		// Act
		box.get(5);
		
		// Assert
		Assert.assertEquals(0, box.currrentBanknoteCount());
	}
	
	/**
	 * Проверка сохранения состояния ячейки
	 */
	@Test
	public void test_atm_box_save_state() throws Exception
	{
		// Prepare
		IAtmBox box = new AtmBox(new AtmCreationParam(Banknote.FIVE, 10));
		
		// Act
		AtmBoxMemento state1 = box.save();
		box.get(5);
		AtmBoxMemento state2 = box.save();
		
		// Assert
		Assert.assertEquals(10, state1.getState());
		Assert.assertEquals(5, state2.getState());
	}
	
	/**
	 * Проверка загрузки состояния ячейки
	 */
	@Test
	public void test_atm_box_load_state() throws Exception
	{
		// Prepare
		IAtmBox box = new AtmBox(new AtmCreationParam(Banknote.FIVE, 10));
		
		// Act
		AtmBoxMemento state = box.save();
		box.get(5);
		box.load(state);
		
		// Assert
		Assert.assertEquals(10, box.currrentBanknoteCount());
	}

	/**
	 * Проверка исключения при получении слишком большого количества денег из ячейки
	 */
	@Test
	public void test_atm_box_unsufficient_money_exception()
	{
		// Prepare
		IAtmBox box = new AtmBox(new AtmCreationParam(Banknote.FIVE, 5));
		
		// Act
		try
		{
			box.get(10);
			Assert.fail("Expected UnsufficientBanknoteCountException to be thrown");
		}
		catch(UnsufficientBanknoteCountException e)
		{
		}
		
		Assert.assertEquals(5, box.currrentBanknoteCount());
	}

}
