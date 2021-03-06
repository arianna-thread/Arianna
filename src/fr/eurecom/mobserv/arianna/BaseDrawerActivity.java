package fr.eurecom.mobserv.arianna;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingActivity;

import dalvik.system.BaseDexClassLoader;

import fr.eurecom.mobserv.arianna.drawerutil.ListItemEventListener;
import fr.eurecom.mobserv.arianna.drawerutil.MenuEventHandler;
import fr.eurecom.mobserv.arianna.drawerutil.MenuItem;
import fr.eurecom.mobserv.arianna.drawerutil.MenuSeparator;
import fr.eurecom.mobserv.arianna.model.Event;
import fr.eurecom.mobserv.arianna.model.Model;
import fr.eurecom.mobserv.arianna.model.Path;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public abstract class BaseDrawerActivity extends SlidingActivity {

//	 @Override
//	public void onCreate(Bundle savedInstanceState) {
//	 super.onCreate(savedInstanceState);
//	 setContentView(R.layout.activity_main);
//	 }

	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			toggle();
		}
		return true;
		// return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO potrebbe essere sbagliato
		//getMenuInflater().inflate(R.menu.activity_dashboard, menu);
		return true;
	}

	protected abstract int getContentViewResource();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			getActionBar().setHomeButtonEnabled(true);
		}
		if (!Model.existDB(getApplicationContext())) {
			Model.initApp(getApplicationContext());
		}
		Model.createModels();
		setTitle(R.string.dashboard_title);
		// set the content view
		setContentView(this.getContentViewResource());

		// configure the SlidingMenu

		setBehindContentView(R.layout.menu);
		fillMenuListView();
		SlidingMenu menu = this.getSlidingMenu();
		menu.setMode(SlidingMenu.LEFT);
		// menu.set
		menu.setTouchModeAbove(this.getTouchModeAbove());
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffsetRes(R.dimen.sliding_menu_offset);
		menu.setFadeDegree(0.35f);
	}

	@Override
	protected void onResume() {
		super.onResume();
		SlidingMenu menu = this.getSlidingMenu();
		if (menu.isMenuShowing()) {
			menu.toggle();
		}
	}

	protected int getTouchModeAbove() {
		return SlidingMenu.TOUCHMODE_FULLSCREEN;
	}

	protected List<MenuItem> getMenuList() {
		MenuItem[] values = new MenuItem[] { new MenuSeparator("Quicklinks"),
				new MenuItem("Dashboard", R.drawable.ic_action_dashboard),
				new MenuItem("Map Explore", R.drawable.ic_action_map),
				new MenuItem("Points of Interest", R.drawable.ic_action_poi),
				new MenuSeparator("Paths"),

		};
		return new ArrayList<MenuItem>(Arrays.asList(values));
	}

	private void fillMenuListView() {
		final Activity that = this;
		ListView lv = (ListView) findViewById(R.id.listView1);
		List<MenuItem> values = this.getMenuList();
		Event event = (Event) Model.getByURI(Event.class, "E0",
				getApplicationContext());
		List<Path> paths = new ArrayList<Path>(event.getPaths().values());
		for(Path p: paths){
			MenuItem i = new MenuItem(p.getTitle(), R.drawable.ic_action_path);
			final Path pinco = p;
			i.addEventListener(new ListItemEventListener() {
				
				@Override
				public void onItemClick(Context context) {
					Intent intent = new Intent(getApplicationContext(),
							PathDetail.class);
					intent.putExtra(DashboardActivity.EXTRA_URI, pinco.getUri());
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
							| Intent.FLAG_ACTIVITY_SINGLE_TOP);
					startActivity(intent);				
				}
			});
			values.add(i);
		}
		final BaseDrawerActivity bda = this;
		values.get(1).addEventListener(new ListItemEventListener() {
			@Override
			public void onItemClick(Context context) {
				ActivityManager am = (ActivityManager) context
						.getSystemService(Activity.ACTIVITY_SERVICE);
				String className = am.getRunningTasks(1).get(0).topActivity
						.getClassName();
				if (className.equals(DashboardActivity.EXTRA_DASHBOARD)) {
					bda.runOnUiThread(new ToggleDrawer(bda));
					return;
				}
				Intent intent = new Intent(context, DashboardActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_SINGLE_TOP);
				that.startActivity(intent);
			}
		});
		values.get(2).addEventListener(new ListItemEventListener() {
			@Override
			public void onItemClick(Context context) {
				ActivityManager am = (ActivityManager) context
						.getSystemService(Activity.ACTIVITY_SERVICE);
				String className = am.getRunningTasks(1).get(0).topActivity
						.getClassName();
				if (className.equals(DashboardActivity.EXTRA_MAP)) {
					bda.runOnUiThread(new ToggleDrawer(bda));
					return;
				}
				Intent intent = new Intent(context, MapActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_SINGLE_TOP);
				that.startActivity(intent);
			}
		});
		values.get(3).addEventListener(new ListItemEventListener() {
			@Override
			public void onItemClick(Context context) {
				ActivityManager am = (ActivityManager) context
						.getSystemService(Activity.ACTIVITY_SERVICE);
				String className = am.getRunningTasks(1).get(0).topActivity
						.getClassName();
				if (className.equals(DashboardActivity.EXTRA_POILIST)) {
					bda.runOnUiThread(new ToggleDrawer(bda));
					return;
				}
				Intent intent = new Intent(context, PointOfInterestList.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_SINGLE_TOP);
				that.startActivity(intent);
			}
		});

		ArrayAdapter<MenuItem> adapter = new MySimpleArrayAdapter(this, values);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new MenuEventHandler(values, this
				.getApplicationContext()));
	}

	public class MySimpleArrayAdapter extends ArrayAdapter<MenuItem> {
		private final Context context;
		private final List<MenuItem> values;

		public MySimpleArrayAdapter(Context context, List<MenuItem> values) {
			super(context, R.layout.menu_item, values);
			this.context = context;
			this.values = values;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return values.get(position).getView(context, parent);

		}
	}

	public class ToggleDrawer implements Runnable {

		private BaseDrawerActivity bda;

		public ToggleDrawer(BaseDrawerActivity bda) {
			this.bda = bda;

		}

		@Override
		public void run() {
			bda.toggle();

		}

	}

}
