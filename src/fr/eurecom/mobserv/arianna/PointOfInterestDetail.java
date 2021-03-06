package fr.eurecom.mobserv.arianna;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.slidingmenu.lib.SlidingMenu;

import fr.eurecom.mobserv.arianna.model.Model;
import fr.eurecom.mobserv.arianna.model.Path;
import fr.eurecom.mobserv.arianna.model.PointOfInterest;

public class PointOfInterestDetail extends BaseDrawerActivity {

	private ViewPager poiDetailPager;
	private Context ctx;
	private PointOfInterestDetailPagerAdapter poiDetailAdapter;
	private ApplicationState state;
	private String uri;
	private PointOfInterest poi;
	private List<PointOfInterest> poisInPath;

	@Override
	// TODO all the work is done in the UI thread!!!!!!
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar ab = getActionBar();
		ab.setTitle(R.string.title_poi_detail);
		ab.setDisplayShowTitleEnabled(true);
		getOverflowMenu();
		ctx = this;
		state = ApplicationState.getInstance();
		Path path = state.getCurrentPath();
		// retrieve the intent which started the activity
		Intent startIntent = getIntent();
		// setContentView(R.layout.activity_point_of_interest_detail);

		// Error during activity start-up, back to dashboard
		if (startIntent == null) {
			Toast.makeText(ctx, R.string.no_intent, Toast.LENGTH_LONG).show();
			// Asynchronous, so statements order doesn't matter
			Intent intent = new Intent(getApplicationContext(),
					DashboardActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);
			finish();
			return;
		}

