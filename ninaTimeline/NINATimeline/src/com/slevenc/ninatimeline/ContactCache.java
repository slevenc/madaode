package com.slevenc.ninatimeline;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;

public class ContactCache {

	private Context context = null;

	public ContactCache(Context context) {
		this.context = context;
	}

	private Map<String, Contact> cache = new HashMap<String, Contact>();

	public Contact getContactByNumber(String number) {
		Contact ct = null;
		if (cache.containsKey(number)) {
			ct = cache.get(number);
		} else {
			ct = getContactFromDB(number);
		}
		return ct;
	}

	private Contact getContactFromDB(String number) {
		String[] projection = {
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
				ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME };
		ContentResolverPageQuery query = new ContentResolverPageQuery(context);
		List<Map<String, String>> contacts = query.query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI.toString(),
				projection,
				ContactsContract.CommonDataKinds.Phone.NUMBER.concat(" = ? "),
				new String[] { number }, null, 0, 1);
		Contact ct = mapToContact(contacts);
		cache.put(number, ct);
		return ct;
	}

	private Contact mapToContact(List<Map<String, String>> contacts) {
		Contact ct = null;

		if (contacts.size() != 0) {
			Map<String, String> ctMap = contacts.get(0);
			ct = new Contact();
			ct.setId(ctMap.get(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
			ct.setName(ctMap
					.get(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
			String ph_id = ctMap
					.get(ContactsContract.CommonDataKinds.Phone.PHOTO_ID);
			if (ph_id != null) {
				ContentResolver cr = context.getContentResolver();
				Cursor cur = cr.query(ContactsContract.Data.CONTENT_URI,
						new String[] { ContactsContract.Data.DATA15 },
						ContactsContract.Data._ID+" = ?",
						new String[] { ph_id }, null);
				if (cur.moveToFirst()) {
					byte[] ba = cur.getBlob(0);
					ct.setHead(BitmapFactory.decodeByteArray(ba, 0, ba.length));
				}
				cur.close();
			}
		}
		return ct;

	}
	
	public void clear(){
		cache.clear();
	}

}
