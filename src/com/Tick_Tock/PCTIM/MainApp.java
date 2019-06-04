package com.Tick_Tock.PCTIM;

import com.Tick_Tock.PCTIM.Socket.*;
import com.Tick_Tock.PCTIM.Utils.*;
import com.Tick_Tock.PCTIM.Window.*;
import com.googlecode.lanterna.*;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.*;
import com.googlecode.lanterna.terminal.*;
import com.googlecode.lanterna.terminal.ansi.*;
import javax.net.ssl.*;
import com.googlecode.lanterna.gui2.dialogs.*;
import com.googlecode.lanterna.input.*;
import java.util.concurrent.atomic.*;
import com.googlecode.lanterna.graphics.*;

public class MainApp extends Thread
{
	private Terminal term = null;

	private Screen screen = null;

	private TerminalSize size;

	private MultiWindowTextGUI textGUI;

	private ChatWindow chatwindow;

	private FriendListWindow friendlistwindow;

	private GroupListWindow grouplistwindow;

	private OutPutWindow logoutputwindow;

	private listener listener;

	private QQUser user;
	
	
	@Override public void run(){
		
		try
		{
			Util.trustAllHttpsCertificates();

			HttpsURLConnection.setDefaultHostnameVerifier(Util.hv);

            // Swing is the default backend on Windows unless explicitly
            // overridden by jexer.Swing.

			this.initterminal();
			this.startloginprocess();
			this.initwindows();
			this.initlistener();
			this.addallllistenertowindow();
			this.setallwindowsize();
			this.addallwindowtoui();
			this.setallwindowposition();
			this.textGUI.setTheme(new DelegatingTheme(this.textGUI.getTheme()) {
					@Override
					public ThemeDefinition getDefinition(Class<?> clazz) {
						ThemeDefinition themeDefinition = super.getDefinition(clazz);
						return new MainTheme(themeDefinition);
					}
				});
			Util.chatwindow=chatwindow;
			textGUI.setActiveWindow(friendlistwindow);

			new qqinfo().setuser(this.user).setfriendwindow(friendlistwindow).setgroupwindow(grouplistwindow).start();

			Util.output=logoutputwindow;
			
			textGUI.waitForWindowToClose(friendlistwindow);



        }
		catch (Exception e)
		{
            e.printStackTrace();
			System.exit(-0);
        }





	}

	private void addallllistenertowindow()
	{
		this.friendlistwindow.addWindowListener(this.listener);
		this.grouplistwindow.addWindowListener(this.listener);
		this.chatwindow.addWindowListener(this.listener);
		
	}

	private void setallwindowposition()
	{
		friendlistwindow.setPosition(new TerminalPosition(0,0));
		grouplistwindow.setPosition(new TerminalPosition(size.getColumns()/3,0));
		chatwindow.setPosition(new TerminalPosition((size.getColumns()/3)*2,0));
		logoutputwindow.setPosition(new TerminalPosition(0,size.getRows()/2));
		
	}

	private void addallwindowtoui()
	{
		this.textGUI.addWindow(this.friendlistwindow);
		this.textGUI.addWindow(this.grouplistwindow);
		this.textGUI.addWindow(this.chatwindow);
		this.textGUI.addWindow(this.logoutputwindow);
		
	}

	private void setallwindowsize()
	{
		this.friendlistwindow.setSize(new TerminalSize(size.getColumns()/3-2,size.getRows()/2-2));
		this.grouplistwindow.setSize(new TerminalSize(size.getColumns()/3-2,size.getRows()/2-2));
		this.chatwindow.setSize(new TerminalSize(size.getColumns()/3-2,size.getRows()/2-2));
		this.logoutputwindow.setSize(new TerminalSize(size.getColumns()-2,size.getRows()/2-2));
		this.logoutputwindow.setlogsize(new TerminalSize(size.getColumns()-3,size.getRows()/2-3));
		this.chatwindow.setchatboxsize();
	}

