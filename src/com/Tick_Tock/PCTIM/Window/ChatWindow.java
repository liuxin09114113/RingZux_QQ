package com.Tick_Tock.PCTIM.Window;
import com.googlecode.lanterna.gui2.*;
import java.util.*;
import com.googlecode.lanterna.gui2.dialogs.*;
import com.googlecode.lanterna.*;
import com.Tick_Tock.PCTIM.sdk.*;
import com.googlecode.lanterna.input.*;
import com.Tick_Tock.PCTIM.Message.*;
import com.Tick_Tock.PCTIM.Utils.*;

public class ChatWindow extends BasicWindow
{

	private Panel contentPanel;
	private TextBox textbox;
	private Label label;
	private Button button;

	public int chattype=0;

	public Long uin=0l;

	private TextBox inputmessagebox;
	

	public ChatWindow(String title){
		super(title);
		this.setHints(Arrays.asList(Window.Hint.FIXED_SIZE,Window.Hint.NO_POST_RENDERING));

		this.button=new Button("<---  --->");

		this.contentPanel = new Panel(new LinearLayout(Direction.VERTICAL)); // can hold multiple sub-components that will be added to a windo
		this.contentPanel.addComponent(this.button);
		this.textbox = new TextBox();
		this.label=new Label("");
		this.button=new Button(" 发送",new Runnable(){
			public void run(){
				if(!ChatWindow.this.inputmessagebox.getText().equals("")){
		        Util.SendMessage(ChatWindow.this.chattype,ChatWindow.this.uin,ChatWindow.this.inputmessagebox.getText());
				ChatWindow.this.inputmessagebox.setText("");
				ChatWindow.this.handleInput(new KeyStroke(KeyType.ArrowUp));
				ChatWindow.this.handleInput(new KeyStroke(KeyType.ArrowUp));
				}
			}
		});
		this.inputmessagebox = new TextBox();
		com.googlecode.lanterna.gui2.TextBox.TextBoxRenderer tbr = this.textbox.getRenderer();
		com.googlecode.lanterna.gui2.TextBox.DefaultTextBoxRenderer dtbr = (TextBox.DefaultTextBoxRenderer) tbr;
	    dtbr.setHideScrollBars(true);
		this.contentPanel.addComponent(this.label);
		this.contentPanel.addComponent(this.inputmessagebox);
		this.contentPanel.addComponent(this.button);
		this.contentPanel.addComponent(this.textbox.setReadOnly(true));
		
		this.setComponent(this.contentPanel);
	}

	



	public void onupdate(Long _uin,int _chattype,String targetname){
		this.label.setText(targetname);
		this.chattype=_chattype;
		this.uin=_uin;
		this.textbox.setText("");
	}

	public void onself(QQMessage message)
	{
		if(this.getacturesizeoftext(message.Message+message.SendName)>this.textbox.getPreferredSize().getColumns()){
			this.cutmessage2(message);
			this.handleInput(new KeyStroke(KeyType.ArrowDown));
			return;
		}
		this.textbox.addLine(stringreader.createblank(this.textbox.getPreferredSize().getColumns()-(this.getacturesizeoftext(message.Message+" :"+message.SendName)))+message.Message+" :"+message.SendName);
		this.handleInput(new KeyStroke(KeyType.ArrowDown));
	}

	private void cutmessage2(QQMessage message)
	{
		int textboxwidth = this.textbox.getPreferredSize().getColumns();
		String head =" :"+message.SendName;
		int headersize = getacturesizeoftext(head);
		int messagesize = textboxwidth-headersize-this.textbox.getPreferredSize().getColumns()/5;
		stringreader reader = new stringreader(message.Message);
		this.textbox.addLine(reader.createblank(this.textbox.getPreferredSize().getColumns()/5)+reader.readstring(messagesize)+head);
		while(!reader.isover()){
			this.textbox.addLine(reader.createblank(this.textbox.getPreferredSize().getColumns()/5)+reader.readstring(messagesize));
		}
	}

	public void onothers(QQMessage message){
		if(this.chattype==1){
		    message.SendName=this.label.getText();
		}
		if(this.getacturesizeoftext(message.Message+message.SendName)>this.textbox.getPreferredSize().getColumns()){
			this.cutmessage(message);
			this.handleInput(new KeyStroke(KeyType.ArrowDown));
			return;
		}
		this.textbox.addLine(message.SendName+": "+message.Message);
		this.handleInput(new KeyStroke(KeyType.ArrowDown));
	}
	
	private void cutmessage(QQMessage message)
	{
		int textboxwidth = this.textbox.getPreferredSize().getColumns();
		String head =message.SendName+": ";
		int headersize = getacturesizeoftext(head);
		int messagesize = textboxwidth-headersize-this.textbox.getPreferredSize().getColumns()/5;
		stringreader reader = new stringreader(message.Message);
		this.textbox.addLine(head+reader.readstring(messagesize));
		while(!reader.isover()){
			this.textbox.addLine(reader.createblank(headersize)+reader.readstring(messagesize));
		}
	}


	private int getacturesizeoftext(String text){
		int length=0;
		for(int i=0;i<text.length();i+=1){
			int onelength = String.valueOf(text.charAt(i)).getBytes().length;
			if(onelength==1){
				length+=1;
			}else if (onelength==3){
				length+=2;
			}
			
		}
		return length;
	}
	
	
	public void setchatboxsize(){
	    this.textbox.setPreferredSize(new TerminalSize(this.getSize().getColumns(),this.getSize().getRows()-5));
	
	}
}

class stringreader
{

	private char[] chartext;
	private int possition=0;
	
	public stringreader(String str){
		this.chartext=str.toCharArray();
	}
	
	
	public String readstring(int length){
		String toreturn="";
		int lengthcount=0;
		int startlength =0;
		 for(int i=0;i<length;i+=1){
			 if(i>=this.chartext.length){
				 break;
			 }
			 String str= String.valueOf(this.chartext[i]);
			 if(str.getBytes().length==1){
				 toreturn+=str;
				 lengthcount+=1;
			 }
			 else  if(str.getBytes().length==3){
				 toreturn+=str;
				 lengthcount+=2;
			 }
			 startlength+=1;
			 if( lengthcount>=length||this.chartext.length<startlength){
				 break;
			 }
			 
		 }
		 this.chartext=this.subByte(this.chartext,startlength,this.chartext.length-startlength);
		 return toreturn;
	}

	
	
	public boolean isover(){
	    if(this.chartext.length==0){
			return true;
		}
		return false;
	}
	
	public static String createblank(int length){
		return String.format("%0"+length+"d", 0).replaceAll("0"," ");
	}
	
	
	public char[] subByte(char[] b, int off, int length)
	{

		if (b.length != 0 && b != null)
		{
			char[] b1 = new char[length];
			System.arraycopy(b, off, b1, 0, length);
			return b1;
		}
		return new char[0];


	}

	
	
}
