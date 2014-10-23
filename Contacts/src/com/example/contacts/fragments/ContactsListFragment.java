package com.example.contacts.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.Serializable;
import java.util.List;

import com.example.contacts.ContactData;
import com.example.contacts.ContactsArrayAdapter;
import com.example.contacts.R;
import com.example.contacts.tools.Logging;

public class ContactsListFragment extends Fragment {

	private static final String LIST_ITEMS_KEY = "listItems";

	public static ContactsListFragment newInstance(OnItemClickListener onItemClickListener, List<ContactData> data) {
		Logging.logEntrance();

		ContactsListFragment list = new ContactsListFragment();
		list.setOnItemClickListener(onItemClickListener); // TODO wrong solution

		Bundle args = new Bundle();
		args.putSerializable(ContactsListFragment.LIST_ITEMS_KEY, (Serializable) data);
		list.setArguments(args);

		return list;
	}

	private ListView listView;
	private List<ContactData> listItems;
	private OnItemClickListener listener;

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Logging.logEntrance();
		listView = (ListView) inflater.inflate(R.layout.contacts_list, container, false);
		
		listView.setOnItemClickListener(listener);

		if (savedInstanceState == null) {
			savedInstanceState = new Bundle();
		}

		Logging.logEntrance("Saved contacts: " + savedInstanceState.getSerializable(LIST_ITEMS_KEY));
		Logging.logEntrance("Initial contacts: " + getArguments().getSerializable(LIST_ITEMS_KEY));

		listItems = (List<ContactData>) savedInstanceState.getSerializable(LIST_ITEMS_KEY);
		if (listItems == null) {
			listItems = (List<ContactData>) getArguments().getSerializable(LIST_ITEMS_KEY);
		}
		updateListItems(listItems);

		return listView;
	}

	public void updateListItems(List<ContactData> listItems) {
		Logging.logEntrance();
		listView.setAdapter(getAdapter(listItems));
		this.listItems = listItems;
	}

	private ArrayAdapter<ContactData> getAdapter(List<ContactData> args) {
		Logging.logEntrance();
		return new ContactsArrayAdapter(getActivity(), args);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Logging.logEntrance();
		outState.putSerializable(LIST_ITEMS_KEY, (Serializable) listItems);

	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		this.listener = listener;
	}

}
