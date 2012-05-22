package com.slevenc.toolkit.ui;

import java.awt.Container;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import com.opensymphony.xwork2.util.finder.ClassFinder;
import com.opensymphony.xwork2.util.finder.ClassLoaderInterfaceDelegate;
import com.slevenc.toolkit.Tookit;
import com.slevenc.toolkit.ui.annotation.Toolkit;

public class MainDialog extends JFrame {

	public MainDialog() {
		init();

	}

	private void init() {

		JTabbedPane tabPane = new JTabbedPane();
		addToTab(tabPane);

		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().add(tabPane);
		this.pack();
	}

	/**
	 * 载入符合的类
	 * 
	 * @return
	 */
	private List<Class> loadToolKit() {
		List<Class> toolkitClazz = new ArrayList<Class>();
		try {
			ClassFinder cf = new ClassFinder(new ClassLoaderInterfaceDelegate(Thread.currentThread().getContextClassLoader()));
			List<Class> clazz = cf.findAnnotatedClasses(Toolkit.class);
			for (int i = 0; i < clazz.size(); i++) {
				if (!clazz.get(i).isInterface()) {
					Class[] interfaces = clazz.get(i).getInterfaces();
					for (Class interf : interfaces) {
						if (interf.equals(com.slevenc.toolkit.Tookit.class)) {
							// 符合 添加之
							toolkitClazz.add(clazz.get(i));
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return toolkitClazz;
	}

	private void addToTab(JTabbedPane tabPane) {
		List<Class> cl = loadToolKit();
		Class c = null;
		for (int i = 0; i < cl.size(); i++) {
			c = cl.get(i);
			Constructor[] constructors = c.getConstructors();
			for (Constructor con : constructors) {
				try {
					Tookit tk = (Tookit) con.newInstance();
					tabPane.insertTab(tk.getName(), tk.getIcon(), tk.getComponent(), tk.getTip(), 0);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

}
