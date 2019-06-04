package com.Tick_Tock.PCTIM.Window;
import com.googlecode.lanterna.gui2.*;
import java.util.*;
import com.googlecode.lanterna.*;
import com.googlecode.lanterna.input.*;

public class OutPutWindow extends BasicWindow
{

	private TextBox textbox;

	private Panel contentPanel;


	public OutPutWindow(String title){
		super(title);
		this.setHints(Arrays.asList(Window.Hint.FIXED_SIZE,Window.Hint.NO_POST_RENDERING));

		this.contentPanel = new Panel(new GridLayout(2)); // can hold multiple sub-components that will be added to a window
		this.textbox = new TextBox();

		this.contentPanel.addComponent(this.textbox);
		com.googlecode.lanterna.gui2.TextBox.TextBoxRenderer tbr = this.textbox.getRenderer();
		com.googlecode.lanterna.gui2.TextBox.DefaultTextBoxRenderer dtbr = (TextBox.DefaultTextBoxRenderer) tbr;
	    dtbr.setHideScrollBars(true);
		

		this.setComponent(contentPanel);


	}

	public void setlogsize(TerminalSize size){
		this.textbox.setPreferredSize(size);
		

	}

	public void print(String text){
		this.textbox.addLine(text);

		
		this.handleInput(new KeyStroke(KeyType.ArrowDown));
	}



}

