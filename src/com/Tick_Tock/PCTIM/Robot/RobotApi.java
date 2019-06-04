package com.Tick_Tock.PCTIM.Robot;
import com.Tick_Tock.PCTIM.sdk.*;
import com.Tick_Tock.PCTIM.Socket.*;
import com.Tick_Tock.PCTIM.*;
import com.Tick_Tock.PCTIM.Message.*;
import com.Tick_Tock.PCTIM.Utils.*;

public class RobotApi implements API{

private Udpsocket socket = null;
	private QQUser user = null;
	 
 public RobotApi(Udpsocket _socket,QQUser _user){
   this.user = _user;
   this.socket = _socket;
   Util.api=this;
 }
@Override
	public void SendGroupMessage(MessageFactory factory){

		SendMessage.SendGroupMessage(this.user,this.socket,factory);

	}
	@Override
	public void SendFriendMessage(MessageFactory factory){

		SendMessage.SendFriendMessage(this.user,this.socket,factory);

	}




}
