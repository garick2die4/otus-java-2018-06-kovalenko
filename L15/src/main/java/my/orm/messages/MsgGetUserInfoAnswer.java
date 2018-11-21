package my.orm.messages;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import my.orm.app.FrontendService;
import my.orm.datasets.UserDataSet;
import my.orm.messageSystem.Address;

public class MsgGetUserInfoAnswer extends MsgToFrontend
{
	private final static Logger LOGGER = LoggerFactory.getLogger(MsgGetUserInfoAnswer.class);
	
	private final int sessionId;
	private final UserDataSet userInfo;
	
	public MsgGetUserInfoAnswer(Address from, Address to, int sessionId, UserDataSet userInfo)
	{
		super(from, to);

        this.sessionId = sessionId;
        this.userInfo = userInfo;
	}

    @Override
    public void exec(FrontendService frontendService)
    {
    	LOGGER.info("MsgGetUserInfoAnswer sessionId: {}, userInfo: {}", sessionId, userInfo);
    	
    	List<String> phones = userInfo.getPhones().stream().map(p -> p.getNumber()).collect(Collectors.toList());
        frontendService.sendUserInfo(sessionId,
    		userInfo.getId(),
    		userInfo.getName(),
    		userInfo.getAge(),
    		userInfo.getAddress().getStreet(),
    		phones);
    }
}
