package com.slevenc.ninatimeline.test;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.test.AndroidTestCase;

import com.slevenc.ninatimeline.ContentResolverPageQuery;

public class woca extends AndroidTestCase {

	private static final Logger logger = Logger.getLogger("danyuantest");

	public void testGetContact() {
		ContentResolverPageQuery query = new ContentResolverPageQuery(this.getContext());
		ContentResolver cr = this.getContext().getContentResolver();
//		Cursor cur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null,null);
		Cursor cur = cr.query(ContactsContract.Data.CONTENT_URI, new String[]{"data15"},ContactsContract.Data._ID+" = '111'",null , null);
		for (String str : cur.getColumnNames()) {
			logger.info(str);
		}
		logger.info("你妹");
		logger.info(ContactsContract.CommonDataKinds.Phone.CONTENT_URI.toString());
		logger.info(ContactsContract.CommonDataKinds.Phone.NUMBER);
		
//		logger.info("where = ".concat(ContactsContract.CommonDataKinds.Phone.NUMBER+" = '15005006019'"));
//		List<Map<String, String>> dataList = query.query(ContactsContract.Data.CONTENT_URI.toString(), new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER,"display_name"},ContactsContract.CommonDataKinds.Phone.NUMBER+" like '%15005006019%'",null , null, 0, 200);
//
//		logger.info("result-size:" + dataList.size());
//		StringBuilder sb = new StringBuilder();
//		for (Map<String, String> data : dataList) {
//			sb.setLength(0);
//			for (Map.Entry<String, String> en : data.entrySet()) {
//				sb.append(en.getKey()).append(":").append(en.getValue()).append("\t");
//			}
//			logger.info(sb.toString());
//		}
	}

}
