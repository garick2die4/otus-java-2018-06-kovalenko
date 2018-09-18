package atm.core;

/**
 * Отделение
 */
public interface IAtmDepartment
{
	/**
	 * Суммарный баланс всех банкоматов отделения
	 */
	int getBalance();
	
	/**
	 * Заполнить заново все банкоматы отделения
	 */
	void refill();
}
