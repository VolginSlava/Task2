package com.example.contacts;

import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import java.util.List;

import com.example.contacts.fragments.ContactsList;
import com.example.contacts.fragments.NoContactsFragment;
import com.example.contacts.fragments.PermissionDialogFragment;
import com.example.contacts.fragments.ProgressDialogFragment;
import com.example.contacts.loaders.ContactsLoader;
import com.example.contacts.loaders.Result;
import com.example.contacts.tools.Logging;

public class HomeActivity extends ActionBarActivity implements OnItemClickListener {

	private static final String READ_CONTACTS_PERMISSION_ACQUIRED_KEY = "readContactsPermissionAcquired";
	private static final boolean READ_CONTACTS_PERMISSION_ACQUIRED_DEFAULT_VALUE = false;

	private static final String CONTACTS_LOADED_KEY = "contactsLoaded";
	private static final boolean CONTACTS_LOADED_DEFAULT_VALUE = false;

	private static final String CONTACTS_EMPTY_KEY = "contactsEmpty";
	private static final boolean CONTACTS_EMPTY_DEFAULT_VALUE = true;

	private boolean readContactsPermissionAcquired;
	private boolean contactsLoaded;
	private boolean contactsEmpty;

	private FragmentUtils fragmentUtils = new FragmentUtils();
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
		contactsEmpty = savedInstanceState.getBoolean(CONTACTS_EMPTY_KEY, CONTACTS_EMPTY_DEFAULT_VALUE);

		if (!readContactsPermissionAcquired) {
			askForReadContactsPermission();
		}
		if (readContactsPermissionAcquired && !contactsLoaded) {
			getLoaderManager().initLoader(LoaderUtils.LOADER_ID, new Bundle(), loaderUtils);
		}
		
		if (contactsLoaded && !contactsEmpty) {
			fragmentUtils.setOnContactsListItemClickListener(this);
		}
	}

	private void askForReadContactsPermission() {
		Logging.logEntrance();
		PermissionDialogFragment dialog = fragmentUtils.getPermissionDialogFragment();
		dialog.setPermissionAcquiredListener(fragmentUtils);
		if (!dialog.isAdded()) {
			dialog.show(getFragmentManager(), FragmentUtils.PERMISSION_DIALOG_FRAGMENT_TAG);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Logging.logEntrance();

		outState.putBoolean(READ_CONTACTS_PERMISSION_ACQUIRED_KEY, readContactsPermissionAcquired);
		outState.putBoolean(CONTACTS_LOADED_KEY, contactsLoaded);
		outState.putBoolean(CONTACTS_EMPTY_KEY, contactsEmpty);
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ContactData data = (ContactData) parent.getItemAtPosition(position);
		Logging.logEntrance(data.toString());
		
		Intent emailIntent = getEmailIntent(data);
		if (emailIntent != null) {
			startActivity(Intent.createChooser(emailIntent, ""));
		} else {
			String msg = String.format(getString(R.string.no_email_format), data.getName());
			Toast.makeText(HomeActivity.this, msg, Toast.LENGTH_SHORT).show();
		}
	}

	private Intent getEmailIntent(ContactData data) {
		Logging.logEntrance();
		if (data == null || data.getEmail() == null) {
			return null;
		}

		String subject = String.format("Hi, %s!", data.getName());
		String text = String.format("Sent from new Contacts App to your e-mail (%s).", data.getEmail());

		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("plain/text");

		intent.putExtra(Intent.EXTRA_EMAIL, new String[] { data.getEmail() });
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		intent.putExtra(Intent.EXTRA_TEXT, text);

		return intent;
	}

	private class FragmentUtils implements PermissionDialogFragment.PositiveClickListener {

		private static final String PERMISSION_DIALOG_FRAGMENT_TAG = "permissionDialogFragment";
		private static final String PROGRESS_DIALOG_FRAGMENT_TAG = "progressDialog";
		private static final String CONTACTS_LIST_FRAGMENT_TAG = "contactsList";
		private static final String NO_CONTACTS_FRAGMENT_TAG = "noContactsFragment";


		private PermissionDialogFragment getPermissionDialogFragment() {
			Logging.logEntrance();
			PermissionDialogFragment dialog = (PermissionDialogFragment) getFragmentManager().findFragmentByTag(PERMISSION_DIALOG_FRAGMENT_TAG);
			if (dialog == null) {
				dialog = PermissionDialogFragment.newInstance();
			}
			return dialog;
		}

		private ProgressDialogFragment getProgressDialogFragment() {
			Logging.logEntrance();
			ProgressDialogFragment dialog = (ProgressDialogFragment) getFragmentManager().findFragmentByTag(PROGRESS_DIALOG_FRAGMENT_TAG);
			if (dialog == null) {
				dialog = ProgressDialogFragment.newInstance();
			}
			return dialog;
		}

		public void onPositiveClick() {
			Logging.logEntrance();
			readContactsPermissionAcquired = true;

			getFragmentManager()
					.beginTransaction()
					.add(R.id.fragment, getProgressDialogFragment(), PROGRESS_DIALOG_FRAGMENT_TAG)
					.commit();
			getFragmentManager().executePendingTransactions();

			getLoaderManager().initLoader(LoaderUtils.LOADER_ID, new Bundle(), loaderUtils);
		}

		private void setOnContactsListItemClickListener(OnItemClickListener listener) {
			ContactsList contactsList = (ContactsList) getFragmentManager().findFragmentByTag(FragmentUtils.CONTACTS_LIST_FRAGMENT_TAG);
			if (contactsList != null) {
				contactsList.setOnItemClickListener(listener);
			}
		}
	}

	private class LoaderUtils implements LoaderCallbacks<Result<List<ContactData>>> {

		private static final int LOADER_ID = 1;

		public Loader<Result<List<ContactData>>> onCreateLoader(int id, Bundle args) {
			Logging.logEntrance();
			switch (id) {
			case 1:
				return new ContactsLoader(HomeActivity.this);
			default:
				return null;
			}
		}

		public void onLoadFinished(Loader<Result<List<ContactData>>> loader, final Result<List<ContactData>> result) {
			Logging.logEntrance();

			new Handler().post(new Runnable() {
				
				public void run() {
					contactsLoaded = true;
					contactsEmpty = isContactsEmpty(result);

					Fragment fragment;
					String fragmentTag;
					
					if (!contactsEmpty) {
						fragment = ContactsList.newInstance(HomeActivity.this, result.data);
						fragmentTag = FragmentUtils.CONTACTS_LIST_FRAGMENT_TAG;
					} else {
						fragment = NoContactsFragment.newInstance();
						fragmentTag = FragmentUtils.NO_CONTACTS_FRAGMENT_TAG;
					}
					
					getFragmentManager()
							.beginTransaction()
							.replace(R.id.fragment, fragment, fragmentTag)
							.commit();
				}
			});
		}

		public void onLoaderReset(Loader<Result<List<ContactData>>> loader) {
			Logging.logEntrance();
			// TODO Auto-generated method stub
		}

		private boolean isContactsEmpty(Result<List<ContactData>> data) {
			return data == null || data.data == null || data.data.size() == 0;
		}
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
}
