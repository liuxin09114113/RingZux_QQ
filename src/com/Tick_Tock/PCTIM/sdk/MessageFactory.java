package com.Tick_Tock.PCTIM.sdk;

public class MessageFactory
{
	public int message_type;
	public long Group_uin;
	public String Message;
 public long Friend_uin;
	public String key30;

	public String key48;
	public MessageFactory(){
		
	}
	
	public MessageFactory setgroupuin(long uin){
		this.Group_uin=uin;
		return this;
	}
	
	public MessageFactory setfrienduin(long uin){
		this.Friend_uin=uin;
		return this;
	}
	
	public MessageFactory setmessage(String message){
		this.Message=message;
		return this;
	}
	
}
