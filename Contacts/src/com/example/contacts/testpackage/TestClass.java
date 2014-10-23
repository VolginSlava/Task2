package com.example.contacts.testpackage;

import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.Loader.OnLoadCompleteListener;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.util.Log;

import java.util.ArrayList;

import com.example.contacts.tools.Logging;

public class TestClass implements OnLoadCompleteListener<Cursor> {


	private static final Uri CONTENT_URI = ContactsContract.Data.CONTENT_URI;
	private static final String[] PROJECTION = { ContactsContract.Data.CONTACT_ID, ContactsContract.Data.PHOTO_URI };
	private static final String SELECTION = String.format("%s = ?", ContactsContract.Data.MIMETYPE);
	private static final String[] SELECTION_ARGS = { Photo.CONTENT_ITEM_TYPE };
	private static final String SORT_ORDER = null;

	
	
	private Activity activity;
	
	private Context getActivity() {
		Logging.logEntrance();
		return activity;
	}
	
	
	public Loader<Cursor> onCreateLoader() {
		Logging.logEntrance();
		/*
		 * Makes search string into pattern and
		 * stores it in the selection array
		 */
		// Starts the query
		// return new CursorLoader(getActivity(), Contacts.CONTENT_URI, null, null, null, null);

		return new CursorLoader(getActivity(), CONTENT_URI, PROJECTION, SELECTION, SELECTION_ARGS, SORT_ORDER);
	}



	
	
	
	
	
	
	
	
	public TestClass(Activity activity) {
		this.activity = activity;

		Logging.logEntrance();

		Loader<Cursor> loader = onCreateLoader();

		loader.registerListener(1, this);
		loader.startLoading();
	}

	public void onLoadComplete(Loader<Cursor> loader, Cursor cursor) {
		Logging.logEntrance(cursor.getCount() + "");

		int contactsNum = 0;
		while (cursor.moveToNext()) {
			contactsNum++;
			Log.i("", contactsNum + "______________________________________________________");
			for (int i = 0; i < cursor.getColumnCount(); i++) {
				if (cursor.getType(i) != Cursor.FIELD_TYPE_BLOB && cursor.getString(i) != null) {
					String msg = String.format("%s: %s", cursor.getColumnName(i), cursor.getString(i));
					Log.i("", msg);
				}
			}
		}
		Logging.logEntrance("n = " + contactsNum);

		ArrayList<String> colNames = new ArrayList<String>();
		for (int i = 0; i < cursor.getColumnCount(); i++) {
			colNames.add(cursor.getColumnName(i));
		}
		Logging.logEntrance(colNames.toString());
	}
}
