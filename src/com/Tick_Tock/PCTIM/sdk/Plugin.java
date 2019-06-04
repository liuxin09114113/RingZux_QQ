package com.Tick_Tock.PCTIM.sdk;

import com.Tick_Tock.PCTIM.Message.*;

	public interface Plugin {
	    String author();
		String Version();
		String name();
		
		void onLoad(API api);

		void onMessageHandler(QQMessage message);
	}

