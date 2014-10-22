package com.example.contacts.testpackage;

import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.Loader.OnLoadCompleteListener;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.Contacts;
import android.util.Log;

import java.util.ArrayList;

import com.example.contacts.tools.Logging;

public class TestClass implements OnLoadCompleteListener<Cursor> {

	private static final String[] PROJECTION = { Contacts._ID, Contacts.DISPLAY_NAME_PRIMARY, Contacts.TIMES_CONTACTED };
	private static final String SELECTION = ContactsContract.Data.MIMETYPE + " ='" + Email.CONTENT_ITEM_TYPE + "'";

	// Defines a variable for the search string
	
	
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

		return new CursorLoader(getActivity(), ContactsContract.Data.CONTENT_URI, null, SELECTION, null, null);

	}



	
	
	
	
	
	
	
	
	public TestClass(Activity activity) {
		this.activity = activity;

		Logging.logEntrance();

		Loader<Cursor> loader = onCreateLoader();

		loader.registerListener(1, this);
		loader.startLoading();
		// TextUtils.
		// getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder)
		// String str = ContactsContract.RawContacts.;
		// Logging.logEntrance("test!!! " + str);
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
			// ArrayList<String> values = new ArrayList<String>();
			// for (int i = 0; i < cursor.getColumnCount(); i++) {
			// values.add(cursor.getString(i));
			// }
			// Logging.logEntrance(contactsNum + ") " + values);
		}
		Logging.logEntrance("n = " + contactsNum);

		ArrayList<String> colNames = new ArrayList<String>();
		for (int i = 0; i < cursor.getColumnCount(); i++) {
			colNames.add(cursor.getColumnName(i));
		}
		Logging.logEntrance(colNames.toString());

		// TODO Auto-generated method stub

	}

	// Previous code
	// public void onLoadComplete(Loader<Cursor> loader, Cursor cursor) {
	// Logging.logEntrance(cursor.getCount() + "");
	//
	// int contactsNum = 0;
	// while (cursor.moveToNext()) {
	// contactsNum++;
	//
	// ArrayList<String> values = new ArrayList<String>();
	// for (int i = 0; i < cursor.getColumnCount(); i++) {
	// values.add(cursor.getString(i));
	// }
	// Logging.logEntrance(contactsNum + ") " + values);
	// }
	// Logging.logEntrance("n = " + contactsNum);
	//
	// ArrayList<String> colNames = new ArrayList<String>();
	// for (int i = 0; i < cursor.getColumnCount(); i++) {
	// colNames.add(cursor.getColumnName(i));
	// }
	// Logging.logEntrance(colNames.toString());
	//
	// // TODO Auto-generated method stub
	//
	// }
}
