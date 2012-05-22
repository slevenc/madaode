package com.slevenc.toolkits;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import com.slevenc.toolkit.Tookit;
import com.slevenc.toolkit.ui.annotation.Toolkit;

/**
 * @author Slevenc
 * 
 */
@Toolkit
public class UrlDecoder implements Tookit {

	public String getName() {
		return "url解码";
	}

	public Icon getIcon() {
		return null;
	}

	public Component getComponent() {
		return genComponent();
	}

	public String getTip() {
		return null;
	}

	/**
	 * 生成控件方法
	 * 
	 * @return
	 */
	private Component genComponent() {
		Box b = Box.createVerticalBox();

		JTextArea sourceText = new JTextArea("", 20, 60);
		sourceText.setLineWrap(true);
		JScrollPane jsp = new JScrollPane(sourceText);
		jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
		b.add(jsp);
		Box buttonBox = Box.createHorizontalBox();
		buttonBox.add(Box.createHorizontalGlue());
		JButton jb = new JButton("解码");
		jb.addActionListener(new urlDecoderAction(sourceText));
		buttonBox.add(jb);
		b.add(buttonBox);
		return b;
	}

	/**响应事件
	 * @author Slevenc
	 *
	 */
	private class urlDecoderAction implements ActionListener {
		private JTextArea ta = null;

		public urlDecoderAction(JTextArea sourceText) {
			ta = sourceText;
		}

		public void actionPerformed(ActionEvent e) {
			String text = ta.getText();
			URLDecoder ud = new URLDecoder();
			try {
				ta.setText(ud.decode(text, "utf-8"));
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}

		}

	}

}
