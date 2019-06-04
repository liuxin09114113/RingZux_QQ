package com.Tick_Tock.PCTIM.Window;

import com.googlecode.lanterna.gui2.*;
import java.util.*;
import com.googlecode.lanterna.gui2.dialogs.*;
import com.Tick_Tock.PCTIM.*;
import com.Tick_Tock.PCTIM.sdk.*;
import com.Tick_Tock.PCTIM.Utils.*;

public class GroupListWindow extends BasicWindow
{

	private Panel contentPanel;

	private Button button;

	private ChatWindow chatwindow;
	public GroupListWindow(String title,ChatWindow _chatwindow){
		super(title);
		this.chatwindow=_chatwindow;
		this.setHints(Arrays.asList(Window.Hint.FIXED_SIZE,Window.Hint.NO_POST_RENDERING));
		this.contentPanel = new Panel(new LinearLayout(Direction.VERTICAL)); // can hold multiple sub-components that will be added to a wind
        this.button=new Button("<---  --->");
		this.contentPanel.addComponent(this.button);
		this.setComponent(this.contentPanel);
		
	}

	
	
	
	
	
	public void setgrouplist(QQUser user)
	{
		if(user.friend_list!=null){
			for(Group_List.Group group:user.group_list.getall_group()){
				GroupListWindow.this.contentPanel.addComponent(new groupbutton().setchatwindow(this.chatwindow).setname(group.group_name).setuin(String.valueOf(group.group_uin)).setaction().settext());
			}

		}else{

		}
	}
}
class groupbutton extends Button{
	public groupbutton(){
		super("");
	}


	private String  groupuin;
	private String groupname;
	private ChatWindow chatwindow;

	public Component settext()
	{
		this.setLabel(" "+this.groupname+"   暂无");
		return this;
	}
	public groupbutton setuin(String _groupuin){
		this.groupuin=_groupuin;
		return this;
	}
	public groupbutton setname(String _name){
		this.groupname=_name;
		return this;

	}
	public groupbutton setchatwindow(ChatWindow _chatwindow){
		this.chatwindow=_chatwindow;
		return this;

	}
	public groupbutton setaction(){

		this.addListener(new Listener(){
				@Override public void onTriggered(Button button){
	
					chatwindow.onupdate(Long.parseLong(groupuin),2,groupname);
				}
			});
		return this;
	}



}
