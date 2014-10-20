package com.example.contacts;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.contacts.fragments.ContactsList;
import com.example.contacts.fragments.PermissionDialogFragment;
import com.example.contacts.fragments.PermissionDialogFragment.Listener;
import com.example.contacts.loaders.ContactsLoader;
import com.example.contacts.loaders.Result;
import com.example.contacts.tools.Logging;

public class HomeActivity extends ActionBarActivity {

	private static final String READ_CONTACTS_PERMISSION_ACQUIRED_KEY = "readContactsPermissionAcquired";
	private static final boolean READ_CONTACTS_PERMISSION_ACQUIRED_DEFAULT_VALUE = false;
	private static final String CONTACTS_LOADED_KEY = "contactsLoaded";
	private static final boolean CONTACTS_LOADED_DEFAULT_VALUE = false;


	private boolean readContactsPermissionAcquired;
	private boolean contactsLoaded;

	private DialogUtils dialogUtils = new DialogUtils();
	private LoaderUtils loaderUtils = new LoaderUtils();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		Logging.logEntrance();

		if (savedInstanceState == null) {
			savedInstanceState = new Bundle();
		}

		readContactsPermissionAcquired = savedInstanceState.getBoolean(READ_CONTACTS_PERMISSION_ACQUIRED_KEY, READ_CONTACTS_PERMISSION_ACQUIRED_DEFAULT_VALUE);
		contactsLoaded = savedInstanceState.getBoolean(CONTACTS_LOADED_KEY, CONTACTS_LOADED_DEFAULT_VALUE);

		if (!readContactsPermissionAcquired) {
			askForReadContactsPermission();
		}
		if (readContactsPermissionAcquired && !contactsLoaded) {
			getLoaderManager().initLoader(LoaderUtils.LOADER_ID, new Bundle(), loaderUtils);
		}
	}

	private void askForReadContactsPermission() {
		Logging.logEntrance();
		PermissionDialogFragment dialog = dialogUtils.getPermissionDialogFragment();
		dialog.setPermissionAcquiredListener(dialogUtils);
		if (!dialog.isAdded()) {
			dialog.show(getFragmentManager(), DialogUtils.PERMISSION_DIALOG_FRAGMENT_TAG);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Logging.logEntrance();
		outState.putBoolean(READ_CONTACTS_PERMISSION_ACQUIRED_KEY, readContactsPermissionAcquired);
		outState.putBoolean(CONTACTS_LOADED_KEY, contactsLoaded);
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

		private static final String PROGRESS_DIALOG_TAG = "progressDialog";
		private static final String CONTACTS_LIST_TAG = "contactsList";
		private static final String PERMISSION_DIALOG_FRAGMENT_TAG = "permissionDialogFragment";


		private PermissionDialogFragment getPermissionDialogFragment() {
			Logging.logEntrance();
			PermissionDialogFragment dialog = (PermissionDialogFragment) getFragmentManager().findFragmentByTag(PERMISSION_DIALOG_FRAGMENT_TAG);
			if (dialog == null) {
				dialog = PermissionDialogFragment.newInstance();
			}
			return dialog;
		}

		public void onPositiveClick() {
			Logging.logEntrance();
			readContactsPermissionAcquired = true;

			ProgressDialogFragment dialog = ProgressDialogFragment.newInstance();
			getFragmentManager()
					.beginTransaction()
					.add(R.id.fragment, dialog, PROGRESS_DIALOG_TAG)
					.commit();
			getFragmentManager().executePendingTransactions();

			getLoaderManager().initLoader(LoaderUtils.LOADER_ID, new Bundle(), loaderUtils);
		}
	}

	private class LoaderUtils implements LoaderCallbacks<Result> {

		private static final int LOADER_ID = 1;

		public Loader<Result> onCreateLoader(int id, Bundle args) {
			Logging.logEntrance();
			switch (id) {
			case 1:
				return new ContactsLoader(HomeActivity.this);
			default:
				return null;
			}
		}

		public void onLoadFinished(Loader<Result> loader, final Result data) {
			Logging.logEntrance();

			new Handler().post(new Runnable() {
				
				public void run() {
					ContactsList contacts = ContactsList.newInstance(data.lines);
					contactsLoaded = true;
					
					getFragmentManager()
							.beginTransaction()
							.replace(R.id.fragment, contacts, DialogUtils.CONTACTS_LIST_TAG)
							.commit();
				}
			});
		}

		public void onLoaderReset(Loader<Result> loader) {
			Logging.logEntrance();
			// TODO Auto-generated method stub
		}
	}
}
