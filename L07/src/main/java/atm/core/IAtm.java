package atm.core;

import java.util.Map;

/**
 * Банкомат 
 */
// TODO add ID
public interface IAtm
{
	/**
	 * Положить деньги в банкомат
	 * 
	 * @param banknote Какая банкнота
	 * @param count Количество банкнот
	 * @throws NoSuchBoxExistsException когда нет ячейки с такой банкнотой
	 */
	void putMoney(Banknote banknote, int count) throws NoSuchBoxExistsException;
	
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
}
