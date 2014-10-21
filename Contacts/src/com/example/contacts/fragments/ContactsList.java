package com.example.contacts.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import com.example.contacts.ContactData;
import com.example.contacts.ContactsArrayAdapter;
import com.example.contacts.R;
import com.example.contacts.tools.Logging;

public class ContactsList extends Fragment {

	private static final String LIST_ITEMS_KEY = "listItems";

	public static ContactsList newInstance(OnItemClickListener onItemClickListener, String[] listItems) {
		Logging.logEntrance();

		ContactsList list = new ContactsList();
		list.setOnItemClickListener(onItemClickListener);

		Bundle args = new Bundle();
		args.putStringArray(ContactsList.LIST_ITEMS_KEY, listItems);
		list.setArguments(args);

		return list;
	}

	private ListView listView;
	private String[] listItems;
	private OnItemClickListener listener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Logging.logEntrance();
		listView = (ListView) inflater.inflate(R.layout.contacts_list, container, false);
		
		listView.setOnItemClickListener(listener);

		if (savedInstanceState == null) {
			savedInstanceState = new Bundle();
		}

		Logging.logEntrance("Saved contacts: " + Arrays.deepToString(savedInstanceState.getStringArray(LIST_ITEMS_KEY)));
		Logging.logEntrance("Initial contacts: " + Arrays.deepToString(getArguments().getStringArray(LIST_ITEMS_KEY)));

		listItems = savedInstanceState.getStringArray(LIST_ITEMS_KEY);
		if (listItems == null) {
			listItems = getArguments().getStringArray(LIST_ITEMS_KEY);
		}
		updateListItems(listItems);

		return listView;
	}

	public void updateListItems(String[] listItems) {
		Logging.logEntrance();
		listView.setAdapter(getAdapter(listItems));
		this.listItems = listItems;
	}

	private ArrayAdapter<ContactData> getAdapter(String[] args) {
		Logging.logEntrance();

		List<ContactData> ar = new ArrayList<ContactData>();
		Random r = new Random();
		for (String s : args) {
			ContactData m = new ContactData(null, s, r.nextBoolean() ? s.toUpperCase(Locale.US) : null);
			ar.add(m);
		}
		return new ContactsArrayAdapter(getActivity(), ar);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Logging.logEntrance();
		
		outState.putStringArray(LIST_ITEMS_KEY, listItems);
	}

	

	public void setOnItemClickListener(OnItemClickListener listener) {
		this.listener = listener;
	}

}
