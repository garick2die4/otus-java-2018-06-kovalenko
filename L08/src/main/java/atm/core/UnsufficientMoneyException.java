package atm.core;

/**
 * Недостаточное количество денег 
 */
public class UnsufficientMoneyException extends Exception
{
	private static final long serialVersionUID = -5843394822788972994L;
	
	public UnsufficientMoneyException(IAtm atm)
	{
		super("Unsufficient money");
	}

}
