package atm.core;

import java.util.List;
import java.util.Map;

/**
 * Банкомат 
 */
public interface IAtm
{
	/**
	 * Получить банкноты, которые находятся в банкомате
	 */
	List<Banknote> getBanknotes();
	
	/**
	 * Положить деньги в банкомат
	 * 
	 * @param banknote Какая банкнота
	 * @throws NoSuchBoxExistsException когда нет ячейки с такой банкнотой
	 */
	void refill(Banknote banknote) throws NoSuchBoxExistsException;
	
	/**
	 * Получить сумму денег
	 * 
	 * @param sum требуемая сумма 
	 * @return банкноты и их количества
	 * @throws UnsufficientMoneyException когда недостаточно денег
	 */
	Map<Banknote, Integer> getMoney(int sum) throws UnsufficientMoneyException;
	
	/**
	 * Получить баланс
	 * 
	 * @return банкноты и их количества
	 */
	Map<Banknote, Integer> getBalance();
	
	/**
	 * Получить общий баланс 
	 */
	int getTotalBalance();
}
