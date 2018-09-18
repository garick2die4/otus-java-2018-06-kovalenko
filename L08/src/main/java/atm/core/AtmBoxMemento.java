package atm.core;

public final class AtmBoxMemento
{
	private int state;
	
	public AtmBoxMemento(int state)
	{
		this.state = state;
	}
	
	public int getState()
	{
		return state;
	}
}
