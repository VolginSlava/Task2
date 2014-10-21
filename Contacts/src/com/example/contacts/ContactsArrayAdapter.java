package com.example.contacts;

import android.app.Activity;
import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import com.example.contacts.tools.Logging;

public class ContactsArrayAdapter extends ArrayAdapter<ContactData> {

	private Activity context;
	private List<ContactData> items;
	private SparseArray<View> itemsViews = new SparseArray<View>();

	public ContactsArrayAdapter(Activity context, List<ContactData> items) {
		super(context, R.layout.contacts_list_item, items);
		Logging.logEntrance();
		this.context = context;
		this.items = items;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Logging.logEntrance();
		
		View view = itemsViews.get(position);
		if (view == null) {
			Logging.logEntrance("Creating new view");
			ContactData m = items.get(position);

			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.contacts_list_item, parent, false);

			initItem(view, m);

			itemsViews.put(position, view);
		}
		return view;
	}


	private void initItem(View view, ContactData m) {
		Logging.logEntrance();
		// ImageView photoView = (ImageView) view.findViewById(R.id.contacts_list_item_image); // TODO
		TextView nameView = (TextView) view.findViewById(R.id.contacts_list_item_contact_name);
		TextView emailView = (TextView) view.findViewById(R.id.contacts_list_item_contact_email);

		// Image photo = (Image) getArguments().getSerializable(PHOTO_KEY); // TODO
		String name = m.getName();
		String email = m.getEmail();

		// if (photoView != null) // TODO
		// photoView ...

		if (name != null) {
			nameView.setText(name);
		}
		if (email != null) {
			emailView.setText(email);
		}
	}
}
