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
	int banknoteCount();

	/**
	 * Извлечь деньги из ячейки
	 *  
	 * @param count Количество банкнот
	 * @throws UnsufficientBanknoteCount
	 */
	void get(int count) throws UnsufficientBanknoteCount;
	
	/**
	 * Пополнить ячейку
	 * @param count
	 */
	void add(int count);
}
