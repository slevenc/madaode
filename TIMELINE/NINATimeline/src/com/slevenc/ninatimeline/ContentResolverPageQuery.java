package com.slevenc.ninatimeline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * 分页查询实现
 * 
 * @author Slevenc
 * 
 */
public class ContentResolverPageQuery {

	private static final Logger logger = Logger.getLogger("ContentResolverPageQuery");

	public ContentResolverPageQuery(Context context) {
		this.context = context;
	}

	Context context = null;

	/**
	 * 多参数查询
	 * 
	 * @param sql
	 * @param values
	 * @return
	 */
	public List<Map<String, String>> inquire(String sql, String[] values) {

		return null;
	}

	/**
	 * 单个参数查询
	 * 
	 * @param sql
	 * @param value
	 * @return
	 */
	public List<Map<String, String>> inquire(String sql, String value) {
		inquire(sql, new String[] { value });
		return null;
	}

	/**
	 * 无参数列表查询语句s
	 * 
	 * @param sql
	 * @return
	 */
	public List<Map<String, String>> inquire(String sql) {
		inquire(sql, new String[0]);
		return null;
	}

	public List<Map<String, String>> query(String uri, String[] projection, String selection, String[] values, String order, int start, int rows) {
		ContentResolver contentResolver = context.getContentResolver();
		Uri u = Uri.parse(uri);
		Cursor cur = contentResolver.query(u, projection, selection, values, order);
		logger.info("查询结果长度:" + cur.getCount());
		List<Map<String, String>> returnData =  getDate(cur, start, rows);
		cur.close();
		return returnData;
	}

	private List<Map<String, String>> getDate(Cursor cur, int start, int rows) {
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		if (cur != null) {
			String[] columnNames = cur.getColumnNames();

			Map<String, String> dataLine = null;

			if (cur.moveToPosition(start)) {
				for (int i = 0; i < rows; i++) {
					putIntoMap(dataList, columnNames, cur);
					if (!cur.moveToNext()) {
						break;
					}
				}
			}
		}
		return dataList;
	}

	private void putIntoMap(List<Map<String, String>> dataList, String[] columnNames, Cursor cur) {
		Map<String, String> dataLine = new HashMap<String, String>();
		for (int i = 0; i < columnNames.length; i++) {
			dataLine.put(columnNames[i], cur.getString(i));
			// Logger.getLogger(this.getClass().getName()).info(columnNames[i]+":"+cur.getString(i));
		}
		dataList.add(dataLine);
	}

}
