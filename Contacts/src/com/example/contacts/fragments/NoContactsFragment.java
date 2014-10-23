package com.example.contacts.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.contacts.R;

public class NoContactsFragment extends Fragment {

	public static NoContactsFragment newInstance() {
		return new NoContactsFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.contacts_empty, container, false);
		return view;
	}

}
