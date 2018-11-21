package my.orm.messages;

import my.orm.app.FrontendService;
import my.orm.messageSystem.Address;
import my.orm.messageSystem.Addressee;
import my.orm.messageSystem.Message;

/**
 * Created by tully.
 */
public abstract class MsgToFrontend extends Message
{
    public MsgToFrontend(Address from, Address to)
    {
        super(from, to);
    }

    @Override
    public void exec(Addressee addressee)
    {
        if (addressee instanceof FrontendService)
        {
            exec((FrontendService) addressee);
        } else {
            //todo error!
        }
    }

    public abstract void exec(FrontendService frontendService);
}