package atm.core;

import java.util.Map;

/**
 * Банкомат 
 */
public interface IAtm
{
	/**
	 * Положить деньги в банкомат
	 * 
	 * @param banknote Какая банкнота
	 * @param count Количество банкнот
	 * @throws NoSuchBoxExists когда нет ячейки с такой банкнотой
	 */
	void putMoney(Banknote banknote, int count) throws NoSuchBoxExists;
	
	/**
	 * Получить сумму денег
	 * 
	 * @param sum требуемая сумма 
	 * @return банкноты и их количества
	 * @throws UnsufficientMoney когда недостаточно денег
	 */
	Map<Banknote, Integer> getMoney(int sum) throws UnsufficientMoney;
	
	/**
	 * Получить баланс
	 * 
	 * @return банкноты и их количества
	 */
	Map<Banknote, Integer> getBalance();
}
