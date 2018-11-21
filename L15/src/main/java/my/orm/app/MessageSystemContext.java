package my.orm.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import my.orm.messageSystem.Address;
import my.orm.messageSystem.Addressee;
import my.orm.messageSystem.MessageSystem;

/**
 * Created by tully.
 */
@Service
public class MessageSystemContext
{
    private final MessageSystem messageSystem;

    private Address frontAddress;
    private Address dbAddress;

    @Autowired
    public MessageSystemContext(MessageSystem messageSystem)
    {
    	this.messageSystem = messageSystem;
    }
    
    public MessageSystem getMessageSystem()
    {
        return messageSystem;
    }

    public Address getFrontAddress()
    {
        return frontAddress;
    }

    public void setFrontAddress(Addressee addressee)
    {
        this.frontAddress = addressee.getAddress();
        messageSystem.addAddressee(addressee);
    }

    public Address getDbAddress()
    {
        return dbAddress;
    }

    public void setDbAddress(Addressee addressee)
    {
        this.dbAddress = addressee.getAddress();
        messageSystem.addAddressee(addressee);
    }
}

