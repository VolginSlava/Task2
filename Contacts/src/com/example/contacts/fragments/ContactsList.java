package com.example.contacts.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;

import com.example.contacts.R;
import com.example.contacts.tools.Logging;

public class ContactsList extends Fragment {

	private static final String CONTACTS_LIST = "ContactsList";
	private static final String LIST_ITEMS_KEY = "listItems";

	public static ContactsList newInstance(String[] listItems) {
		Logging.logEntrance();

		ContactsList list = new ContactsList();

		Bundle args = new Bundle();
		args.putStringArray(ContactsList.LIST_ITEMS_KEY, listItems);
		list.setArguments(args);

		return list;
	}

	private ListView listView;
	private String[] listItems;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Logging.logEntrance();
		listView = (ListView) inflater.inflate(R.layout.contacts_list, container, false);

		if (savedInstanceState == null) {
			savedInstanceState = new Bundle();
		}

		Log.i(CONTACTS_LIST, "Saved contacts: " + Arrays.deepToString(savedInstanceState.getStringArray(LIST_ITEMS_KEY)));
		Log.i(CONTACTS_LIST, "Initial contacts: " + Arrays.deepToString(getArguments().getStringArray(LIST_ITEMS_KEY)));

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

	private ArrayAdapter<String> getAdapter(String[] args) {
		Logging.logEntrance();
		return args != null ? new ArrayAdapter<String>(getActivity(), R.layout.contacts_list_item, args) : null;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Logging.logEntrance();
		
		outState.putStringArray(LIST_ITEMS_KEY, listItems);
	}
}
