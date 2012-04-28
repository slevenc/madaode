package com.slevenc.ninatimeline;

import android.graphics.Bitmap;

/**联系人对象
 * @author Slevenc
 *
 */
public class Contact {
	/**
	 *联系人ID 
	 */
	private String id;
	/**
	 * 联系人姓名
	 */
	private String name;
	/**
	 * 电话
	 */
	private String number;
	/**
	 * 头像
	 */
	private Bitmap head;
	
	
	public Bitmap getHead() {
		return head;
	}
	public void setHead(Bitmap head) {
		this.head = head;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
}
