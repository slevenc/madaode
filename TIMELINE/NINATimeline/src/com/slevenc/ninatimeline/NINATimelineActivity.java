package com.slevenc.ninatimeline;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TableLayout;

public class NINATimelineActivity extends Activity {
	/** Called when the activity is first created. */

	private ListView lv = null;
	private CallTimeLine ctl = null;
	private SMSTimeLine stl = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		lv = (ListView) LayoutInflater.from(this).inflate(R.layout.mainlayout, null);
		stl = new SMSTimeLine(this);
		ctl = new CallTimeLine(this);
		this.setContentView(lv);
		
		
		loadSMS();

		// ListView listView = (ListView)
		// LayoutInflater.from(this).inflate(R.layout.timeline, null);
		// tll = new CallTimeLine(this);
		// listView.setAdapter(tll);
		// this.setContentView(listView);
	}

	private void loadSMS() {
		lv.setAdapter(stl);
		this.setTitle("短信");
	}
	private void loadCall(){
		lv.setAdapter(ctl);
		this.setTitle("通话");
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
		ctl.clear();
		stl.clear();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add("短信");
		menu.add("通话");
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		String menuStr = item.getTitle().toString();
		if(menuStr.equals("短信")){
			loadSMS();
		}else{
			loadCall();
		}
		return true;
	}

}