package com.example.contacts;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.contacts.PermissionDialogFragment.Listener;
import com.example.contacts.tools.Logging;

public class HomeActivity extends ActionBarActivity {

	private DialogUtils dialogUtils = new DialogUtils();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		dialogUtils.addPermissionDialog();
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

		private static final String PERMISSION_DIALOG_FRAGMENT_TAG = "progressDialogFragment";

		private PermissionDialogFragment permissionDialogFragment;

		private PermissionDialogFragment getFragment() {
			Logging.logEntrance(ACTIVITY_SERVICE);
			PermissionDialogFragment fragment = (PermissionDialogFragment) getFragmentManager().findFragmentByTag(PERMISSION_DIALOG_FRAGMENT_TAG);
			if (fragment == null) {
				if (permissionDialogFragment == null) {
					permissionDialogFragment = new PermissionDialogFragment();
				}
				fragment = permissionDialogFragment;
			}
			return fragment;
		}

		private void addPermissionDialog() {
			boolean res = getFragmentManager().executePendingTransactions();
			Logging.logEntrance(ACTIVITY_SERVICE, "executePendingTransactions: " + res);

			if (!getFragment().isAdded()) {
				getFragmentManager().beginTransaction().add(getFragment(), PERMISSION_DIALOG_FRAGMENT_TAG).commit();
			}
		}

		private void removePermissionDialog() {
			boolean res = getFragmentManager().executePendingTransactions();
			Logging.logEntrance(ACTIVITY_SERVICE, "executePendingTransactions: " + res);

			if (getFragment().isAdded()) {
				getFragmentManager().beginTransaction().remove(getFragment()).commit();
			}
		}

		private void addListener() {
			Logging.logEntrance(ACTIVITY_SERVICE);
			getFragment().setListener(DialogUtils.this);
		}

		private void removeListener() {
			Logging.logEntrance(ACTIVITY_SERVICE);
			getFragment().setListener(null);
		}

		// @Override
		// public void onCancel() {
		// Logging.logEntrance(ACTIVITY_SERVICE);
		// finish();
		// }
	}
}
