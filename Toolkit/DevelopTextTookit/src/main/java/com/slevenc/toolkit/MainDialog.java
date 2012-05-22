package com.slevenc.toolkit;

import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class MainDialog extends JFrame {
	
	
	public MainDialog(){
		init();
		
	}
	
	private void init(){
		
		JTabbedPane tabPane = new JTabbedPane();
		tabPane.insertTab("tab1", null, new Container(), "tab1", 0);
		tabPane.insertTab("tab2", null, new Container(), "tab2", 0);
		
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().add(tabPane);
		this.pack();
	}

}
