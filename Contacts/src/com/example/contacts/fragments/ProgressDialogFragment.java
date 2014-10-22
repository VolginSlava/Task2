package com.example.contacts.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.contacts.R;
import com.example.contacts.tools.Logging;

public class ProgressDialogFragment extends DialogFragment {

	public static ProgressDialogFragment newInstance() {
		Logging.logEntrance();
		return new ProgressDialogFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Logging.logEntrance();
		return inflater.inflate(R.layout.contacts_loading_progress, container, false);
	}
}
