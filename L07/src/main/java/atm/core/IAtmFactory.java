package atm.core;

/**
 * Фабрика для создания банкомата 
 */
public interface IAtmFactory
{
	/**
	 * Создать банкомат, содержащий ячейки соответствующих банкнот
	 * @param array
	 * @return
	 */
	IAtm create(Banknote[] array);
}
