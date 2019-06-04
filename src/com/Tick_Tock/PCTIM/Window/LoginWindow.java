package com.Tick_Tock.PCTIM.Window;
import com.googlecode.lanterna.gui2.*;
import java.util.*;
import com.googlecode.lanterna.gui2.dialogs.*;

public class LoginWindow extends BasicWindow
{

	private Panel contentPanel;

	private TextBox accountinput;

	private TextBox passwordinput;

	public LoginWindow(String title){
		super(title);
		this.setHints(Arrays.asList(Window.Hint.FULL_SCREEN,Window.Hint.NO_POST_RENDERING));


		this.contentPanel = new Panel(new GridLayout(2)); // can hold multiple sub-components that will be added to a window
		this.accountinput = new TextBox();
		this.passwordinput = new TextBox();
		
		contentPanel.addComponent(new Label("QQ"));
		this.contentPanel.addComponent(this.accountinput).setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.END, GridLayout.Alignment.CENTER));
		contentPanel.addComponent(new Label("password"));
		this.contentPanel.addComponent(this.passwordinput).setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.END, GridLayout.Alignment.CENTER));
		
		this.contentPanel.addComponent(new Button("Login", new Runnable() {
											   @Override
											   public void run() {
												   if(LoginWindow.this.account()==null||LoginWindow.this.account()==null){
													   MessageDialog.showMessageDialog(LoginWindow.this.getTextGUI(), "错误", "账号密码不能为空", MessageDialogButton.OK);
												   }
												   else{
													   LoginWindow.this.close();
												   }
											   }
										   }).setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.END, GridLayout.Alignment.CENTER)));
		
		this.setComponent(contentPanel);

	}

	
	public String account(){
		return this.accountinput.getText();
	}


	public String password(){
		return this.passwordinput.getText();
	}
}
