package com.slevenc.ninatimeline;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CallTimeLine extends BaseAdapter {

	private static final Logger logger = Logger.getLogger("CallTimeline");

	public int getCount() {
		return totalCount;
	}

	public Object getItem(int arg0) {
		while (arg0 > dataList.size()) {
			if (initData() == 0) {
				break;
			}
		}
		if (arg0 >= dataList.size()) {
			arg0 = dataList.size() - 1;
		}

		return dataList.get(arg0);
	}

	public long getItemId(int arg0) {
		return arg0;
	}

	public View getView(int arg0, View arg1, ViewGroup arg2) {

		while (arg0 >= dataList.size()) {
			if (initData() == 0) {
				break;
			}
		}
		if (arg0 >= dataList.size()) {
			arg0 = dataList.size() - 1;
		}
		View view = LayoutInflater.from(context).inflate(R.layout.callview,
				null);

		TextView address = (TextView) view.findViewById(R.id.address);
		TextView date = (TextView) view.findViewById(R.id.date);
		ImageView img = (ImageView) view.findViewById(R.id.icon);
		ImageView head = (ImageView) view.findViewById(R.id.head);

		String name = dataList.get(arg0).get("name");
		String number = dataList.get(arg0).get("number");
		String addressText = number;
		if (name != null && name.length() != 0) {
			addressText = name.concat("\n").concat(number);
		}

		date.setText(formatDateLong(dataList.get(arg0).get("date")));

		String callType = dataList.get(arg0).get("callType");
		String forword = "";
		if (callType.equals("1")) {
			forword = "From ";
		} else {
			forword = "To ";
		}
		address.setText(forword.concat(addressText));
		
		if(arg0%2==0){
			view.setBackgroundResource(R.color.GM_color1);
		}else{
			view.setBackgroundResource(R.color.GM_color2);
		}
		address.setTextColor(Color.WHITE);
		date.setTextColor(Color.WHITE);

		Contact ct = cache.getContactByNumber(number);
		if (ct != null) {
			conactListener cl = new conactListener(Long.parseLong(ct.getId()),
					context);
			view.setOnClickListener(cl);
			if(ct.getHead()!=null){
				head.setImageBitmap(ct.getHead());
			}
		}else{
			dialogListener dl = new dialogListener(number, context);
			view.setOnClickListener(dl);
		}
		return view;

	}

	private List<Map<String, String>> dataList = null;
	private Context context = null;
	private int pageSize = 10;
	private ContactCache cache = null;
	private int totalCount = 0;

	/**
	 * 具体的构造方法初始化方法
	 * 
	 * @param context
	 * @param pageSize
	 */
	private void init(Context context, int pageSize) {
		this.context = context;
		this.pageSize = pageSize;
		this.dataList = new ArrayList<Map<String, String>>();
		cache = new ContactCache(context);
	}

	public CallTimeLine(Context context) {
		init(context, 2);
	}

	public CallTimeLine(Context context, int pageSize) {
		init(context, pageSize);
	}

	private int initData() {
		queryCount();
		String uri = CallLog.Calls.CONTENT_URI.toString();
		String[] projection = { "_id", "name", "number", "date",
				"type as callType" };
		String order = "date desc";
		ContentResolverPageQuery query = new ContentResolverPageQuery(context);
		List<Map<String, String>> callList = query.query(uri, projection, null, null, order, dataList.size(), pageSize);
		for (Map<String, String> call : callList) {
			call.put("type", "call");
		}
		dataList.addAll(callList);
		this.notifyDataSetChanged();
		return callList.size();
	}

	private void queryCount() {
		ContentResolver cr = context.getContentResolver();
		Cursor cur = cr.query(CallLog.Calls.CONTENT_URI, new String[] { "_id" }, null, null, null);
		cur.close();
		totalCount = cur.getCount();
	}

	public void clear() {
		dataList.clear();
		this.totalCount = 0;
		initData();
		queryCount();
		this.notifyDataSetChanged();
	}

	private String formatDateLong(String longDate) {
		long dl = Long.parseLong(longDate);
		Date d = new Date(dl);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd\nHH:mm:ss");
		return sdf.format(d);
	}

	private class conactListener implements OnClickListener {

		long num = 0;
		Context context = null;

		public conactListener(long num, Context context) {
			this.num = num;
			this.context = context;
		}

		public void onClick(View v) {
			Intent it = new Intent(Intent.ACTION_VIEW,
					ContentUris.withAppendedId(
							ContactsContract.Contacts.CONTENT_URI, num));
			logger.info(ContentUris.withAppendedId(
					ContactsContract.Contacts.CONTENT_URI, num).toString());
			context.startActivity(it);
		}
	}

	private class dialogListener implements OnClickListener {

		String tel = null;
		Context context = null;

		public dialogListener(String tel, Context context) {
			this.tel = tel;
			this.context = context;
		}

		public void onClick(View v) {
			Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"
					.concat(tel)));
			context.startActivity(it);

		}

	}

}
