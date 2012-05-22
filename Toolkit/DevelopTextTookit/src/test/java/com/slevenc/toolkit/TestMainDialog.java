package com.slevenc.toolkit;

import com.opensymphony.xwork2.util.finder.ClassFinder;
import com.opensymphony.xwork2.util.finder.ClassLoaderInterfaceDelegate;
import com.slevenc.toolkit.ui.MainDialog;

public class TestMainDialog {

	// @Test
	public void testIt() {

		try {
			ClassFinder cf = new ClassFinder(new ClassLoaderInterfaceDelegate(Thread.currentThread().getContextClassLoader()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		MainDialog md = new MainDialog();

		md.setVisible(true);
	}
}
