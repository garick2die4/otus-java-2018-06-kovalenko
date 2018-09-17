package atm.core;

/**
 * Недостаточное количество банкнот 
 */
public class UnsufficientBanknoteCountException extends Exception
{
	private static final long serialVersionUID = 1502754158428386075L;
	
	public UnsufficientBanknoteCountException(Banknote banknote)
	{
		super("Unsufficient banknotes with nominal " + banknote.toString());
	}
}
