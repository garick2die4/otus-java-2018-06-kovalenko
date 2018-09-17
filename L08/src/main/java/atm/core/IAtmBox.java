package atm.core;

/**
 * Ячейка банкомата 
 */
public interface IAtmBox
{
	/**
	 * Номинал банкнот в ячейки
	 */
	Banknote banknote();

	/**
	 * количество банкнот
	 */
	int currrentBanknoteCount();
	
	/**
	 * вместимость 
	 */
	int capacity();

	/**
	 * Извлечь деньги из ячейки
	 *  
	 * @param count Количество банкнот
	 * @throws UnsufficientBanknoteCountException
	 */
	void get(int count) throws UnsufficientBanknoteCountException;
	
	/**
	 * Пополнить ячейку
	 * @param count
	 */
	void refill();
}