	private void startloginprocess()
	{
		QQUser user = null ;
		String qq =Util.read_property("account");
		String password = Util.read_property("password");
		byte[] passwordmd5=new byte[0];
		LoginManager manager = null ;
		if (qq == null || password == null)
		{
			LoginWindow loginwindow = new LoginWindow("登录");
			textGUI.addWindow(loginwindow);
			loginwindow.waitUntilClosed();
			qq=loginwindow.account();
			passwordmd5=Util.MD5(loginwindow.password());
			user = new QQUser(Long.parseLong(qq), passwordmd5);
			manager = new LoginManager(Long.parseLong(qq), passwordmd5);

		}
		else
		{
			passwordmd5 = Util.str_to_byte(password);
			user = new QQUser(Long.parseLong(qq), passwordmd5);
			manager = new LoginManager(Long.parseLong(qq), passwordmd5);


		}

		OutPutWindow processingloginoutputwindow = new OutPutWindow("登录日志");
		processingloginoutputwindow.setSize(new TerminalSize(size.getColumns()-2,size.getRows()-2));
		processingloginoutputwindow.setlogsize(new TerminalSize(size.getColumns()-3,size.getRows()-3));

		this.textGUI.addWindow(processingloginoutputwindow);
		processingloginoutputwindow.setPosition(new TerminalPosition(0,0));
		Util.output=processingloginoutputwindow;
		manager.st(processingloginoutputwindow);
		processingloginoutputwindow.waitUntilClosed();
		this.textGUI.removeWindow(processingloginoutputwindow);
		this.user=manager.user;
	}

	private void initlistener()
	{
		this.listener = new listener();
		listener.setchatwindow(this.chatwindow).setfriendwindow(this.friendlistwindow).setgroupwindow(this.grouplistwindow).settextgui(this.textGUI);
	}

	private void initwindows()
	{
		this.chatwindow = new ChatWindow("聊天");
		this.friendlistwindow = new FriendListWindow("好友列表",chatwindow);
		this.grouplistwindow = new GroupListWindow("群列表",chatwindow);
		this.logoutputwindow = new OutPutWindow("日志");
		
	}

	private void initterminal() throws Exception
	{
		this.term = new UnixTerminal();
		this.screen = new TerminalScreen(term);
		this.screen.startScreen();
		this.textGUI = new MultiWindowTextGUI(screen);
		this.size = screen.getTerminalSize();
	}

}

class listener implements WindowListener
{

	private ChatWindow chatwindow;

	private GroupListWindow groupwindow;

	private FriendListWindow friendwindow;

	private MultiWindowTextGUI ui;

	public listener setchatwindow(ChatWindow _window)
	{
		this.chatwindow=_window;
		return this;
	}
	public listener setgroupwindow(GroupListWindow _window)
	{
		this.groupwindow=_window;
		return this;
	}
	public listener setfriendwindow(FriendListWindow _window)
	{
		this.friendwindow=_window;
		return this;
	}
	public listener settextgui(MultiWindowTextGUI _ui)
	{
		this.ui=_ui;
		return this;
	}

	@Override
	public void onInput(Window p1, KeyStroke p2, AtomicBoolean p3)
	{
		if(p2.getKeyType()==KeyType.ArrowLeft){
			if(p1 instanceof FriendListWindow){
				
			}else if(p1 instanceof GroupListWindow){
				this.ui.setActiveWindow(this.friendwindow);
			}else if(p1 instanceof ChatWindow){
				this.ui.setActiveWindow(this.groupwindow);
			}
		}else if(p2.getKeyType()==KeyType.ArrowRight){
			if(p1 instanceof FriendListWindow){
				this.ui.setActiveWindow(this.groupwindow);
			}else if(p1 instanceof GroupListWindow){
				this.ui.setActiveWindow(this.chatwindow);
			}else if(p1 instanceof ChatWindow){
				
			}
		}
		
		
	}

	@Override
	public void onUnhandledInput(Window p1, KeyStroke p2, AtomicBoolean p3)
	{
		
	}

	@Override
	public void onResized(Window p1, TerminalSize p2, TerminalSize p3)
	{
		
	}

	@Override
	public void onMoved(Window p1, TerminalPosition p2, TerminalPosition p3)
	{
		
	}
	

	
	

	
}




class qqinfo extends Thread
{

	private QQUser user;

	private FriendListWindow friendlistwindow;

	private GroupListWindow grouplistwindow;

	public qqinfo setuser(QQUser _user){
		this.user=_user;
		return this;
	}
	public qqinfo setfriendwindow(FriendListWindow window){
		this.friendlistwindow=window;
		return this;
	}

	public qqinfo setgroupwindow(GroupListWindow window){
		this.grouplistwindow=window;
		return this;
	}
	@Override public void run(){
		Util.getquncookie(this.user);
		Util.getqunlist(this.user);
		Util.getfriendlist(this.user);
		this.friendlistwindow.setfriendlist(this.user);
		this.grouplistwindow.setgrouplist(this.user);
	}
}

