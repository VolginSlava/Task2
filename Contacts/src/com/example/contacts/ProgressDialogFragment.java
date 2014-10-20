package com.example.contacts;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.contacts.tools.Logging;

public class ProgressDialogFragment extends DialogFragment {

	private static final String PROGRESS_DIALOG_FRAGMENT = "ProgressDialogFragment";

	public static ProgressDialogFragment newInstance() {
		Logging.logEntrance(PROGRESS_DIALOG_FRAGMENT);
		return new ProgressDialogFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.progress_dialog_fragment, container, false);
		return view;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Logging.logEntrance(PROGRESS_DIALOG_FRAGMENT);
		ProgressDialog pd = new ProgressDialog(getActivity());

		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setTitle("Title");
		pd.setMessage("Downloading...");

		pd.setCanceledOnTouchOutside(false);
		return pd;
	}
}
