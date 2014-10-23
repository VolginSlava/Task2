package com.example.contacts;

import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.example.contacts.fragments.ContactsListFragment;
import com.example.contacts.fragments.NoContactsFragment;
import com.example.contacts.fragments.PermissionDialogFragment;
import com.example.contacts.fragments.ProgressDialogFragment;
import com.example.contacts.loaders.ContactNamesLoader;
import com.example.contacts.loaders.EmailLoader;
import com.example.contacts.loaders.Loaders;
import com.example.contacts.loaders.PhotosLoader;
import com.example.contacts.tools.Logging;


public class HomeActivity extends ActionBarActivity implements OnItemClickListener {

	private ActivityState activityState = new ActivityState();
	private FragmentUtils fragmentUtils = new FragmentUtils();
	private LoaderUtils loaderUtils = new LoaderUtils();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		Logging.logEntrance();

		activityState.restoreInstanceState(savedInstanceState);

		if (!activityState.readContactsPermissionAcquired) {
			askForReadContactsPermission();
		}
		if (activityState.readContactsPermissionAcquired && !activityState.contactsLoaded) {
			loaderUtils.loadContacts();
		}
		if (activityState.contactsLoaded && !activityState.contactsEmpty) {
			fragmentUtils.setOnContactsListItemClickListener(this);
		}

		
		// newLoaderUtils.loadContacts();
		// new TestClass(this);
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
		activityState.saveInstanceState(outState);
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

		String subject = String.format(getString(R.string.email_letter_subject_template), data.getName());
		String text = String.format(getString(R.string.email_letter_text_template), data.getEmail());

		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("plain/text");

		intent.putExtra(Intent.EXTRA_EMAIL, new String[] { data.getEmail() });
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		intent.putExtra(Intent.EXTRA_TEXT, text);

