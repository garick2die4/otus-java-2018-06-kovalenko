package atm.core;

public enum Banknote
{
	ONE (1),
	FIVE(5),
	TEN (10),
	FIFTY (50),
	HUNDRED (100);
	
	private final int nominal;
	
	Banknote(int val)
	{
		nominal = val;
	}
	
	public int nominal()
	{
		return nominal;
	}
}
