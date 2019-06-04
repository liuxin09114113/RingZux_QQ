package com.Tick_Tock.PCTIM.Socket;
import com.Tick_Tock.PCTIM.*;
import com.Tick_Tock.PCTIM.Package.*;
import java.util.*;
import com.Tick_Tock.PCTIM.Utils.*;




public class HeartBeat implements Runnable 
{


	private QQUser user = null;
	private long time_miles = 0;
	private Udpsocket socket = null;
	LoginManager manager;
	private boolean running=true;
	public HeartBeat(QQUser _user,LoginManager _manager){
		
		this.user = _user;
		this.manager=_manager;
		this.socket = this.manager.socket;
		
		
	}
	
	
	public void run(){
		while(this.running){
			try
			{
				if(!this.user.offline){
					this.user.offline=true;
				}else{
					//this.manager.update("offline");
			
					//break;
				}
				byte[] data = SendPackageFactory.get0058(this.user);
				time_miles  = new Date().getTime();
				socket.sendMessage(data);
				Thread.currentThread().sleep(20000);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		Util.log("----------心跳停止----------");
	}

	
	
	
	public void kill(){
		this.running = false;
		try
		{
			this.finalize();
		}
		catch (Throwable e)
		{e.printStackTrace();}
		
	}
	
}
