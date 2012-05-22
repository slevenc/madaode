package com.slevenc.toolkit;

import java.awt.Component;

import javax.swing.Icon;

/**小工具窗体接口
 * @author Slevenc
 * 
 */
public interface Tookit {

	/**获取tab名称
	 * @return
	 */
	public String getName();

	/**获取图标
	 * @return
	 */
	public Icon getIcon();

	/**获取子组件
	 * @return
	 */
	public Component getComponent();

	/**获取提示
	 * @return
	 */
	public String getTip();

}
