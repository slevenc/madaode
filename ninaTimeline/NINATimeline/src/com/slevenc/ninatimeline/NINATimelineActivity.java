package com.slevenc.ninatimeline;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ListView;

public class NINATimelineActivity extends Activity {
	/** Called when the activity is first created. */

	private ListView dataList = null;
	private TimeLineList tll = null; 
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView listView = (ListView) LayoutInflater.from(this).inflate(R.layout.timeline, null);
        tll = new TimeLineList(this);
        listView.setAdapter(tll);
        this.setContentView(listView);
    }
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.finish(); 
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		tll.clear();
	}
	
	

}