package atm.core;

/**
 * Недостаточное количество денег 
 */
public class UnsufficientMoneyException extends Exception
{
	private static final long serialVersionUID = -5843394822788972994L;
	
	// TODO print atm id
	public UnsufficientMoneyException(IAtm atm)
	{
		super("Unsufficient money");
	}

}
