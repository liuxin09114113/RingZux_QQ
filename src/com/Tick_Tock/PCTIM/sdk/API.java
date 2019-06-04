package com.Tick_Tock.PCTIM.sdk;
import com.Tick_Tock.PCTIM.Message.*;


public interface API {
	public void SendGroupMessage(MessageFactory factory);
	
	public void SendFriendMessage(MessageFactory factory);
}
