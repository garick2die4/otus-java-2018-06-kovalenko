package my.orm.messages;

import my.orm.app.HibernateDBService;
import my.orm.messageSystem.Address;
import my.orm.messageSystem.Addressee;
import my.orm.messageSystem.Message;

/**
 * Created by tully.
 */
public abstract class MsgToDB extends Message
{
    public MsgToDB(Address from, Address to)
    {
        super(from, to);
    }

    @Override
    public void exec(Addressee addressee)
    {
        if (addressee instanceof HibernateDBService)
        {
            exec((HibernateDBService) addressee);
        }
    }

    public abstract void exec(HibernateDBService dbService);
}
