package com.example.contacts.loaders;

import android.content.Context;
import android.content.CursorLoader;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Photo;

public class PhotosLoader extends CursorLoader {

	private static final Uri CONTENT_URI = ContactsContract.Data.CONTENT_URI;
	private static final String[] PROJECTION = { ContactsContract.Data.CONTACT_ID, ContactsContract.Data.PHOTO_URI };
	private static final String SELECTION = String.format("%s = ?", ContactsContract.Data.MIMETYPE);
	private static final String[] SELECTION_ARGS = { Photo.CONTENT_ITEM_TYPE };
	private static final String SORT_ORDER = null;

	public PhotosLoader(Context context) {
		super(context, CONTENT_URI, PROJECTION, SELECTION, SELECTION_ARGS, SORT_ORDER);
	}
}
