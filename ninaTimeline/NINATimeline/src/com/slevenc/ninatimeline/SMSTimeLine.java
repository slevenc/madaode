package com.slevenc.ninatimeline;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SMSTimeLine extends BaseAdapter {

	private static final Logger logger = Logger.getLogger("SMSTimeline");

	public int getCount() {
		return totalCount;
	}

	public Object getItem(int arg0) {
		return dataList.get(arg0);
	}

	public long getItemId(int arg0) {
		return arg0;
	}

	public View getView(int arg0, View arg1, ViewGroup arg2) {

		while(arg0>dataList.size()){
			initData(); 
		}
		View view = LayoutInflater.from(context).inflate(R.layout.smsview, null);

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
			if (ct.getHead() != null) {
				head.setImageBitmap(ct.getHead());
			}
		}
		address.setText(addressText);
		body.setText(dataList.get(arg0).get("body"));
		date.setText(formatDateLong(dataList.get(arg0).get("date")));

		String smsType = dataList.get(arg0).get("smsType");
		if (smsType.equals("1")) {
			img.setImageResource(R.drawable.in);
		} else {
			img.setImageResource(R.drawable.out);
		}
		if(arg0%2 == 0){
			view.setBackgroundResource(R.color.BR_color1);
		}else{
			view.setBackgroundResource(R.color.BR_color2);
		}
		
		

		smsDialogListener vu = new smsDialogListener(num, context);
		view.setOnClickListener(vu);

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
		initData();
	}

	public SMSTimeLine(Context context) {
		init(context, 30);
	}

	public SMSTimeLine(Context context, int pageSize) {
		init(context, pageSize);
	}

	private void initData() {
		queryCount();
		String uri = "content://sms/";
		String[] projections = { "_id", "address", "body", "date", "type as smsType" };
		String order = "date desc";
		ContentResolverPageQuery query = new ContentResolverPageQuery(context);
		List<Map<String, String>> smsList = query.query(uri, projections, null, null, order, dataList.size() + 1, pageSize);
		for (Map<String, String> sms : smsList) {
			sms.put("type", "sms");
		}
		dataList.addAll(smsList);
	}
	
	private void queryCount(){
		ContentResolver cr = context.getContentResolver();
		Cursor cur  = cr.query(Uri.parse("content://sms/"), new String[]{"_id"}, null, null, null);
		totalCount = cur.getCount();
	}

	public void clear() {
		dataList.clear();
		this.notifyDataSetChanged();
		initData();
		this.totalCount = 0;
	}

	private String formatDateLong(String longDate) {
		long dl = Long.parseLong(longDate);
		Date d = new Date(dl);
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd\nHH:mm:ss");
		return sdf.format(d);
	}

	private class smsDialogListener implements OnClickListener {

		String num = "";
		Context context = null;

		public smsDialogListener(String num, Context context) {
			this.num = num;
			this.context = context;
		}

		public void onClick(View v) {
			Intent it = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:".concat(num)));
			context.startActivity(it);
		}
	}

}
