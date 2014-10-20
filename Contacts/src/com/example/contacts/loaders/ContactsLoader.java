package com.example.contacts.loaders;

import android.content.Context;

import com.example.contacts.defaultloader.DefaultLoader;
import com.example.contacts.tools.Logging;

public class ContactsLoader extends DefaultLoader<Result> {

	public ContactsLoader(Context context) {
		super(context);
		Logging.logEntrance();
	}

	@Override
	public Result loadInBackground() {
		Logging.logEntrance();
		String[] listItems = new String[] { "q", "qw", "qwe", "qwer", "qwert", "qwerty", "qwertyu", "qwertyui", "qwertyuio", "qwertyuiop" };
		return Result.finished(listItems);
	}

	@Override
	protected void releaseResources(Result data) {
		Logging.logEntrance();
		// TODO Auto-generated method stub

	}
}
