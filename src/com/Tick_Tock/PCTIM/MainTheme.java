package com.Tick_Tock.PCTIM;
import com.googlecode.lanterna.graphics.*;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.*;

public class MainTheme extends DelegatingThemeDefinition
{
	
	public MainTheme(ThemeDefinition definition) {
		super(definition);
		
	}
	
	
	@Override 
	public ThemeStyle getActive() {
		DefaultMutableThemeStyle mutableThemeStyle = new DefaultMutableThemeStyle(super.getActive());
		return mutableThemeStyle.setBackground(TextColor.Factory.fromString("CYAN"));
	}
	
	
}
