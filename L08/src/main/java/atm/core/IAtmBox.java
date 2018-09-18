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
	 * Извлечь деньги из ячейки
	 *  
	 * @param count Количество банкнот
	 * @throws UnsufficientBanknoteCountException
	 */
	void get(int count) throws UnsufficientBanknoteCountException;
	
	AtmBoxMemento save();
	
	void load(AtmBoxMemento state);
}
