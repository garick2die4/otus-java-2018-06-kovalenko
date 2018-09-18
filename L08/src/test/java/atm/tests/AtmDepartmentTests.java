package atm.tests;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import atm.core.AtmCreationParam;
import atm.core.Banknote;
import atm.core.IAtm;
import atm.core.IAtmDepartment;
import atm.impl.AtmDepartment;
import atm.impl.AtmFactory;

public class AtmDepartmentTests
{
	/**
	 * Проверка создания отделения 
	 */
	@Test
	public void test_create_dept() throws Exception
	{
		AtmCreationParam[] params = {
			new AtmCreationParam(Banknote.ONE, 1),
			new AtmCreationParam(Banknote.FIVE, 1),
			new AtmCreationParam(Banknote.TEN, 1),
			new AtmCreationParam(Banknote.FIFTY, 1),
			new AtmCreationParam(Banknote.HUNDRED, 1)
		};
		IAtm atm1 = new AtmFactory().create(params);
			
		AtmCreationParam[] params2 = {
			new AtmCreationParam(Banknote.ONE, 2),
			new AtmCreationParam(Banknote.FIVE, 2),
			new AtmCreationParam(Banknote.TEN, 2),
			new AtmCreationParam(Banknote.FIFTY, 2),
			new AtmCreationParam(Banknote.HUNDRED, 2)
		};
		IAtm atm2 = new AtmFactory().create(params2);
		
		IAtmDepartment dept = new AtmDepartment(Arrays.asList(atm1, atm2));
		
		Assert.assertEquals(498, dept.getBalance());
		
		atm1.getMoney(10);
		atm2.getMoney(50);
		
		Assert.assertEquals(438, dept.getBalance());
		
		dept.refill();
		
		Assert.assertEquals(498, dept.getBalance());
	}
	
	/**
	 * Проверка баланса отделения после снятия денег в банкоматах
	 */
	@Test
	public void test_dept_balance_after_atm_get() throws Exception
	{
		AtmCreationParam[] params = {
			new AtmCreationParam(Banknote.ONE, 1),
			new AtmCreationParam(Banknote.FIVE, 1),
			new AtmCreationParam(Banknote.TEN, 1),
			new AtmCreationParam(Banknote.FIFTY, 1),
			new AtmCreationParam(Banknote.HUNDRED, 1)
		};
		IAtm atm1 = new AtmFactory().create(params);
			
		AtmCreationParam[] params2 = {
			new AtmCreationParam(Banknote.ONE, 2),
			new AtmCreationParam(Banknote.FIVE, 2),
			new AtmCreationParam(Banknote.TEN, 2),
			new AtmCreationParam(Banknote.FIFTY, 2),
			new AtmCreationParam(Banknote.HUNDRED, 2)
		};
		IAtm atm2 = new AtmFactory().create(params2);
		
		IAtmDepartment dept = new AtmDepartment(Arrays.asList(atm1, atm2));
		
		atm1.getMoney(10);
		atm2.getMoney(50);
		
		Assert.assertEquals(438, dept.getBalance());
	}
	
	/**
	 * Проверка пополнения всех банкоматов отделения 
	 */
	@Test
	public void test_dept_refill() throws Exception
	{
		AtmCreationParam[] params = {
			new AtmCreationParam(Banknote.ONE, 1),
			new AtmCreationParam(Banknote.FIVE, 1),
			new AtmCreationParam(Banknote.TEN, 1),
			new AtmCreationParam(Banknote.FIFTY, 1),
		};
		IAtm atm1 = new AtmFactory().create(params);
			
		AtmCreationParam[] params2 = {
			new AtmCreationParam(Banknote.ONE, 2),
			new AtmCreationParam(Banknote.FIVE, 2),
			new AtmCreationParam(Banknote.FIFTY, 2),
			new AtmCreationParam(Banknote.HUNDRED, 2)
		};
		IAtm atm2 = new AtmFactory().create(params2);
		
		IAtmDepartment dept = new AtmDepartment(Arrays.asList(atm1, atm2));
		
		atm1.getMoney(10);
		atm2.getMoney(50);
		
		dept.refill();
		
		Assert.assertEquals(378, dept.getBalance());
	}

}
