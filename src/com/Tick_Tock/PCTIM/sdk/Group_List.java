package com.Tick_Tock.PCTIM.sdk;
import java.util.*;

public class Group_List
{
	public List<Group> joined_group =new ArrayList<Group>();
	
	public List<Group> managed_group =new ArrayList<Group>();
	
	public List<Group> created_group =new ArrayList<Group>();
	
	public List<Group> getall_group(){
		List<Group> all = new ArrayList<Group>();
		all.addAll(joined_group);
		all.addAll(managed_group);
		all.addAll(created_group);
		return all;
	}
	
	public Group getgroupobj(){
		return new Group();
	}
	
	public class Group{
		public String group_name;
		public long group_uin;
		public long owner_uin;

		public Group set_group_name(String name){
			this.group_name=name;
			return this;
		}
		public Group set_group_uin(long uin){
			this.group_uin=uin;
			return this;
		}
		public Group set_owner_uin(long uin){
			this.owner_uin=uin;
			return this;
		}
}




}
