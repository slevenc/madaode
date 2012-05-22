package com.slevenc.toolkits;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import com.slevenc.toolkit.Tookit;
import com.slevenc.toolkit.ui.annotation.Toolkit;

/**
 * url形式的地址转换
 * 
 * @author Slevenc
 * 
 */
@Toolkit
public class StringTrans implements Tookit {
	public String getName() {
		return "String转义";
	}

	public Icon getIcon() {
		return null;
	}

	public Component getComponent() {
		Box b = Box.createVerticalBox();
		JTextArea sourceTxt = new JTextArea(10, 60);
		JTextArea destTxt = new JTextArea(10, 60);
		
		sourceTxt.setLineWrap(true);
		destTxt.setLineWrap(true);
		
		JScrollPane sourceScroll = new JScrollPane(sourceTxt);
		sourceScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
		JScrollPane destScroll = new JScrollPane(destTxt);
		destScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
		
		Box buttonArea = Box.createHorizontalBox();
		buttonArea.add(Box.createHorizontalGlue());
		JButton toMapButton = new JButton("入字符串");
		toMapButton.addActionListener(new buttonAction(sourceTxt, destTxt, 1));
		buttonArea.add(toMapButton);
		JButton toMapMultiButton = new JButton("出字符串");
		toMapMultiButton.addActionListener(new buttonAction(sourceTxt, destTxt, 2));
		buttonArea.add(toMapMultiButton);
		
		b.add(sourceScroll);
		b.add(buttonArea);
		b.add(destScroll);
		return b;
	}

	public String getTip() {
		return "String转义";
	}

	
	
	/**
	 * 转换成能放进“”的样式
	 * 
	 * @param text
	 * @return
	 */
	private String toMap(String text) {
		text = text.replace("\\", "\\\\");
		text = text.replace("\"", "\\\"");
		text = text.replace("\t", "\\t");
		text = text.replace("\r", "\\r");
		text = text.replace("\n", "\\n");
		
		return text;
	}
	
	/**
	 * 转换成map多值的形式
	 * 
	 * @param text
	 * @return
	 */
	private String toMapMulti(String text) {
		text = text.replace("\\\"", "\"");
		text = text.replace("\\t", "\t");
		text = text.replace("\\r", "\r");
		text = text.replace("\\n", "\n");
		text = text.replace( "\\\\","\\");
		return text;
	}
	
	

	private class buttonAction implements ActionListener {

		private JTextArea sourceTxt = null;
		private JTextArea destTxt = null;
		private int type = 0;

		public buttonAction(JTextArea sourceTxt, JTextArea destTxt, int type) {
			this.sourceTxt = sourceTxt;
			this.destTxt = destTxt;
			this.type = type;
		}

		public void actionPerformed(ActionEvent e) {
			switch (this.type) {
			case 1:
				destTxt.setText(toMap(sourceTxt.getText()));
				break;
			case 2:
				destTxt.setText(toMapMulti(sourceTxt.getText()));
				break;
			}

		}

	}

}
