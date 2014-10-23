package com.example.contacts.testpackage;

import android.content.Context;
import android.content.CursorLoader;
import android.net.Uri;
import android.provider.ContactsContract;

public class PhotosLoader extends CursorLoader {

	private static final Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
	private static final String[] PROJECTION = { ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME_PRIMARY };
	private static final String SELECTION = null;
	private static final String[] SELECTION_ARGS = null;
	private static final String SORT_ORDER = null;

	public PhotosLoader(Context context) {
		super(context, CONTENT_URI, PROJECTION, SELECTION, SELECTION_ARGS, SORT_ORDER);
	}
}
