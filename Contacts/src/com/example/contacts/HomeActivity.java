package com.example.contacts;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.contacts.PermissionDialogFragment.Listener;
import com.example.contacts.tools.Logging;

public class HomeActivity extends ActionBarActivity {

	private static final String READ_CONTACTS_PERMISSION_ACQUIRED_KEY = "readContactsPermissionAcquired";
	private static final boolean READ_CONTACTS_PERMISSION_ACQUIRED_DEFAULT_VALUE = false;


	private boolean readContactsPermissionAcquired;

	private DialogUtils dialogUtils = new DialogUtils();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		Logging.logEntrance(ACTIVITY_SERVICE);

		if (savedInstanceState == null) {
			savedInstanceState = new Bundle();
		}

		readContactsPermissionAcquired = savedInstanceState.getBoolean(READ_CONTACTS_PERMISSION_ACQUIRED_KEY, READ_CONTACTS_PERMISSION_ACQUIRED_DEFAULT_VALUE);

		if (!readContactsPermissionAcquired) {
			askForReadContactsPermission();
		}

	}

	private void askForReadContactsPermission() {
		Logging.logEntrance(ACTIVITY_SERVICE);
		PermissionDialogFragment dialog = dialogUtils.getPermissionDialogFragment();
		dialog.setListener(dialogUtils);
		if (!dialog.isAdded()) {
			dialog.show(getFragmentManager(), DialogUtils.PERMISSION_DIALOG_FRAGMENT_TAG);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Logging.logEntrance(ACTIVITY_SERVICE);
		outState.putBoolean(READ_CONTACTS_PERMISSION_ACQUIRED_KEY, readContactsPermissionAcquired);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class DialogUtils implements Listener {

		private static final String DIALOG_UTILS = "DialogUtils";
		private static final String PERMISSION_DIALOG_FRAGMENT_TAG = "permissionDialogFragment";


		private PermissionDialogFragment getPermissionDialogFragment() {
			Logging.logEntrance(DIALOG_UTILS);
			PermissionDialogFragment dialog = (PermissionDialogFragment) getFragmentManager().findFragmentByTag(PERMISSION_DIALOG_FRAGMENT_TAG);
			if (dialog == null) {
				dialog = PermissionDialogFragment.newInstance();
			}
			return dialog;
		}

		public void onPositiveClick() {
			Logging.logEntrance(DIALOG_UTILS);
			readContactsPermissionAcquired = true;
		}




		// @Deprecated
		// private PermissionDialogFragment getFragment() {
		// Logging.logEntrance(ACTIVITY_SERVICE);
		// PermissionDialogFragment fragment = (PermissionDialogFragment) getFragmentManager().findFragmentByTag(PERMISSION_DIALOG_FRAGMENT_TAG);
		// if (fragment == null) {
		// if (permissionDialogFragment == null) {
		// permissionDialogFragment = PermissionDialogFragment.newInstance();
		// }
		// fragment = permissionDialogFragment;
		// }
		// return fragment;
		// }


		// private void addPermissionDialog() {
		// boolean res = getFragmentManager().executePendingTransactions();
		// Logging.logEntrance(ACTIVITY_SERVICE, "executePendingTransactions: " + res);
		//
		// if (!getFragment().isAdded()) {
		// getFragmentManager().beginTransaction().add(getFragment(), PERMISSION_DIALOG_FRAGMENT_TAG).commit();
		// }
		// }
		//
		// private void removePermissionDialog() {
		// boolean res = getFragmentManager().executePendingTransactions();
		// Logging.logEntrance(ACTIVITY_SERVICE, "executePendingTransactions: " + res);
		//
		// if (getFragment().isAdded()) {
		// getFragmentManager().beginTransaction().remove(getFragment()).commit();
		// }
		// }

		// private void addListener() {
		// Logging.logEntrance(ACTIVITY_SERVICE);
		// getFragment().setListener(DialogUtils.this);
		// }
		//
		// private void removeListener() {
		// Logging.logEntrance(ACTIVITY_SERVICE);
		// getFragment().setListener(null);
		// }


		// @Override
		// public void onCancel() {
		// Logging.logEntrance(ACTIVITY_SERVICE);
		// finish();
		// }
	}
}
