package com.example.contacts.testpackage;

import android.content.Context;
import android.content.CursorLoader;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.Contacts;

public class EmailLoader extends CursorLoader {

	private static Uri uri = ContactsContract.Data.CONTENT_URI;
	private static String[] projection = { Contacts._ID, Contacts.DISPLAY_NAME_PRIMARY, Contacts.TIMES_CONTACTED };
	private static String selection = ContactsContract.Data.MIMETYPE + " ='" + Email.CONTENT_ITEM_TYPE + "'";
	private static String[] selectionArgs = null;
	private static String sortOrder = null;

	public EmailLoader(Context context) {
		super(context, uri, projection, selection, selectionArgs, sortOrder);
	}
}
