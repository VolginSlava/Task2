package com.example.contacts;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

public class PermissionDialogFragment extends DialogFragment {
	public static interface Listener {

	}

	private static final String DIALOG_FRAGMENT = "PermissionDialog";

	private Listener listener;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return super.onCreateDialog(savedInstanceState);
	}

	public void setListener(Listener listener) {
		this.listener = listener;
	}
}
