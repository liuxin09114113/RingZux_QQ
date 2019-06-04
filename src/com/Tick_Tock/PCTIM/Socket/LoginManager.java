package com.Tick_Tock.PCTIM.Socket;
import com.Tick_Tock.PCTIM.*;
import com.Tick_Tock.PCTIM.Package.*;
import com.Tick_Tock.PCTIM.Utils.*;
import java.util.*;
import com.Tick_Tock.PCTIM.Robot.*;
import com.Tick_Tock.PCTIM.Window.*;
import com.googlecode.lanterna.gui2.dialogs.*;

public class LoginManager extends Thread
{

	private byte[] data = null;
	public Udpsocket socket = null;
	public QQUser user;
	QQRobot robot;
	MessageService messageservice;
	HeartBeat heartbeat;

	private byte[] passwordmd5;

	private LuaQQRobot luarobot;

	private OutPutWindow processingloginoutputwindow;

	public void st(OutPutWindow _processingloginoutputwindow)
	{
		this.processingloginoutputwindow=_processingloginoutputwindow;
		this.start();
	}

	
	public boolean islogined()
	{
		if (this.user == null)
		{
			return false;
		}
		return this.user.islogined ;
	}

	private long qq;


	public void update(String p2)

	{
		System.out.println(p2);
		if (p2.equals("offline"))
		{
			Util.log("----------已掉线,程序重置----------");
			this.heartbeat.kill();
			this.messageservice.kill();
			this.Login();
		}


	}



	public LoginManager(long _qq, byte[] _passwordmd5)
	{
		this.qq = _qq;
		this.passwordmd5 = _passwordmd5;
		//user.TXProtocol.DwServerIP= "sz.tencent.com";

	}

	@Override public void run(){
		while(!this.islogined()){
		    this.Login();
		}
	}



	public void Login()
	{
		ParseRecivePackage parsereceive=null;
		try
		{
			this.user = new QQUser(this.qq, this.passwordmd5);
			socket = new Udpsocket(this.user);
			data = SendPackageFactory.get0825(this.user);

			socket.sendMessage(data);
			byte[] result = socket.receiveMessage();
			//System.out.println(Util.byte2HexString(result));
			parsereceive = new ParseRecivePackage(result, this.user.QQPacket0825Key, this.user);
			parsereceive.decrypt_body();
			parsereceive.parse_tlv();
			while (parsereceive.Header[0] == -2)
			{
				Util.log("重定向到:" + this.user.TXProtocol.DwRedirectIP);
				this.user.TXProtocol.WRedirectCount += 1;
				this.user.IsLoginRedirect = true;
				socket = new Udpsocket(this.user);

				data = SendPackageFactory.get0825(this.user);

				this.socket.sendMessage(data);

				result = this.socket.receiveMessage();
				parsereceive = new ParseRecivePackage(result, this.user.QQPacket0825Key, this.user);
				parsereceive.decrypt_body();
				parsereceive.parse_tlv();
			}

			Util.log("服务器连接成功,开始登陆");
			data = SendPackageFactory.get0836(this.user, false);
			socket.sendMessage(data);
			result = socket.receiveMessage();
			parsereceive = new ParseRecivePackage(result, this.user.TXProtocol.BufDhShareKey, this.user);
			parsereceive.parse0836();
			if (parsereceive.Header[0] == 52)
			{
				Util.log("密码错误");
				System.exit(100);
			}
			if (parsereceive.Header[0] == -5)
			{
				Util.log("需要验证码");
				while (parsereceive.Status == 0x1)
				{
					while (true)
					{//死循环获取验证码
						data = SendPackageFactory.get00ba(this.user, "");
						socket.sendMessage(data);
						result = socket.receiveMessage();
						parsereceive = new ParseRecivePackage(result, this.user.QQPacket00BaKey, this.user);
						parsereceive.parse00ba();
						if (Util.isvalidimg(this.user.QQPacket00BaVerifyCode))
						{
							break;//当验证码能够被显示时跳出循环
						}
					}
					
					String textimg = Util.gettextimg(this.user.QQPacket00BaVerifyCode);
					//此时验证码已获取，开始输入验证码
			
					String code = TextInputDialog.showDialog(this.processingloginoutputwindow.getTextGUI(), "输入验证码", textimg, "");
					if (code == null || code.isEmpty() || code.equals(""))
					{
						code = "TICK";
					}
					data = SendPackageFactory.get00ba(this.user, code);
					socket.sendMessage(data);
					result = socket.receiveMessage();
					parsereceive = new ParseRecivePackage(result, this.user.QQPacket00BaKey, this.user);
					parsereceive.parse00ba();
					if (parsereceive.Status != 0x0)
					{
						return;//验证失败结束方法，成功则继续执行
					}

				}


			}	


			while (parsereceive.Header[0] != 0)
			{
				Util.log("二次登陆");
				data = SendPackageFactory.get0836(this.user, true);
				socket.sendMessage(data);
				result = socket.receiveMessage();
				parsereceive = new ParseRecivePackage(result, this.user.TXProtocol.BufDhShareKey, this.user);
				parsereceive.parse0836();

				Thread.sleep(1000);

			}
			if (parsereceive.Header[0] == 0)
			{
				Util.log("成功获取用户信息: Nick: " + this.user.NickName + " Age: " + this.user.Age + " Sex: " + this.user.Gender);
				this.user.islogined = true;
				this.user.logintime = new Date().getTime();
				data = SendPackageFactory.get0828(this.user);
				socket.sendMessage(data);
				result = socket.receiveMessage(); 
				parsereceive = new ParseRecivePackage(result, this.user.TXProtocol.BufTgtGtKey, this.user);
				parsereceive.decrypt_body();
				parsereceive.parse_tlv();
				data = SendPackageFactory.get00ec(this.user, QQGlobal.Online);
				socket.sendMessage(data);
				result = socket.receiveMessage();
				parsereceive = new ParseRecivePackage(result, this.user.TXProtocol.SessionKey, this.user);
				parsereceive.decrypt_body();
				data = SendPackageFactory.get001d(this.user);
				socket.sendMessage(data);
				result = socket.receiveMessage();
				parsereceive = new ParseRecivePackage(result, this.user.TXProtocol.SessionKey, this.user);
				parsereceive.parse001d();
				if (this.robot == null)
				{
					this.robot = new QQRobot(this.socket, user);
				}
				if (this.luarobot == null)
				{
					this.luarobot = new LuaQQRobot(this.socket, user);
				}
				this.messageservice = new MessageService(user, this.socket, robot, luarobot);
				this.user.offline = false;
				this.messageservice.start();
				this.heartbeat = new HeartBeat(user, this);
				new Thread(heartbeat).start();
				
				this.processingloginoutputwindow.close();
				
				Util.write_property("account",String.valueOf(qq));
				Util.write_property("password", Util.byte2HexString(passwordmd5));
				
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}
}
