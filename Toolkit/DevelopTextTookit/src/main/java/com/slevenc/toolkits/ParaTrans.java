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
public class ParaTrans implements Tookit {
	public String getName() {
		return "url参数转换";
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
		JButton toMapButton = new JButton("单值Map形式");
		toMapButton.addActionListener(new buttonAction(sourceTxt, destTxt, 1));
		buttonArea.add(toMapButton);
		JButton toMapMultiButton = new JButton("数组Map形式");
		toMapMultiButton.addActionListener(new buttonAction(sourceTxt, destTxt, 2));
		buttonArea.add(toMapMultiButton);
		
		b.add(sourceScroll);
		b.add(buttonArea);
		b.add(destScroll);
		return b;
	}

	public String getTip() {
		return "url参数提取";
	}

	
	
	/**
	 * 转换成map单值的形式
	 * 
	 * @param text
	 * @return
	 */
	private String toMap(String text) {
		StringBuilder sb = new StringBuilder();
		Matcher mm = Pattern.compile(".*\\?(.+)").matcher(text);
		if(mm.find()){
			text = mm.group(1);
		}
		
		if (text.contains("&")) {
			Pattern p = Pattern.compile("([^=]+)=(.*)");
			Matcher matcher = null;
			String[] parts = text.split("&");
			for (String part : parts) {
				matcher = p.matcher(part);
				if (matcher.find()) {
					sb.append("map.put(\"").append(matcher.group(1)).append("\",\"").append(matcher.group(2)).append("\");\n");
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * 转换成map多值的形式
	 * 
	 * @param text
	 * @return
	 */
	private String toMapMulti(String text) {
		StringBuilder sb = new StringBuilder();
		Matcher mm = Pattern.compile(".*\\?(.+)").matcher(text);
		if(mm.find()){
			text = mm.group(1);
		}
		Map<String,List<String>> valueMap = new HashMap<String,List<String>>();
		List<String> values = null;
		if (text.contains("&")) {
			Pattern p = Pattern.compile("([^=]+)=(.*)");
			Matcher matcher = null;
			String[] parts = text.split("&");
			for (String part : parts) {
				matcher = p.matcher(part);
				if (matcher.find()) {
					if(valueMap.containsKey(matcher.group(1))){
						values = valueMap.get(matcher.group(1));
					}else{
						values = new ArrayList<String>();
						valueMap.put(matcher.group(1), values);
					}
					values.add(matcher.group(2));
				}
			}
		}
		for(Entry<String, List<String>> en:valueMap.entrySet()){
			sb.append("map.put(\"").append(en.getKey()).append("\",new String[]{");
			for(String v:en.getValue()){
				sb.append("\"").append(v).append("\",");
			}
			if(sb.toString().endsWith(",")){
				sb.setLength(sb.length()-1);
			}
			sb.append("});\n");
		}
		return sb.toString();
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
