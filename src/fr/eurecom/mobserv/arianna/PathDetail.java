package fr.eurecom.mobserv.arianna;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.devsmart.android.ui.HorizontalListView;
import com.slidingmenu.lib.SlidingMenu;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import fr.eurecom.mobserv.arianna.model.Model;
import fr.eurecom.mobserv.arianna.model.Path;
import fr.eurecom.mobserv.arianna.model.PointOfInterest;

public class PathDetail extends BaseDrawerActivity {
	private Context ctx;
	private String uri;
	private Path path;
	private List<PointOfInterest> poisInPath;

	private ArrayList<String> imageSrc = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar ab = getActionBar();
        ab.setTitle(R.string.title_path_detail);
        ab.setDisplayShowTitleEnabled(true);
		setContentView(R.layout.activity_path_detail);
		getOverflowMenu();
		ctx = getApplicationContext();

		// retrieve the intent which started the activity
		Intent startIntent = getIntent();

		// Error during activity start-up, back to dashboard
		if (startIntent == null) {
			Toast.makeText(ctx, R.string.no_intent, Toast.LENGTH_LONG).show();
			Intent intent = new Intent(getApplicationContext(),
					DashboardActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);
			finish();
		}

		// Error during activity start-up, back to dashboard
		if (!startIntent.hasExtra(DashboardActivity.EXTRA_URI)) {
			Toast.makeText(ctx, R.string.intent_no_uri, Toast.LENGTH_LONG)
					.show();
			// Asynchronous, so statements order doesn't matter
			Intent intent = new Intent(getApplicationContext(),
					DashboardActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);
			finish();
		}

		uri = startIntent.getStringExtra(DashboardActivity.EXTRA_URI);
		path = (Path) Model.getByURI(Path.class, uri, ctx);

		// title of path
		TextView title = (TextView) findViewById(R.id.path_detail_title);
		title.setText(path.getTitle());

		// images of path in a horizontal view
		HorizontalListView listview = (HorizontalListView) findViewById(R.id.listview);
		listview.setAdapter(mAdapter);

		// description of path
		TextView desc = (TextView) findViewById(R.id.path_detail_desc);
		desc.setText(path.getDescription());

		// list view POIs
		ListView listView = (ListView) findViewById(R.id.path_detail_poi_list_view);
		poisInPath = path.getPOIs();
		for (PointOfInterest poi : poisInPath) {
			imageSrc.add(poi.getImage());
		}
		ArrayAdapter<PointOfInterest> adapter = new PointOfInterestArrayAdapter(
				this, poisInPath);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				PointOfInterest item = (PointOfInterest) parent.getAdapter()
						.getItem(position);
				Intent intentDetail = new Intent(getApplicationContext(),
						PointOfInterestDetail.class);
				intentDetail.putExtra(DashboardActivity.EXTRA_URI,
						item.getUri());
				intentDetail.putExtra(DashboardActivity.EXTRA_LAUNCHER,
						DashboardActivity.LAUNCHER_PATH_DETAIL);
				intentDetail.putExtra(DashboardActivity.EXTRA_PATH,
						path.getUri());
				intentDetail.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intentDetail);
				// Toast.makeText(getApplicationContext(),
				// item.getTitle() + " selected", Toast.LENGTH_LONG)
				// .show();
			}
		});

	}

	@Override
	protected void onNewIntent(Intent startIntent) {
		super.onNewIntent(startIntent);
		uri = startIntent.getStringExtra(DashboardActivity.EXTRA_URI);
		path = (Path) Model.getByURI(Path.class, uri, ctx);

		// title of path
		TextView title = (TextView) findViewById(R.id.path_detail_title);
		title.setText(path.getTitle());

		// images of path in a horizontal view
		HorizontalListView listview = (HorizontalListView) findViewById(R.id.listview);
		listview.setAdapter(mAdapter);

		// description of path
		TextView desc = (TextView) findViewById(R.id.path_detail_desc);
		desc.setText(path.getDescription());

		// list view POIs
		ListView listView = (ListView) findViewById(R.id.path_detail_poi_list_view);
		poisInPath = path.getPOIs();
		for (PointOfInterest poi : poisInPath) {
			imageSrc.add(poi.getImage());
		}
		ArrayAdapter<PointOfInterest> adapter = new PointOfInterestArrayAdapter(
				this, poisInPath);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				PointOfInterest item = (PointOfInterest) parent.getAdapter()
						.getItem(position);
				Intent intentDetail = new Intent(getApplicationContext(),
						PointOfInterestDetail.class);
				intentDetail.putExtra(DashboardActivity.EXTRA_URI,
						item.getUri());
				intentDetail.putExtra(DashboardActivity.EXTRA_LAUNCHER,
						DashboardActivity.LAUNCHER_PATH_DETAIL);
				intentDetail.putExtra(DashboardActivity.EXTRA_PATH,
						path.getUri());
				intentDetail.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intentDetail);
				// Toast.makeText(getApplicationContext(),
				// item.getTitle() + " selected", Toast.LENGTH_LONG)
				// .show();
			}
		});
	}
	public void setCurrentPath(View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.alert_title_choose_path);
		builder.setMessage(R.string.alert_msg_choose_path)
				.setCancelable(false);
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				ApplicationState.getInstance().setCurrentPath(path);
				Intent intentDetail = new Intent(ctx, MapActivity.class);
				intentDetail.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intentDetail);
			}
		});
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to invoke NO event
				dialog.cancel();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
		ApplicationState.getInstance().setCurrentPath(path);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_path_detail, menu);
		return true;
	}

	@Override
	protected int getContentViewResource() {
		// TODO Auto-generated method stub
		return R.layout.activity_path_detail;
	}

	@Override
	protected int getTouchModeAbove() {
		return SlidingMenu.TOUCHMODE_MARGIN;
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

	// adapter for horizontal list view
	private BaseAdapter mAdapter = new BaseAdapter() {

//		private OnClickListener mOnButtonClicked = new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				AlertDialog.Builder builder = new AlertDialog.Builder(
//						PathDetail.this);
//				builder.setMessage("hello from " + v);
//				builder.setPositiveButton("Cool", null);
//				builder.show();
//
//			}
//		};

		@Override
		public int getCount() {
			return imageSrc.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		// Single view in the horizontal list view
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View retval = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.viewitem, null);
			ImageView image = (ImageView) retval.findViewById(R.id.image);
			InputStream bitmap = null;
			try {
				bitmap = getAssets().open(imageSrc.get(position));
				Bitmap bit = BitmapFactory.decodeStream(bitmap);
				image.setImageBitmap(bit);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					bitmap.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return retval;
		}
	};

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
					.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

								}
							});
			// Create the AlertDialog object and return it
			AlertDialog alert = builder.create();
			alert.show();
			break;
		}
		}
		return super.onOptionsItemSelected(item);
	}
}
