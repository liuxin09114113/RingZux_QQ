package com.Tick_Tock.PCTIM.sdk;
import java.util.*;

public class Friend_List
{
	public List<Friend> members =new ArrayList<Friend>();


	public Friend getfriendobj(){
		return new Friend();
	}

	public class Friend{
		public String friend_name;
		public long friend_uin;

		public Friend set_friend_name(String name){
			this.friend_name=name;
			return this;
		}
		
		public Friend set_friend_uin(long uin){
			this.friend_uin=uin;
			return this;
		}
		
	}
}
