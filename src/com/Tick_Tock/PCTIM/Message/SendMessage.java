package com.Tick_Tock.PCTIM.Message;
import com.Tick_Tock.PCTIM.Socket.*;
import com.Tick_Tock.PCTIM.Package.*;
import com.Tick_Tock.PCTIM.*;
import com.Tick_Tock.PCTIM.sdk.*;
import com.Tick_Tock.PCTIM.Utils.*;

public class SendMessage
{


	public static void SendGroupMessage(QQUser user, Udpsocket socket, MessageFactory factory)
	{
		Util.log("[机器人] [群消息 发送] 到群 "+factory.Group_uin+" [消息] "+factory.Message);
		if (factory.message_type ==0||factory.message_type==1){
			byte[] data = SendPackageFactory.get0002(user,factory);
			socket.sendMessage(data);
		}else{
			
			byte[] data = SendPackageFactory.get0388(user,factory);
			socket.sendMessage(data);
		}
	}
	
	public static void SendFriendMessage(QQUser user, Udpsocket socket, MessageFactory factory)
	{
		Util.log("[机器人] [好友消息 发送] 到 "+factory.Friend_uin+" [消息] "+factory.Message);
		if (factory.message_type ==0||factory.message_type==1){
			byte[] data = SendPackageFactory.get00cd(user,factory);
			socket.sendMessage(data);
		}
	}
	
}
