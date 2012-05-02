package com.slevenc.ninatimeline;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
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

/**
 * @author Administrator
 * 
 */
public class TimeLineList extends BaseAdapter {

	private static final Logger logger = Logger.getLogger(TimeLineList.class
			.getName());
	/**
	 * 页面大小
	 */
	private int pageSize = 10;
	/**
	 * 当前页数
	 */
	private int page = 0;

	private int smsStart = 0;
	private int callStart = 0;

	/**
	 * 缓存
	 */
	private ContactCache cache = null;

	public ContactCache getCache() {
		return cache;
	}

	public void setCache(ContactCache cache) {
		this.cache = cache;
	}

	/**
	 * 列表数据
	 */
	private List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
	/**
	 * Context 对象
	 */
	private Context context;

	/**
	 * 默认构造方法
	 * 
	 * @param context
	 */
	public TimeLineList(Context context) {
		this.context = context;
		cache = new ContactCache(context);
	}

	/**
	 * 指定页面大小的构造方法
	 * 
	 * @param context
	 * @param pageSize
	 */
	public TimeLineList(Context context, int pageSize) {
		this.context = context;
		this.pageSize = pageSize;
		cache = new ContactCache(context);
	}

	/**
	 * 初始化数据
	 */
	private boolean next() {
		List<Map<String, String>> smsList = getSmsList();
		List<Map<String, String>> callList = getCallLogList();

		if (smsList.size() + callList.size() == 0) {
			return false;
		}

		smsStart += smsList.size();
		callStart += callList.size();

		List<List<Map<String, String>>> lists = new ArrayList<List<Map<String, String>>>();
		lists.add(smsList);
		lists.add(callList);
		int i = 0;
		if (dataList.size() == 0) {
			dataList = lists.get(0);
			i = 1;
		}
		for (; i < lists.size(); i++) {
			insertIntoList(lists.get(i), dataList);
		}
		return true;
	}

	/**
	 * 按照时间顺序插入列表
	 * 
	 * @param source
	 * @param dest
	 */
	private void insertIntoList(List<Map<String, String>> source,
			List<Map<String, String>> dest) {
		int sourceIndex = 0;
		int destIndex = 0;
		while (sourceIndex < source.size()) {
			long sourceDate = Long.parseLong(source.get(sourceIndex)
					.get("date"));
			while (destIndex < dest.size()
					&& Long.parseLong(source.get(sourceIndex).get("date")) < Long
							.parseLong(dest.get(destIndex).get("date"))) {
				destIndex++;
			}
			dest.add(destIndex, source.get(sourceIndex));
			sourceIndex++;
			destIndex++;
		}
	}

	private List<Map<String, String>> getCallLogList() {
		String uri = CallLog.Calls.CONTENT_URI.toString();
		String[] projection = { "_id", "name", "number", "date",
				"type as callType" };
		String order = "date desc";
		ContentResolverPageQuery query = new ContentResolverPageQuery(context);
		List<Map<String, String>> callLogList = query.query(uri, projection,
				null, null, order, callStart, pageSize);
		for (Map<String, String> callLog : callLogList) {
			callLog.put("type", "call");
		}
		return callLogList;
	}

	/**
	 * 获取短信列表
	 * 
	 * @param pageIndex
	 * @return
	 */
	private List<Map<String, String>> getSmsList() {
		String uri = "content://sms/";
		String[] projections = { "_id", "address", "body", "date",
				"type as smsType" };
		String order = "date desc";
		ContentResolverPageQuery query = new ContentResolverPageQuery(context);
		List<Map<String, String>> smsList = query.query(uri, projections, null,
				null, order, smsStart, pageSize);
		for (Map<String, String> sms : smsList) {
			sms.put("type", "sms");
		}
		return smsList;
	}

	public int getCount() {
		if (page == 0) {
			return dataList.size() + 1;
		} else {
			return dataList.size() + 2;
		}
	}

	public Object getItem(int arg0) {
		return dataList.get(arg0);
	}

