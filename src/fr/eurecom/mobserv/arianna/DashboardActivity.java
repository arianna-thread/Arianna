package fr.eurecom.mobserv.arianna;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import fr.eurecom.mobserv.arianna.model.Category;
import fr.eurecom.mobserv.arianna.model.DbHelper;
import fr.eurecom.mobserv.arianna.model.Event;
import fr.eurecom.mobserv.arianna.model.MapLevel;
import fr.eurecom.mobserv.arianna.model.Model;
import fr.eurecom.mobserv.arianna.model.NavigationLink;
import fr.eurecom.mobserv.arianna.model.NavigationNode;
import fr.eurecom.mobserv.arianna.model.Path;
import fr.eurecom.mobserv.arianna.model.PointOfInterest;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;

public class DashboardActivity extends BaseDrawerActivity {
	public final static String EXTRA_URI = "fr.eurecom.mobserv.arianna.URI";
	public final static String EXTRA_NFC = "fr.eurecom.mobserv.arianna.NFC";
	public final static String EXTRA_LAUNCHER = "fr.eurecom.mobserv.arianna.LAUNCHER";
	public final static String EXTRA_PATH = "fr.eurecom.mobserv.arianna.PATH";
	public final static String EXTRA_DASHBOARD = "fr.eurecom.mobserv.arianna.DashboardActivity";
	public final static String EXTRA_MAP = "fr.eurecom.mobserv.arianna.MapActivity";
	public final static String EXTRA_POILIST = "fr.eurecom.mobserv.arianna.PointOfInterestList";
	public final static String LAUNCHER_NFC = "NFC";
	public final static String LAUNCHER_PATH_DETAIL = "PATH_DETAIL";
	public final static String LAUNCHER_PATH_LIST = "PATH_LIST";
	public final static String LAUNCHER_POI_DETAIL = "POI_DETAIL";
	public final static String LAUNCHER_POI_LIST = "POI_LIST";
	public final static String LAUNCHER_MAP = "MAP";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar ab = getActionBar();
		ab.setTitle(R.string.title_dashboard);
		ab.setDisplayShowTitleEnabled(true);
		getOverflowMenu();
		if (!Model.existDB(getApplicationContext())) {
			Model.initApp(getApplicationContext());
		}
		Model.createModels();
		// this.testDb();
		// this.loadDb();
		ApplicationState state = ApplicationState.getInstance();
		state.setContext(this);
	}

	

	public void goToPathList(View w) {
		Intent intent = new Intent(this, PathList.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
	}

	public void goToPointOfInterestList(View w) {
		startActivity(new Intent(this, PointOfInterestList.class));
	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_dashboard, menu);
		return true;

	}

	public void goToMap(View v) {
		Intent intent = new Intent(this, MapActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);

	}

	public void pointOfInterestList(View w) {
		Intent intent = new Intent(this, PointOfInterestList.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public void testDb() {
		Log.w("ARIANNA", "starting testing mode");
		try {
			SQLiteDatabase db = DbHelper.getInstance(getApplicationContext())
					.getWritableDatabase();
			// DbHelper.getInstance(getApplicationContext()).onCreate(db);
			DbHelper.getInstance(getApplicationContext()).onUpgrade(db, 0, 1);
		} catch (Exception e) {
			Log.w("ARIANNA", e);
		}

		/**
		 * EVENT
		 */
		Event[] events = { new Event(getApplicationContext(), "E0", "name",
				"address", "image", "mail", "phoneNumber") };
		for (Event event : events) {
			Assert.assertTrue(event.save());
		}
		for (Event event : events) {
			Event eventTest = (Event) Model.getByURI(Event.class,
					event.getUri(), getApplicationContext());
			Assert.assertTrue(event != null);
			Assert.assertTrue(eventTest != null);
			Assert.assertTrue(eventTest.equals(event));
			Assert.assertTrue(eventTest.hashCode() == event.hashCode());
		}
		/**
		 * MAP LEVEL
		 */
		MapLevel[] maps = {
				new MapLevel(getApplicationContext(), "M0", "name",
						"map_image", events[0]),
				new MapLevel(getApplicationContext(), "M1", "name",
						"map_image", events[0]),
				new MapLevel(getApplicationContext(), "M2", "name",
						"map_image", events[0]) };
		for (MapLevel mapLevel : maps) {
			Assert.assertTrue(mapLevel.save());
		}
		for (MapLevel mapLevel : maps) {
			MapLevel mapTest = (MapLevel) Model.getByURI(MapLevel.class,
					mapLevel.getUri(), getApplicationContext());
			Assert.assertTrue(mapTest.equals(mapLevel));
			Assert.assertTrue(mapTest.hashCode() == mapLevel.hashCode());
		}
		/**
		 * NAV NODE
		 */
		NavigationNode[] navNodes = {
				new NavigationNode(getApplicationContext(), "NN0", 660, 75,
						maps[0]),// entrata
				new NavigationNode(getApplicationContext(), "NN1", 720, 585,
						maps[0]),// 1bivio
				new NavigationNode(getApplicationContext(), "NN2", 300, 525,
						maps[0]),// minosse
				new NavigationNode(getApplicationContext(), "NN3", 300, 225,
						maps[0]),// ristorante
				new NavigationNode(getApplicationContext(), "NN4", 540, 1185,
						maps[0]), // dedalo
				new NavigationNode(getApplicationContext(), "NN5", 1260, 870,
						maps[0]), // cesso
				new NavigationNode(getApplicationContext(), "NN6", 720, 1275,
						maps[0]),// uscita
		};
		for (NavigationNode navigationNode : navNodes) {
			Assert.assertTrue(navigationNode.save());
		}
		for (NavigationNode navigationNode : navNodes) {
			NavigationNode nodeTest = (NavigationNode) Model.getByURI(
					NavigationNode.class, navigationNode.getUri(),
					getApplicationContext());
			Assert.assertTrue(nodeTest.equals(navigationNode));
			Assert.assertTrue(nodeTest.hashCode() == navigationNode.hashCode());
		}

		/**
		 * NAV LINK
		 */
		NavigationLink[] navLinks = {
				new NavigationLink(
						getApplicationContext(),
						"NL0",
						navNodes[0],
						navNodes[1],
						maps[0],
						"[{\"x\": 660,\"y\": 75}, {\"x\": 660,\"y\": 105}, {\"x\": 780,\"y\": 105}, {\"x\": 780,\"y\": 165}, {\"x\": 660,\"y\": 165}, {\"x\": 660,\"y\": 345}, {\"x\": 600,\"y\": 345}, {\"x\": 600,\"y\": 405}, {\"x\": 720,\"y\": 405}, {\"x\": 720,\"y\": 345}, {\"x\": 780,\"y\": 345}, {\"x\": 780,\"y\": 405}, {\"x\": 900,\"y\": 405}, {\"x\": 900,\"y\": 525}, {\"x\": 840,\"y\": 525}, {\"x\": 840,\"y\": 465}, {\"x\": 660,\"y\": 465}, {\"x\": 660,\"y\": 525}, {\"x\": 780,\"y\": 525}, {\"x\": 780,\"y\": 585}, {\"x\": 720,\"y\": 585}]"),
				new NavigationLink(
						getApplicationContext(),
						"NL1",
						navNodes[1],
						navNodes[2],
						maps[0],
						"[{\"x\": 720,\"y\": 585}, {\"x\": 600,\"y\": 585}, {\"x\": 600,\"y\": 525}, {\"x\": 300,\"y\": 525}]"),
				new NavigationLink(
						getApplicationContext(),
						"NL2",
						navNodes[2],
						navNodes[3],
						maps[0],
						"[{\"x\": 300,\"y\": 525}, {\"x\": 300,\"y\": 345}, {\"x\": 360,\"y\": 345}, {\"x\": 360,\"y\": 285}, {\"x\": 300,\"y\": 285}, {\"x\": 300,\"y\": 225}]"),
				new NavigationLink(
						getApplicationContext(),
						"NL3",
						navNodes[2],
						navNodes[4],
						maps[0],
						"[{\"x\": 300,\"y\": 525}, {\"x\": 240,\"y\": 525}, {\"x\": 240,\"y\": 765}, {\"x\": 300,\"y\": 765}, {\"x\": 300,\"y\": 825}, {\"x\": 240,\"y\": 825}, {\"x\": 240,\"y\": 885}, {\"x\": 300,\"y\": 885}, {\"x\": 300,\"y\": 945}, {\"x\": 180,\"y\": 945}, {\"x\": 180,\"y\": 1125}, {\"x\": 240,\"y\": 1125}, {\"x\": 240,\"y\": 1245}, {\"x\": 480,\"y\": 1245}, {\"x\": 480,\"y\": 1185}, {\"x\": 540,\"y\": 1185}]"),
				new NavigationLink(
						getApplicationContext(),
						"NL4",
						navNodes[4],
						navNodes[6],
						maps[0],
						"[{\"x\": 540,\"y\": 1185}, {\"x\": 540,\"y\": 1245}, {\"x\": 660,\"y\": 1245}, {\"x\": 660,\"y\": 1185}, {\"x\": 720,\"y\": 1185}, {\"x\": 720,\"y\": 1275}]"),
				new NavigationLink(
						getApplicationContext(),
						"NL5",
						navNodes[1],
						navNodes[5],
						maps[0],
						"[{\"x\": 720,\"y\": 585}, {\"x\": 720,\"y\": 645}, {\"x\": 660,\"y\": 645}, {\"x\": 660,\"y\": 825}, {\"x\": 600,\"y\": 825}, {\"x\": 600,\"y\": 945}, {\"x\": 660,\"y\": 945}, {\"x\": 660,\"y\": 885}, {\"x\": 780,\"y\": 885}, {\"x\": 780,\"y\": 825}, {\"x\": 840,\"y\": 825}, {\"x\": 840,\"y\": 945}, {\"x\": 1080,\"y\": 945}, {\"x\": 1080,\"y\": 825}, {\"x\": 1140,\"y\": 825}, {\"x\": 1140,\"y\": 1005}, {\"x\": 1200,\"y\": 1005}, {\"x\": 1200,\"y\": 945}, {\"x\": 1260,\"y\": 945}, {\"x\": 1260,\"y\": 870}]"),

				new NavigationLink(
						getApplicationContext(),
						"NL6",
						navNodes[1],
						navNodes[0],
						maps[0],
						"[{\"x\": 720,\"y\": 585}, {\"x\": 780,\"y\": 585}, {\"x\": 780,\"y\": 525}, {\"x\": 660,\"y\": 525}, {\"x\": 660,\"y\": 465}, {\"x\": 840,\"y\": 465}, {\"x\": 840,\"y\": 525}, {\"x\": 900,\"y\": 525}, {\"x\": 900,\"y\": 405}, {\"x\": 780,\"y\": 405}, {\"x\": 780,\"y\": 345}, {\"x\": 720,\"y\": 345}, {\"x\": 720,\"y\": 405}, {\"x\": 600,\"y\": 405}, {\"x\": 600,\"y\": 345}, {\"x\": 660,\"y\": 345}, {\"x\": 660,\"y\": 165}, {\"x\": 780,\"y\": 165}, {\"x\": 780,\"y\": 105}, {\"x\": 660,\"y\": 105}, {\"x\": 660,\"y\": 75}]"),
				new NavigationLink(
						getApplicationContext(),
						"NL7",
						navNodes[2],
						navNodes[1],
						maps[0],
						"[{\"x\": 300,\"y\": 525}, {\"x\": 600,\"y\": 525}, {\"x\": 600,\"y\": 585}, {\"x\": 720,\"y\": 585}]"),
				new NavigationLink(
						getApplicationContext(),
						"NL8",
						navNodes[3],
						navNodes[2],
						maps[0],
						"[{\"x\": 300,\"y\": 225}, {\"x\": 300,\"y\": 285}, {\"x\": 360,\"y\": 285}, {\"x\": 360,\"y\": 345}, {\"x\": 300,\"y\": 345}, {\"x\": 300,\"y\": 525}]"),
				new NavigationLink(
						getApplicationContext(),
						"NL9",
						navNodes[4],
						navNodes[2],
						maps[0],
						"[{\"x\": 540,\"y\": 1185}, {\"x\": 480,\"y\": 1185}, {\"x\": 480,\"y\": 1245}, {\"x\": 240,\"y\": 1245}, {\"x\": 240,\"y\": 1125}, {\"x\": 180,\"y\": 1125}, {\"x\": 180,\"y\": 945}, {\"x\": 300,\"y\": 945}, {\"x\": 300,\"y\": 885}, {\"x\": 240,\"y\": 885}, {\"x\": 240,\"y\": 825}, {\"x\": 300,\"y\": 825}, {\"x\": 300,\"y\": 765}, {\"x\": 240,\"y\": 765}, {\"x\": 240,\"y\": 525}, {\"x\": 300,\"y\": 525}]"),
				new NavigationLink(
						getApplicationContext(),
						"NL10",
						navNodes[6],
						navNodes[4],
						maps[0],
						"[{\"x\": 720,\"y\": 1275}, {\"x\": 720,\"y\": 1185}, {\"x\": 660,\"y\": 1185}, {\"x\": 660,\"y\": 1245}, {\"x\": 540,\"y\": 1245}, {\"x\": 540,\"y\": 1185}]"),
				new NavigationLink(
						getApplicationContext(),
						"NL11",
						navNodes[5],
						navNodes[1],
						maps[0],
						"[{\"x\": 1260,\"y\": 870}, {\"x\": 1260,\"y\": 945}, {\"x\": 1200,\"y\": 945}, {\"x\": 1200,\"y\": 1005}, {\"x\": 1140,\"y\": 1005}, {\"x\": 1140,\"y\": 825}, {\"x\": 1080,\"y\": 825}, {\"x\": 1080,\"y\": 945}, {\"x\": 840,\"y\": 945}, {\"x\": 840,\"y\": 825}, {\"x\": 780,\"y\": 825}, {\"x\": 780,\"y\": 885}, {\"x\": 660,\"y\": 885}, {\"x\": 660,\"y\": 945}, {\"x\": 600,\"y\": 945}, {\"x\": 600,\"y\": 825}, {\"x\": 660,\"y\": 825}, {\"x\": 660,\"y\": 645}, {\"x\": 720,\"y\": 645}, {\"x\": 720,\"y\": 585}]"),

		};
		for (NavigationLink navigationLink : navLinks) {
			Assert.assertTrue(navigationLink.save());
		}
		for (NavigationLink navigationLink : navLinks) {
			NavigationLink linkTest = (NavigationLink) Model.getByURI(
					NavigationLink.class, navigationLink.getUri(),
					getApplicationContext());
			Assert.assertTrue(linkTest.equals(navigationLink));
			Assert.assertTrue(linkTest.hashCode() == navigationLink.hashCode());

		}
		/**
		 * CATEGORY
		 */
		Category[] cats = {
				new Category(getApplicationContext(), "C0", "ArtWork",
						"ic_poi.png", false),
				new Category(getApplicationContext(), "C1", "Toilets",
						"ic_toilet.png", true),
				new Category(getApplicationContext(), "C2", "Restaurant",
						"ic_snack.png", true),
				new Category(getApplicationContext(), "C3", "Entrance",
						"ic_entrance.png", true),
				new Category(getApplicationContext(), "C4", "Exit",
						"ic_exit.png", true), };

		for (Category category : cats) {
			Assert.assertTrue(category.save());
		}
		for (Category category : cats) {
			Category catTest = (Category) Model.getByURI(Category.class,
					category.getUri(), getApplicationContext());
			Assert.assertTrue(catTest.equals(category));
			Assert.assertTrue(catTest.hashCode() == category.hashCode());
		}
		/**
		 * POI
		 */
		PointOfInterest[] pois = {
				new PointOfInterest(getApplicationContext(), "P1", "Entrance",
						"Please, do not feed the Minotaur",
						"This is the entrance of the labirinth", "curr.png",
						cats[3], navNodes[0], 660, 75),
				new PointOfInterest(getApplicationContext(), "P2",
						"Teseus e Arianne", "subtitle", "description",
						"curr.png", cats[0], navNodes[2], 450, 495),
				new PointOfInterest(getApplicationContext(), "P3", "Minotaur",
						"subtitle", "description", "curr.png", cats[0],
						navNodes[2], 300, 585),
				new PointOfInterest(getApplicationContext(), "P4",
						"Restaurant 'Chez le Minotaur'", "subtitle",
						"description", "curr.png", cats[2], navNodes[3], 300,
						225),
				new PointOfInterest(getApplicationContext(), "P5",
						"Yesterday night bones", "subtitle", "description",
						"curr.png", cats[0], navNodes[2], 240, 420),
				new PointOfInterest(getApplicationContext(), "P6", "Dedalo",
						"subtitle", "description", "curr.png", cats[0],
						navNodes[4], 540, 1185),
				new PointOfInterest(getApplicationContext(), "P7", "Toilets",
						"subtitle", "description", "curr.png", cats[1],
						navNodes[5], 1260, 870),
				new PointOfInterest(getApplicationContext(), "P8", "Exit",
						"subtitle", "description", "curr.png", cats[4],
						navNodes[6], 720, 1275),

		};
		for (PointOfInterest pointOfInterest : pois) {
			Assert.assertTrue(pointOfInterest.save());

		}
		for (PointOfInterest pointOfInterest : pois) {
			PointOfInterest poiTest = (PointOfInterest) Model.getByURI(
					PointOfInterest.class, pointOfInterest.getUri(),
					getApplicationContext());
			Assert.assertTrue(poiTest.equals(pointOfInterest));
			Assert.assertTrue(poiTest.hashCode() == pointOfInterest.hashCode());
		}
		/**
		 * PATH
		 */
		Path[] paths = { new Path(getApplicationContext(), "PATH0", "Leonardo",
				"descrizione", events[0]) };
		List<PointOfInterest> l = new ArrayList<PointOfInterest>();
		l.add(pois[0]);
		l.add(pois[2]);
		l.add(pois[7]);
		paths[0].setPOIs(l);
		Assert.assertTrue(l.equals(paths[0].getPOIs()));
		for (Path path : paths) {
			Assert.assertTrue(path.save());
		}
		for (Path path : paths) {
			Path pathTest = (Path) Model.getByURI(Path.class, path.getUri(),
					getApplicationContext());
			Assert.assertTrue(pathTest.equals(path));
			Assert.assertTrue(pathTest.hashCode() == path.hashCode());
		}

		/**
		 * TEST query by Param
		 */
		Map<String, MapLevel> lm = (Map) Model.getByParam(MapLevel.class,
				MapLevel.COLUMN_NAME_EVENT_URI, events[0].getUri(),
				getApplicationContext());
		for (MapLevel mapl : lm.values()) {
			MapLevel mapl2 = (MapLevel) Model.getByURI(MapLevel.class,
					mapl.getUri(), getApplicationContext());
			Assert.assertTrue(mapl.equals(mapl2));
			Assert.assertTrue(mapl.hashCode() == mapl2.hashCode());

		}
	}

	public void loadDb() {
		Event event = (Event) Model.getByURI(Event.class, "E0",
				getApplicationContext());

		for (MapLevel mapLevel : event.getLevels().values()) {
			for (NavigationNode navigationNode : mapLevel.getNavigationNodes()
					.values()) {
				Collection<NavigationLink> ins = navigationNode.getInLinks()
						.values();
				Collection<NavigationLink> outs = navigationNode.getOutLinks()
						.values();
				Collection<PointOfInterest> pois = navigationNode.getPois()
						.values();
			}
			for (NavigationLink navigationLink : mapLevel.getNavigationLinks()
					.values()) {
				navigationLink.getFromNode();
				navigationLink.getToNode();

			}
		}
		// for (Path path : event.getPaths().values()) {
		// PointOfInterest prec = null;
		// for (PointOfInterest poi : path.getPOIs()) {
		// NavigationNode n = poi.getNavNode();
		// if (prec != null) {
		// List<NavigationNode> navPath = Model.getShortestPath(
		// prec.getNavNode(), poi.getNavNode());
		// List<NavigationLink> navLink =
		// Model.getShortestPathLink(prec.getNavNode(), poi.getNavNode());
		// for (NavigationNode navigationNode : navPath) {
		// navigationNode.getUri();
		// }
		// }
		// prec = poi;
		// }
		// }

		Collection<PointOfInterest> ps = event.getPois().values();
		for (PointOfInterest pointOfInterest : ps) {
			Log.w("POI", pointOfInterest.getUri());
		}

	}

	@Override
	protected int getContentViewResource() {
		return R.layout.activity_dashboard;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_search:
			// Toast.makeText(this, "Tapped search", Toast.LENGTH_SHORT).show();
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
			builder.setMessage(R.string.msg_help).setPositiveButton(
					R.string.ok, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {

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
}