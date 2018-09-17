package atm.core;

/**
 * Не сущестует ячейки для такой банкноты 
 */
public class NoSuchBoxExistsException extends Exception
{
	private static final long serialVersionUID = -6327481510800252346L;
	
	public NoSuchBoxExistsException(Banknote banknote)
	{
		super("Box with nominal " + banknote.toString() + " does not exists in atm");
	}

}