	public long getItemId(int arg0) {

		return arg0;
	}

	public View getView(int arg0, View arg1, ViewGroup arg2) {
		View view = null;

		while (arg0 >= dataList.size() && next()) {
			this.notifyDataSetChanged();
		}
		if (arg0 >= dataList.size()) {
			arg0 = dataList.size() - 1;
		}
		String type = dataList.get(arg0).get("type");
		if (type.equals("sms")) {
			view = getSMSView(arg0, arg1, arg2);
		} else if (type.equals("call")) {
			view = getCallView(arg0, arg1, arg2);
		}

		return view;
	}

	private View getCallView(int position, View arg1, ViewGroup arg2) {
		View view = LayoutInflater.from(context).inflate(R.layout.callview,
				null);

		TextView address = (TextView) view.findViewById(R.id.address);
		TextView date = (TextView) view.findViewById(R.id.date);
		ImageView img = (ImageView) view.findViewById(R.id.icon);
		ImageView head = (ImageView) view.findViewById(R.id.head);

		String name = dataList.get(position).get("name");
		String number = dataList.get(position).get("number");
		String addressText = number;
		if (name != null && name.length() != 0) {
			addressText = name.concat("\n").concat(number);
		}

		address.setText(addressText);
		date.setText(formatDateLong(dataList.get(position).get("date")));

		String callType = dataList.get(position).get("callType");
		if (callType.equals("1")) {
			view.setBackgroundResource(R.color.call_in);
			img.setImageResource(R.drawable.in);

		} else {
			view.setBackgroundResource(R.color.call_out);
			img.setImageResource(R.drawable.out);
		}

		Contact ct = cache.getContactByNumber(number);
		if (ct != null) {
			conactListener cl = new conactListener(Long.parseLong(ct.getId()),
					context);
			view.setOnClickListener(cl);
			if(ct.getHead()!=null){
				head.setImageBitmap(ct.getHead());
			}
		}
		return view;
	}

	private View getSMSView(int arg0, View arg1, ViewGroup arg2) {
		View view = LayoutInflater.from(context)
				.inflate(R.layout.smsview, null);

		TextView address = (TextView) view.findViewById(R.id.address);
		TextView body = (TextView) view.findViewById(R.id.body);
		TextView date = (TextView) view.findViewById(R.id.date);
		ImageView img = (ImageView) view.findViewById(R.id.icon);
		ImageView head = (ImageView) view.findViewById(R.id.head);

		String num = dataList.get(arg0).get("address");
		Contact ct = cache.getContactByNumber(num);
		String addressText = num;
		if (ct != null) {
			addressText = ct.getName() + "/" + num;
			if(ct.getHead()!=null){
				head.setImageBitmap(ct.getHead());
			}
		}
		address.setText(addressText);
		body.setText(dataList.get(arg0).get("body"));
		date.setText(formatDateLong(dataList.get(arg0).get("date")));

		String smsType = dataList.get(arg0).get("smsType");
		if (smsType.equals("1")) {
			view.setBackgroundResource(R.color.sms_recive);
			img.setImageResource(R.drawable.in);
		} else {
			view.setBackgroundResource(R.color.sms_send_text);
			img.setImageResource(R.drawable.out);
		}

		smsDialogListener vu = new smsDialogListener(num, context);
		view.setOnClickListener(vu);

		return view;
	}

	private String formatDateLong(String longDate) {
		long dl = Long.parseLong(longDate);
		Date d = new Date(dl);
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd\nHH:mm:ss");
		return sdf.format(d);
	}

	public void clear() {
		dataList.clear();
		smsStart = 0;
		callStart = 0;
		cache.clear();
		this.notifyDataSetChanged();
	}

	private class smsDialogListener implements OnClickListener {

		String num = "";
		Context context = null;

		public smsDialogListener(String num, Context context) {
			this.num = num;
			this.context = context;
		}

		public void onClick(View v) {
			Intent it = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"
					.concat(num)));
			context.startActivity(it);
		}
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
