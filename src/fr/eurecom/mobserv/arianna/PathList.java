package fr.eurecom.mobserv.arianna;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import fr.eurecom.mobserv.arianna.model.Event;
import fr.eurecom.mobserv.arianna.model.Model;
import fr.eurecom.mobserv.arianna.model.Path;

// TODO Use a Loader in order to query the db in a different thread
//	than the main UI's one.
public class PathList extends BaseDrawerActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar ab = getActionBar();
        ab.setTitle(R.string.title_path_list);
        ab.setDisplayShowTitleEnabled(true);
		getOverflowMenu();
		ListView listView = (ListView) findViewById(R.id.path_list_view);
		Event event = (Event) Model.getByURI(Event.class, "E0",
				getApplicationContext());
		List<Path> paths = new ArrayList<Path>(event.getPaths().values());
		ArrayAdapter<Path> adapter = new PathArrayAdapter(this, paths);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Implement the startActivity
				Path item = (Path) parent.getAdapter().getItem(position);
//				Toast.makeText(getApplicationContext(),
//						item.getTitle() + " selected", Toast.LENGTH_LONG)
//						.show();

				Intent intent = new Intent(getApplicationContext(),
						PathDetail.class);
				intent.putExtra(DashboardActivity.EXTRA_URI, item.getUri());
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intent);
			}
		});
		// onListItemClick(new onListItemClick ListView l, View v, int position,
		// long id) {

	}

	/**
	 * method to retrieve elements of action bar to put them in overflow icon
	 */
	public void getOverflowMenu() {
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_path_list, menu);
		return true;
	}

	@Override
	protected int getContentViewResource() {
		return R.layout.activity_path_list;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_search:
			//Toast.makeText(this, "Tapped search", Toast.LENGTH_SHORT).show();
			break;
		case R.id.menu_map:
			Intent intent = new Intent(this, MapActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);
			break;
		case R.id.menu_settings: {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.title_setting);
			builder.setMessage(R.string.msg_setting)
					.setCancelable(false)
					.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// do things
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
			break;
		}
		case R.id.menu_help: {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.title_help);
			builder.setMessage(R.string.msg_help)
					.setCancelable(false)
					.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// do things
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
			break;
		}
		}
		return super.onOptionsItemSelected(item);
	}

}

/*
 * @SuppressWarnings("deprecation")
 * 
 * @Override protected void onCreate(Bundle savedInstanceState) {
 * super.onCreate(savedInstanceState);
 * 
 * 
 * Cursor mCursor = getContacts(); startManagingCursor(mCursor); // Now create a
 * new list adapter bound to the cursor. // SimpleListAdapter is designed for
 * binding to a Cursor. ListAdapter adapter = new SimpleCursorAdapter(this, //
 * Context. android.R.layout.two_line_list_item, // Specify the row template //
 * to use (here, two // columns bound to the // two retrieved cursor // rows).
 * mCursor, // Pass in the cursor to bind to. // Array of cursor columns to bind
 * to. new String[] { ContactsContract.Contacts._ID,
 * ContactsContract.Contacts.DISPLAY_NAME }, // Parallel array of which template
 * objects to bind to those // columns. new int[] { android.R.id.text1,
 * android.R.id.text2 });
 * 
 * // Bind to our new adapter. setListAdapter(adapter); }
 * 
 * @SuppressWarnings("deprecation") private Cursor getContacts() { // Run query
 * Uri uri = ContactsContract.Contacts.CONTENT_URI; String[] projection = new
 * String[] { ContactsContract.Contacts._ID,
 * ContactsContract.Contacts.DISPLAY_NAME }; String selection =
 * ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '" + ("1") + "'"; String[]
 * selectionArgs = null; String sortOrder =
 * ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
 * 
 * return managedQuery(uri, projection, selection, selectionArgs, sortOrder); }
 * 
 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
 * menu; this adds items to the action bar if it is present.
 * getMenuInflater().inflate(R.menu.activity_path_list, menu); return true; }
 * 
 * }
 */

/*
 * @Override protected void onCreate(Bundle savedInstanceState) {
 * super.onCreate(savedInstanceState);
 * //setContentView(R.layout.activity_path_list); String[] values = new String[]
 * { "Android", "iPhone", "WindowsMobile", "Blackberry", "WebOS", "Ubuntu",
 * "Windows7", "Max OS X", "Linux", "OS/2", "Android", "iPhone",
 * "WindowsMobile", "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
 * "Linux", "OS/2", "Android", "iPhone", "WindowsMobile", "Blackberry", "WebOS",
 * "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2", "Android", "iPhone",
 * "WindowsMobile", "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
 * "Linux", "OS/2" }; // Use your own layout ArrayAdapter<String> adapter = new
 * ArrayAdapter<String>(this, R.layout.path_list_row_layout, R.id.path_name,
 * values); setListAdapter(adapter); }
 * 
 * @Override protected void onListItemClick(ListView l, View v, int position,
 * long id) { String item = (String) getListAdapter().getItem(position);
 * Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show(); }
 * 
 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
 * menu; this adds items to the action bar if it is present.
 * getMenuInflater().inflate(R.menu.activity_path_list, menu); return true; }
 * 
 * }
 */