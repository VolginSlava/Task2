package com.example.contacts.fragments;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.view.KeyEvent;

import com.example.contacts.R;
import com.example.contacts.tools.Logging;

public class PermissionDialogFragment extends DialogFragment {

	private static final String DIALOG_FRAGMENT = "PermissionDialog";
	private static final String TITLE_KEY = "title";
	private static final String MESSAGE_KEY = "message";

	public static PermissionDialogFragment newInstance() {
		PermissionDialogFragment fragment = new PermissionDialogFragment();

		Bundle args = new Bundle();
		args.putInt(TITLE_KEY, R.string.permission_dialog_title);
		args.putInt(MESSAGE_KEY, R.string.permission_dialog_message);
		fragment.setArguments(args);

		return fragment;
	}


	private Listener listener;


	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Logging.logEntrance(DIALOG_FRAGMENT);
		int titleId = getArguments().getInt(TITLE_KEY);
		int messageId = getArguments().getInt(MESSAGE_KEY);

		Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(titleId)
				.setMessage(messageId)
				.setCancelable(false);

		builder.setPositiveButton(R.string.permission_dialog_button_ok, getPositiveClickListener());
		builder.setNegativeButton(R.string.permission_dialog_button_cancel, getNegativeClickListener());
		builder.setOnKeyListener(getBackKeyListener());

		return builder.create();
	}

	private OnClickListener getPositiveClickListener() {
		return new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				doPositiveClick();
			}
		};
	}

	private OnClickListener getNegativeClickListener() {
		return new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				doNegativeClick();
			}
		};
	}

	private OnKeyListener getBackKeyListener() {
		return new DialogInterface.OnKeyListener() {
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				// Logging.logEntrance(DIALOG_FRAGMENT, String.format("keyCode: %d, isBackPressed: %b, KeyEvent: %s", keyCode, keyCode == KeyEvent.KEYCODE_BACK, event));
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					if (event.getAction() == KeyEvent.ACTION_UP) {
						Logging.logEntrance(DIALOG_FRAGMENT, "Back key pressed");
						doNegativeClick();
						return true;
					}
				}
				return false;
			}
		};
	}

	protected void doNegativeClick() {
		Logging.logEntrance(DIALOG_FRAGMENT);
		getActivity().finish();
	}

	protected void doPositiveClick() {
		Logging.logEntrance(DIALOG_FRAGMENT);
		if (listener != null) {
			listener.onPositiveClick();
		}
	}

	public void setListener(Listener listener) {
		Logging.logEntrance(DIALOG_FRAGMENT);
		this.listener = listener;
	}

	public static interface Listener {
		void onPositiveClick();
	}
}