		return intent;
	}

	private class ActivityState {
		private static final String READ_CONTACTS_PERMISSION_ACQUIRED_KEY = "readContactsPermissionAcquired";
		private static final boolean READ_CONTACTS_PERMISSION_ACQUIRED_DEFAULT_VALUE = false;

		private static final String CONTACTS_LOADED_KEY = "contactsLoaded";
		private static final boolean CONTACTS_LOADED_DEFAULT_VALUE = false;

		private static final String CONTACTS_EMPTY_KEY = "contactsEmpty";
		private static final boolean CONTACTS_EMPTY_DEFAULT_VALUE = true;

		private boolean readContactsPermissionAcquired;
		private boolean contactsLoaded;
		private boolean contactsEmpty;
		
		
		private void saveInstanceState(Bundle outState) {
			Logging.logEntrance();

			outState.putBoolean(READ_CONTACTS_PERMISSION_ACQUIRED_KEY, readContactsPermissionAcquired);
			outState.putBoolean(CONTACTS_LOADED_KEY, contactsLoaded);
			outState.putBoolean(CONTACTS_EMPTY_KEY, contactsEmpty);
		}
		
		private void restoreInstanceState(Bundle savedInstanceState) {
			if (savedInstanceState == null) {
				Logging.logEntrance("Bundle savedInstanceState == null");
				return;
			}
			Logging.logEntrance();

			readContactsPermissionAcquired = savedInstanceState.getBoolean(READ_CONTACTS_PERMISSION_ACQUIRED_KEY, READ_CONTACTS_PERMISSION_ACQUIRED_DEFAULT_VALUE);
			contactsLoaded = savedInstanceState.getBoolean(CONTACTS_LOADED_KEY, CONTACTS_LOADED_DEFAULT_VALUE);
			contactsEmpty = savedInstanceState.getBoolean(CONTACTS_EMPTY_KEY, CONTACTS_EMPTY_DEFAULT_VALUE);
		}
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
			activityState.readContactsPermissionAcquired = true;

			getFragmentManager()
					.beginTransaction()
					.add(R.id.fragment, getProgressDialogFragment(), PROGRESS_DIALOG_FRAGMENT_TAG)
					.commit();
			getFragmentManager().executePendingTransactions();

			loaderUtils.loadContacts();
		}

		private void setOnContactsListItemClickListener(OnItemClickListener listener) {
			ContactsListFragment contactsList = (ContactsListFragment) getFragmentManager().findFragmentByTag(FragmentUtils.CONTACTS_LIST_FRAGMENT_TAG);
			if (contactsList != null) {
				contactsList.setOnItemClickListener(listener);
			}
		}
	}

	private class LoaderUtils implements LoaderCallbacks<Cursor> {

		private int loadersRemained;
		private SparseArray<ContactData> contacts;
		private ArrayList<ContactData> contactsList;

		private void loadContacts() {
			Logging.logEntrance();

			loadersRemained = Loaders.values().length;
			contacts = new SparseArray<ContactData>();
			contactsList = new ArrayList<ContactData>();

			getLoaderManager().initLoader(Loaders.CONTACTS_LOADER.ordinal(), null, this);
		}

		private void onLoadContactsComplete(final List<ContactData> contacts) {
			Logging.logEntrance();

			if (contacts != null) {// TODO remove maybe
				for (ContactData data : contactsList) {
					Log.i("", data.toString());
				}
			}
			Log.i("", "Size: " + contactsList.size());

			new Handler().post(new Runnable() {

				public void run() {
					activityState.contactsLoaded = true;
					activityState.contactsEmpty = (contacts == null || contacts.size() == 0);

					Fragment fragment;
					String fragmentTag;

					if (!activityState.contactsEmpty) {
						fragment = ContactsListFragment.newInstance(HomeActivity.this, contacts);
						fragmentTag = FragmentUtils.CONTACTS_LIST_FRAGMENT_TAG;
					} else {
						fragment = NoContactsFragment.newInstance();
						fragmentTag = FragmentUtils.NO_CONTACTS_FRAGMENT_TAG;
					}

					getFragmentManager().beginTransaction().replace(R.id.fragment, fragment, fragmentTag).commit();
				}
			});
		}

		public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			Logging.logEntrance();
			if (!isInRange(id, 0, Loaders.values().length)) {
				return null;
			}
			switch (Loaders.values()[id]) {
			case CONTACTS_LOADER:
				return new ContactNamesLoader(HomeActivity.this);
			case EMAILS_LOADER:
				return new EmailLoader(HomeActivity.this);
			case PHOTOS_LOADER:
				return new PhotosLoader(HomeActivity.this);

			default:
				return null;
			}
		}

		public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
			Logging.logEntrance("loadersRemained: " + (loadersRemained - 1));
			loadersRemained--;

			int id = loader.getId();
			if (!isInRange(id, 0, Loaders.values().length)) {
				return;
			}

			switch (Loaders.values()[id]) {
			case CONTACTS_LOADER:
				onContactsLoaded(data);
				break;
			case EMAILS_LOADER:
				onEmailLoaded(data);
				break;
			case PHOTOS_LOADER:
				onPhotosLoaded(data);
				break;

			default:
				break;
			}

			if (loadersRemained == 0) {
				onLoadContactsComplete(contactsList);
			}
		}


		// checks condition: id in [from, to)
		private boolean isInRange(int id, int from, int to) {
			return id >= from && id < to;
		}

		private void onContactsLoaded(Cursor data) {
			Logging.logEntrance();

			if (data.getCount() == 0) {
				onLoadContactsComplete(null);
				return;
			}

			loadAdditionalInfo();

			while (data.moveToNext()) {
				int id = data.getInt(0);
				String name = data.getString(1);

				getContact(id).setName(name);
			}
		}

		private void onEmailLoaded(Cursor data) {
			Logging.logEntrance();

			while (data.moveToNext()) {
				int id = data.getInt(0);
				String email = data.getString(1);

				getContact(id).setEmail(email);
			}
		}

		private void onPhotosLoaded(Cursor data) {
			Logging.logEntrance();

			while (data.moveToNext()) {
				int id = data.getInt(0);
				String photo = data.getString(1);

				if (photo != null) {
					Uri photoUri = Uri.parse(photo);
					getContact(id).setPhotoUri(photoUri);
				}
			}
		}

		private ContactData getContact(int id) {
			Logging.logEntrance("id: " + id);
			ContactData data = contacts.get(id);
			if (data == null) {
				data = addNewContact(id);
			}
			return data;
		}

		private ContactData addNewContact(int id) {
			ContactData contactData = new ContactData();
			contacts.put(id, contactData);
			contactsList.add(contactData);
			return contactData;
		}

		private void loadAdditionalInfo() {
			getLoaderManager().initLoader(Loaders.EMAILS_LOADER.ordinal(), null, this);
			getLoaderManager().initLoader(Loaders.PHOTOS_LOADER.ordinal(), null, this);
		}

		public void onLoaderReset(Loader<Cursor> loader) {
			// TODO Auto-generated method stub
			Logging.logEntrance();
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