		// Error during activity start-up, back to dashboard
		if (!startIntent.hasExtra(DashboardActivity.EXTRA_URI)) {
			Toast.makeText(ctx, R.string.intent_no_uri, Toast.LENGTH_LONG)
					.show();
			Intent intent = new Intent(getApplicationContext(),
					DashboardActivity.class);
			// Asynchronous, so statements order doesn't matter
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);
			finish();
			return;
		}

		/*
		 * ACTIVITY START-UP STATE COHERENCY CHECK INTERNAL REFERENCES
		 * POPULATION
		 */

		// the uri of the POI is stored in the intent
		// constants for intent extra are defined in the dashboard
		uri = startIntent.getStringExtra(DashboardActivity.EXTRA_URI);
		// TODO DEBUG
		// Toast.makeText(this, uri, Toast.LENGTH_LONG).show();

		String launcher = startIntent
				.getStringExtra(DashboardActivity.EXTRA_LAUNCHER);
		// TODO move in worker thread
		poi = (PointOfInterest) Model.getByURI(PointOfInterest.class, uri, ctx);
		
		if (launcher.equals(DashboardActivity.LAUNCHER_NFC)) {
			/**LAUNCHED BY NFC**/
			// Toast.makeText(this, "Started by NFC", Toast.LENGTH_LONG).show();
			state.setCurrentPointOfInterest(poi);
			if (path != null) {
				boolean isInPath = false;
				// TODO Needed only URI, not objects
				poisInPath = path.getPOIs();
				for (PointOfInterest poiIterator : poisInPath) {
					if (poiIterator.getUri().equals(uri)) {
						isInPath = true;
						break;
					}
				}
				if (!isInPath) {
					state.setCurrentPath(null);
					path = null;
				}
			}
			// TODO move in worker thread
			if (path != null) {
				poisInPath = path.getPOIs();
			} else {
				poisInPath = new ArrayList<PointOfInterest>(state
						.getCurrentEvent().getPois().values());
				//Collections.sort(poisInPath);
			}

		} else if (launcher.equals(DashboardActivity.LAUNCHER_PATH_DETAIL)) {
			/**LAUNCHED BY PATH DETAIL**/
			// Toast.makeText(this, "Started by PATH_DETAIL", Toast.LENGTH_LONG)
			// .show();

			// Error during activity start-up, back to dashboard
			if (!startIntent.hasExtra(DashboardActivity.EXTRA_PATH)) {
				Toast.makeText(ctx, R.string.intent_no_uri, Toast.LENGTH_LONG)
						.show();
				// Asynchronous, so statements order doesn't matter
				Intent intent = new Intent(getApplicationContext(),
						DashboardActivity.class);
				// Asynchronous, so statements order doesn't matter
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intent);
				finish();
				return;
			}

			String pathUri = startIntent
					.getStringExtra(DashboardActivity.EXTRA_PATH);
			path = (Path) Model.getByURI(Path.class, pathUri, ctx);
			poisInPath = path.getPOIs();
			
		} else if (launcher.equals(DashboardActivity.LAUNCHER_POI_LIST)) {
			/**LAUNCHED BY POI LIST**/
			// Toast.makeText(this, "Started by POI_LIST", Toast.LENGTH_LONG)
			// .show();
			// TODO move in worker thread
			poisInPath = new ArrayList<PointOfInterest>(state.getCurrentEvent()
					.getPois().values());
			//Collections.sort(poisInPath);
		} else if (launcher.equals(DashboardActivity.LAUNCHER_MAP)) {
			/**LAUNCHED BY MAP**/
			// Toast.makeText(this, "Started by Map", Toast.LENGTH_LONG).show();
			if (path != null) {
				boolean isInPath = false;
				// TODO Needed only URI, not objects
				poisInPath = path.getPOIs();
				for (PointOfInterest poiIterator : poisInPath) {
					if (poiIterator.getUri().equals(uri)) {
						isInPath = true;
						break;
					}
				}
				if (!isInPath) {
					path = null;
				}
			}
			// TODO move in worker thread
			if (path != null) {
				poisInPath = path.getPOIs();
			} else {
				poisInPath = new ArrayList<PointOfInterest>(state
						.getCurrentEvent().getPois().values());
				//Collections.sort(poisInPath);
			}

		}
		//TODO
		if(path==null)
			Collections.sort(poisInPath);
		/*
		 * SET PAGE ADPTER
		 */

		poiDetailAdapter = new PointOfInterestDetailPagerAdapter();
		poiDetailPager = (ViewPager) findViewById(R.id.point_of_interest_detail_pager);
		poiDetailPager.setAdapter(poiDetailAdapter);
		int pos = poisInPath.indexOf(poi);
		int i = 0;
		for (PointOfInterest poiIterator : poisInPath) {
			if (poiIterator.getUri().equals(uri)) {
				pos = i;
			}
			i++;
		}
		// positionPage=pos;
		poiDetailPager.setCurrentItem(pos);
	}

	@Override
	protected void onNewIntent(Intent startIntent) {
		super.onNewIntent(startIntent);
		// the uri of the POI is stored in the intent
		// constants for intent extra are defined in the dashboard
		Path path = state.getCurrentPath();
		uri = startIntent.getStringExtra(DashboardActivity.EXTRA_URI);
		// TODO DEBUG
		// Toast.makeText(this, uri, Toast.LENGTH_LONG).show();

		String launcher = startIntent
				.getStringExtra(DashboardActivity.EXTRA_LAUNCHER);
		// TODO move in worker thread
		poi = (PointOfInterest) Model.getByURI(PointOfInterest.class, uri, ctx);

		if (launcher.equals(DashboardActivity.LAUNCHER_NFC)) {
			// Toast.makeText(this, "Started by NFC", Toast.LENGTH_LONG).show();
			state.setCurrentPointOfInterest(poi);
			if (path != null) {
				boolean isInPath = false;
				// TODO Needed only URI, not objects
				poisInPath = path.getPOIs();
				for (PointOfInterest poiIterator : poisInPath) {
					if (poiIterator.getUri().equals(uri)) {
						isInPath = true;
						break;
					}
				}
				if (!isInPath) {
					state.setCurrentPath(null);
					path = null;
				}
			}
			// TODO move in worker thread
			if (path != null) {
				poisInPath = path.getPOIs();
			} else {
				poisInPath = new ArrayList<PointOfInterest>(state
						.getCurrentEvent().getPois().values());
				//Collections.sort(poisInPath);
			}

		} else if (launcher.equals(DashboardActivity.LAUNCHER_PATH_DETAIL)) {
			// Toast.makeText(this, "Started by PATH_DETAIL", Toast.LENGTH_LONG)
			// .show();

			// Error during activity start-up, back to dashboard
			if (!startIntent.hasExtra(DashboardActivity.EXTRA_PATH)) {
				Toast.makeText(ctx, R.string.intent_no_uri, Toast.LENGTH_LONG)
						.show();
				// Asynchronous, so statements order doesn't matter
				Intent intent = new Intent(getApplicationContext(),
						DashboardActivity.class);
				// Asynchronous, so statements order doesn't matter
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intent);
				finish();
				return;
			}

			String pathUri = startIntent
					.getStringExtra(DashboardActivity.EXTRA_PATH);
			path = (Path) Model.getByURI(Path.class, pathUri, ctx);
			poisInPath = path.getPOIs();

		} else if (launcher.equals(DashboardActivity.LAUNCHER_POI_LIST)) {
			// Toast.makeText(this, "Started by POI_LIST", Toast.LENGTH_LONG)
			// .show();
			// TODO move in worker thread
			poisInPath = new ArrayList<PointOfInterest>(state.getCurrentEvent()
					.getPois().values());
			//Collections.sort(poisInPath);
		} else if (launcher.equals(DashboardActivity.LAUNCHER_MAP)) {

			// Toast.makeText(this, "Started by Map", Toast.LENGTH_LONG).show();
			if (path != null) {
				boolean isInPath = false;
				// TODO Needed only URI, not objects
				poisInPath = path.getPOIs();
				for (PointOfInterest poiIterator : poisInPath) {
					if (poiIterator.getUri().equals(uri)) {
						isInPath = true;
						break;
					}
				}
				if (!isInPath) {
					path = null;
				}
			}
			// TODO move in worker thread
			if (path != null) {
				poisInPath = path.getPOIs();
			} else {
				poisInPath = new ArrayList<PointOfInterest>(state
						.getCurrentEvent().getPois().values());
			}

		}
		//TODO
		if(path==null)
			Collections.sort(poisInPath);
		/*
		 * SET PAGE ADPTER
		 */

		int pos = poisInPath.indexOf(poi);
		int i = 0;
		for (PointOfInterest poiIterator : poisInPath) {
			if (poiIterator.getUri().equals(uri)) {
				pos = i;
			}
			i++;
		}
		// positionPage=pos;
		poiDetailPager.setCurrentItem(pos);

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
	protected int getContentViewResource() {
		return R.layout.activity_point_of_interest_detail;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_point_of_interest_detail,
				menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * listener on button set current poi
	 * 
	 * @param view
	 */

	public void onSetCurrentPoi(View view) {
		final int positionPage = poiDetailPager.getCurrentItem();
		if (ApplicationState.getInstance().getCurrentPath() != null
				&& !ApplicationState.getInstance().getCurrentPath().getPOIs()
						.contains(poisInPath.get(positionPage))) {
			//ApplicationState.getInstance().setCurrentPath(null);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.alert_title_delete_your_path);
			builder.setMessage(R.string.alert_msg_delete_your_path)
					.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									ApplicationState.getInstance()
											.setCurrentPath(null);
									ApplicationState.getInstance()
											.setCurrentPointOfInterest(
													poisInPath
															.get(positionPage));
									Toast.makeText(
											getApplicationContext(),
											poisInPath.get(positionPage)
													.getTitle()
													+ " is now the current position!",
											Toast.LENGTH_SHORT).show();
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// User cancelled the dialog
								}
							});
			// Create the AlertDialog object and return it
			AlertDialog alert = builder.create();
			alert.show();

			//Toast.makeText(this, "Path deleted", Toast.LENGTH_SHORT).show();
		} else {
			ApplicationState.getInstance().setCurrentPointOfInterest(
					poisInPath.get(positionPage));
			Toast.makeText(
					this,
					poisInPath.get(positionPage).getTitle()
							+ " is now the current position!",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected int getTouchModeAbove() {
		return SlidingMenu.TOUCHMODE_NONE;
	}

	private class PointOfInterestDetailPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return poisInPath.size();
		}

		/**
		 * Create the page for the given position. The adapter is responsible
		 * for adding the view to the container given here, although it only
		 * must ensure this is done by the time it returns from
		 * {@link #finishUpdate(android.view.ViewGroup)}.
		 * 
		 * @param collection
		 *            The containing View in which the page will be shown.
		 * @param position
		 *            The page position to be instantiated.
		 * @return Returns an Object representing the new page. This does not
		 *         need to be a View, but can be some other container of the
		 *         page.
		 */
		@Override
		public Object instantiateItem(ViewGroup collection, int position) {
			LayoutInflater inflater = (LayoutInflater) collection.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View view = inflater.inflate(
					R.layout.point_of_interest_detail_pager_content, null);

			ImageView image = (ImageView) view
					.findViewById(R.id.point_of_interest_detail_img);
			InputStream bitmap = null;
			try {
				bitmap = getAssets().open(poisInPath.get(position).getImage());
				Bitmap bit = BitmapFactory.decodeStream(bitmap);
				image.setImageBitmap(bit);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					bitmap.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			TextView name = (TextView) view
					.findViewById(R.id.point_of_interest_detail_name);
			name.setText(poisInPath.get(position).getTitle());

			TextView desc = (TextView) view
					.findViewById(R.id.point_of_interest_detail_desc);
			desc.setText(poisInPath.get(position).getDescription());

			// TODO
			collection.addView(view, 0);

			return view;

		}

		/**
		 * Remove a page for the given position. The adapter is responsible for
		 * removing the view from its container, although it only must ensure
		 * this is done by the time it returns from
		 * {@link #finishUpdate(android.view.ViewGroup)}.
		 * 
		 * @param collection
		 *            The containing View from which the page will be removed.
		 * @param position
		 *            The page position to be removed.
		 * @param view
		 *            The same object that was returned by
		 *            {@link #instantiateItem(android.view.View, int)}.
		 */
		@Override
		public void destroyItem(ViewGroup collection, int position, Object view) {
			collection.removeView((View) view);
		}

		/**
		 * Determines whether a page View is associated with a specific key
		 * object as returned by instantiateItem(ViewGroup, int). This method is
		 * required for a PagerAdapter to function properly.
		 * 
		 * @param view
		 *            Page View to check for association with object
		 * @param object
		 *            Object to check for association with view
		 * @return
		 */
		@Override
		public boolean isViewFromObject(View view, Object object) {
			return (view == object);
		}

		/**
		 * Called when the a change in the shown pages has been completed. At
		 * this point you must ensure that all of the pages have actually been
		 * added or removed from the container as appropriate.
		 * 
		 * @param arg0
		 *            The containing View which is displaying this adapter's
		 *            page views.
		 */
		@Override
		public void finishUpdate(ViewGroup arg0) {
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(ViewGroup arg0) {
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int position = poiDetailPager.getCurrentItem();
		switch (item.getItemId()) {
		case R.id.menu_search:
			// Toast.makeText(this, "Tapped search", Toast.LENGTH_SHORT).show();
			break;
		case R.id.menu_share:
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_SUBJECT, "Arianna App");
			intent.putExtra(Intent.EXTRA_TEXT,
					"I have seen " + poisInPath.get(position).getTitle() + "! "
							+ "http://www.artkiller-web.com");

			startActivity(Intent.createChooser(intent, "Sharing with"));
			break;
		case R.id.menu_map:
			intent = new Intent(this, MapActivity.class);
			// Asynchronous, so statements order doesn't matter
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);
			break;
		case R.id.menu_go_to:
			if (ApplicationState.getInstance().getCurrentPointOfInterest() != null) {
				// Check if there is already a check in
				final int positionPage = poiDetailPager.getCurrentItem();
				if (!ApplicationState.getInstance().getCurrentPointOfInterest()
						.equals(poisInPath.get(positionPage))) {
					// Check if current and destination are the same
					if (ApplicationState.getInstance().getCurrentPath() != null) {
						// Check if you are following a path
						AlertDialog.Builder builder = new AlertDialog.Builder(
								this);
						builder.setTitle(R.string.alert_title_delete_your_path_go_to);
						builder.setMessage(
								R.string.alert_msg_delete_your_path_go_to)
								.setPositiveButton("Ok",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												ApplicationState
														.getInstance()
														.setDestinationPointOfInterest(
																poisInPath
																		.get(positionPage));
												Intent intent = new Intent(
														getApplicationContext(),
														MapActivity.class);
												intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
														| Intent.FLAG_ACTIVITY_SINGLE_TOP);
												startActivity(intent);
											}
										})
								.setNegativeButton("Cancel",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												// User cancelled the dialog
											}
										});
						// Create the AlertDialog object and return it
						AlertDialog alert = builder.create();
						alert.show();
					} else {
						// not in path
						ApplicationState.getInstance()
								.setDestinationPointOfInterest(
										poisInPath.get(positionPage));
						intent = new Intent(getApplicationContext(),
								MapActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
								| Intent.FLAG_ACTIVITY_SINGLE_TOP);
						startActivity(intent);
					}
				} else {
					//you are already here
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle(R.string.alert_title_you_are_already_here);
					builder.setMessage(R.string.alert_msg_you_are_already_here)
							.setCancelable(false)
							.setPositiveButton(R.string.ok,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											// do things
										}
									});
					AlertDialog alert = builder.create();
					alert.show();
				}
			} else {
				//you have to check in somewhere
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle(R.string.alert_title_please_checkin);
				builder.setMessage(R.string.alert_msg_please_checkin)
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
			}
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