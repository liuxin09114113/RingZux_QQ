package com.Tick_Tock.PCTIM.Robot;
import com.Tick_Tock.PCTIM.Socket.*;
import com.Tick_Tock.PCTIM.*;
import java.io.*;
import java.util.*;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.jse.*;
import com.Tick_Tock.PCTIM.Utils.*;
import com.Tick_Tock.PCTIM.sdk.*;

public class LuaQQRobot
{
	private Udpsocket socket = null;
	private QQUser user = null;
	private RobotApi api;
	private String exact_directory;
	private Map<String,LuaValue> plugins = new HashMap<String,LuaValue>();

	public LuaQQRobot(Udpsocket _socket, QQUser _user)
	{
		this.socket = _socket;
		this.user = _user;
		this.api = new RobotApi(this.socket, this.user);
		File directory = new File("");

		try
		{
			this.exact_directory  = directory.getCanonicalPath();
			File plugin_path = new File(exact_directory + "/plugin-lua");
			String[] plugin_list = plugin_path.list();
			if (plugin_list != null)
			{
				List<String> list = Arrays.asList(plugin_list);
				Globals globals = JsePlatform.standardGlobals();
				final LuaValue luaapi = CoerceJavaToLua.coerce(api);
				final LuaValue luathis = CoerceJavaToLua.coerce(this);
				
				for (String file: list)
				{
					if (file.endsWith(".lua"))
					{
						String script_path=this.exact_directory + "/plugin-lua/" + file;
						LuaValue plugin = globals.loadfile(script_path).call();
						final LuaValue load = plugin.get(LuaValue.valueOf("load"));
						final LuaValue name = plugin.get(LuaValue.valueOf("name"));
						this.plugins.put(name.call().toString(), plugin);
						new Thread(){
							@Override public void run()
							{
								load.call(luaapi,luathis);
							}
						}.start();
						Util.log("[Lua插件] 加载成功 [插件名]: " + name.call().tostring());
					}
				}
			}
		}
		catch (Exception e)
		{
			System.out.println(e.toString());
		}

	}

	public void call(final QQMessage qqmessage)
	{
		final LuaValue luamsg = CoerceJavaToLua.coerce(qqmessage);
		for (final LuaValue plugin:plugins.values())
		{
			new Thread(){
				public void run()
				{
					LuaValue onqqmessage = plugin.get(LuaValue.valueOf("onqqmessage"));
					onqqmessage.call(luamsg);
				}
			}.start();
		}
	}
	public httpentry gethttpentry(String url){
		return new httpentry(url);
	}
	

	public void reload(String plugin_name)
	{
		plugins.remove(plugin_name);
		File directory = new File("");
		try
		{
			this.exact_directory  = directory.getCanonicalPath();
		}
		catch (IOException e)
		{}
		File plugin_path = new File(exact_directory + "/plugin-lua");
		String[] plugin_list = plugin_path.list();
		if (plugin_list != null)
		{
			List<String> list = Arrays.asList(plugin_list);
			Globals globals = JsePlatform.standardGlobals();
			final LuaValue luaapi = CoerceJavaToLua.coerce(api);
			final LuaValue luathis = CoerceJavaToLua.coerce(this);
			
			for (String file: list)
			{
				if (file.endsWith(".lua"))
				{
					String script_path=this.exact_directory + "/plugin-lua/" + file;
					LuaValue plugin = globals.loadfile(script_path).call();
					final LuaValue load = plugin.get(LuaValue.valueOf("load"));
					final LuaValue name = plugin.get(LuaValue.valueOf("name"));
					if(name.call().toString().equals(plugin_name)){
						this.plugins.put(name.call().toString(), plugin);
						new Thread(){
							@Override public void run()
							{
								load.call(luaapi,luathis);
							}
						}.start();
						Util.log("[Lua插件] 加载成功 [插件名]: " + name.call().tostring());
					}
				}
			}
		}

	}
}
