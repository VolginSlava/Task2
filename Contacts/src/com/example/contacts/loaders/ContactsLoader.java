package com.example.contacts.loaders;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import com.example.contacts.ContactData;
import com.example.contacts.defaultloader.DefaultLoader;
import com.example.contacts.tools.Logging;

public class ContactsLoader extends DefaultLoader<Result<List<ContactData>>> {

	public ContactsLoader(Context context) {
		super(context);
		Logging.logEntrance();
	}

	@Override
	public Result<List<ContactData>> loadInBackground() {
		Logging.logEntrance();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] listItems = new String[] { "q", "qw", "qwe", "qwer", "qwert", "qwerty", "qwertyu", "qwertyui", "qwertyuio", "qwertyuiop",
											"q", "qw", "qwe", "qwer", "qwert", "qwerty", "qwertyu", "qwertyui", "qwertyuio", "qwertyuiop" };
		List<ContactData> contacts = new ArrayList<ContactData>();

		Random r = new Random();
		for (String s : listItems) {
			ContactData data = new ContactData(null, s, r.nextBoolean() ? s.toUpperCase(Locale.US) : null);
			contacts.add(data);
		}
		return Result.finished(contacts);
	}

	@Override
	protected void releaseResources(Result<List<ContactData>> data) {
		Logging.logEntrance();
		// TODO Auto-generated method stub
	}
}
