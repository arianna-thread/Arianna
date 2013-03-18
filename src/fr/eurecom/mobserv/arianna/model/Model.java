package fr.eurecom.mobserv.arianna.model;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import junit.framework.Assert;

import com.google.gson.annotations.Expose;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


/**
 * 
 * if you want to add a field for a model in DB you must add:
 * 	1 string COLUMN_NAME_FIELD
 *  and update:
 * 	1 save()
 * 	2 constructor from cursor
 *  3 constructor standard
 * 	4 getter/setter
 *  5 string SQL_CREATE_TABLE
 *  6 string COLUMNS_NAME
 */
public abstract class Model {
	/*
	 * Maps to store object already retrieving from DB
	 */
	public static Map<String, Map<String, Model>> instances=null;
	/*
	 * Maps to store object already retrieving from DB
	 */
	public static void createModels(){
		if(instances == null){
		instances = new HashMap<String, Map<String, Model>>();	
		instances.put(Event.TABLE_NAME, new HashMap<String, Model>());
		instances.put(MapLevel.TABLE_NAME, new HashMap<String, Model>());
		instances.put(NavigationNode.TABLE_NAME, new HashMap<String, Model>());
		instances.put(NavigationLink.TABLE_NAME, new HashMap<String, Model>());
		instances.put(Category.TABLE_NAME, new HashMap<String, Model>());
		instances.put(PointOfInterest.TABLE_NAME, new HashMap<String, Model>());
		instances.put(Path.TABLE_NAME, new HashMap<String, Model>());
		}
	}
	

	/*
	 * DATABASE PROPERTIES
	 */
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "Arianna.db";

	/*
	 * SQL SYNTAX CONSTANTS
	 */
	public static final String COMMA_SEP = ", ";
	public static final String TEXT_TYPE = " TEXT";
	public static final String INT_TYPE = " INTEGER";
	public static final String UNIQUE = " UNIQUE";
	public static final String COORDINATE_TYPE = " INTEGER";
	public static final String PRIMARY_KEY = " INTEGER PRIMARY KEY";// AUTOINCREMENT

	/*
	 * MODEL PROPERTIES COLUMN NAME
	 */
	public static final String COLUMN_NAME_URI = "uri";

	/*
	 * ADDITIONAL TABLES
	 */
	public static final String TABLE_NAME_POI_PATH_RELATION = "poi_path_relation";
	public static final String COLUMN_NAME_PATH_URI = "path_uri";
	public static final String COLUMN_NAME_POI_URI = "poi_uri";
	public static final String COLUMN_NAME_POSITION = "position";

	public static final String SQL_CREATE_TABLE_POI_PATH_RELATION = "CREATE TABLE "
			+ Model.TABLE_NAME_POI_PATH_RELATION
			+ " ("
			+ Model.COLUMN_NAME_PATH_URI+ Model.TEXT_TYPE+ Model.COMMA_SEP
			+ Model.COLUMN_NAME_POI_URI+ Model.TEXT_TYPE+ Model.COMMA_SEP
			+ Model.COLUMN_NAME_POSITION + Model.INT_TYPE + Model.COMMA_SEP
			+ "FOREIGN KEY("+ Model.COLUMN_NAME_PATH_URI+ ") REFERENCES "+ Path.TABLE_NAME+ "("+ Model.COLUMN_NAME_URI+ ")"
			+ COMMA_SEP
			+ "FOREIGN KEY("
			+ Model.COLUMN_NAME_POI_URI
			+ ") REFERENCES "
			+ PointOfInterest.TABLE_NAME
			+ "("
			+ Model.COLUMN_NAME_URI
			+ ")"
			+ COMMA_SEP
			+ "PRIMARY KEY("
			+ Model.COLUMN_NAME_PATH_URI
			+ COMMA_SEP + Model.COLUMN_NAME_POI_URI + ")" + ")";

	public static final String TABLE_NAME_POI_ATTRIBUTE = "poi_attribute";
	public static final String COLUMN_NAME_ATTRIBUTE_KEY = "attribute_key";
	public static final String COLUMN_NAME_ATTRIBUTE_VALUE = "attribute_value";

	public static final String SQL_CREATE_TABLE_POI_ATTRIBUTE = "CREATE TABLE "
			+ Model.TABLE_NAME_POI_ATTRIBUTE + " (" + Model.COLUMN_NAME_POI_URI
			+ Model.TEXT_TYPE + Model.COMMA_SEP
			+ Model.COLUMN_NAME_ATTRIBUTE_KEY + Model.TEXT_TYPE
			+ Model.COMMA_SEP + Model.COLUMN_NAME_ATTRIBUTE_VALUE
			+ Model.TEXT_TYPE + Model.COMMA_SEP + "FOREIGN KEY("
			+ Model.COLUMN_NAME_POI_URI + ") REFERENCES "
			+ PointOfInterest.TABLE_NAME + "(" + Model.COLUMN_NAME_URI + ")"
			+ COMMA_SEP + "PRIMARY KEY(" + Model.COLUMN_NAME_POI_URI
			+ COMMA_SEP + Model.COLUMN_NAME_ATTRIBUTE_KEY + ")" + ")";

	/*
	 * GENERAL DROP STATEMENT
	 */
	public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS ";

	/*
	 * MODEL PROPERTIES
	 */
	private Context context;
	@Expose
	private String uri;

	/**
	 * @author uccio
	 */

	/* Array with all the create table statements */
	public static ArrayList<String> SQLCreateTableStatements = new ArrayList<String>();

	static {
		SQLCreateTableStatements.add(Event.SQL_CREATE_TABLE);
		SQLCreateTableStatements.add(MapLevel.SQL_CREATE_TABLE);
		SQLCreateTableStatements.add(NavigationNode.SQL_CREATE_TABLE);
		SQLCreateTableStatements.add(NavigationLink.SQL_CREATE_TABLE);
		SQLCreateTableStatements.add(PointOfInterest.SQL_CREATE_TABLE);
		SQLCreateTableStatements.add(Path.SQL_CREATE_TABLE);
		SQLCreateTableStatements.add(Category.SQL_CREATE_TABLE);
		SQLCreateTableStatements.add(Model.SQL_CREATE_TABLE_POI_PATH_RELATION);
		SQLCreateTableStatements.add(Model.SQL_CREATE_TABLE_POI_ATTRIBUTE);
	}
	
	public static boolean existDB(Context ctx) {
		boolean result;
		File dbFile = ctx.getDatabasePath(
				Model.DATABASE_NAME);
		result = dbFile.exists();
		return result;
	}
	/* Array with all the drop table statements */
	public static ArrayList<String> SQLDropTableStatements = new ArrayList<String>();

	static {
		SQLDropTableStatements.add(Model.SQL_DROP_TABLE + Event.TABLE_NAME);
		SQLDropTableStatements.add(Model.SQL_DROP_TABLE + MapLevel.TABLE_NAME);
		SQLDropTableStatements.add(Model.SQL_DROP_TABLE
				+ NavigationNode.TABLE_NAME);
		SQLDropTableStatements.add(Model.SQL_DROP_TABLE
				+ NavigationLink.TABLE_NAME);
		SQLDropTableStatements.add(Model.SQL_DROP_TABLE
				+ PointOfInterest.TABLE_NAME);
		SQLDropTableStatements.add(Model.SQL_DROP_TABLE + Path.TABLE_NAME);
		SQLDropTableStatements.add(Model.SQL_DROP_TABLE + Category.TABLE_NAME);
		SQLDropTableStatements.add(Model.SQL_DROP_TABLE
				+ Model.TABLE_NAME_POI_PATH_RELATION);
		SQLDropTableStatements.add(Model.SQL_DROP_TABLE
				+ Model.TABLE_NAME_POI_ATTRIBUTE);
	}

	public Model(Context context, String uri) {
		this.context = context;
		this.uri = uri;
	}

	/**
	 * Method for retrieving a model given its unique URI
	 * 
	 * @param uri
	 * @return the model requested
	 */
	public static Model getByURI(Class<?> classname, String uri, Context context) {
		Map<String, Model> elements = null;
		Model model = null;
		try {
			/** try to select object from objects already loaded **/
			elements = instances.get((String) classname.getDeclaredField(
					"TABLE_NAME").get(null));
			model = elements.get(uri);
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (NoSuchFieldException e1) {
			e1.printStackTrace();
		}

		if (model == null) {
			SQLiteDatabase db = DbHelper.getInstance(context)
					.getReadableDatabase();
			try {
				// get field declared as TABLE_NAME from specific model class
				String table = (String) classname
						.getDeclaredField("TABLE_NAME").get(null);
				// projection null in order to return all columns
				// get field declared as COLUMNS_NAME from specific class of a
				// model, it is an array containing all column names to select
				String[] projection = (String[]) classname.getField(
						"COLUMNS_NAME").get(null);
				// Define 'where' part of query.
				String selection = Model.COLUMN_NAME_URI + "=?";
				// Specify arguments in placeholder order.
				String[] selectionArgs = { uri };
				Cursor cur = db.query(table, // The table to query
						projection, // The columns to return
						selection, // The columns for the WHERE clause
						selectionArgs, // The values for the WHERE clause
						null, // don't group the rows
						null, // don't filter by row groups
						null // The sort order
						);

				cur.moveToFirst();
				// Retrieve the right constructor for the specific class of a
				// model
				Constructor<?> constructor = classname
						.getConstructor(new Class[] { Context.class,
								Cursor.class });
				model = (Model) constructor.newInstance(context, cur);
				elements.put(model.getUri(), model);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return model;
	}

	public static Map<String, ? extends Model> getByParam(Class<?> classname,
			String paramName, String paramValue, Context context) {
		Map<String, Model> mapModels = new HashMap<String, Model>();
		SQLiteDatabase db = DbHelper.getInstance(context).getReadableDatabase();
		Map<String, Model> elements = null;
		Model model = null;
		// get field declared as TABLE_NAME from specific model class
		String table;
		try {
			table = (String) classname.getDeclaredField("TABLE_NAME").get(null);
			// projection null in order to return all columns
			String[] projection = (String[]) classname.getField("COLUMNS_NAME")
					.get(null);
			// Define 'where' part of query.
			String selection = paramName + "=?";
			// Specify arguments in placeholder order.
			String[] selectionArgs = { paramValue };
			Cursor cur = db.query(table, // The table to query
					projection, // The columns to return
					selection, // The columns for the WHERE clause
					selectionArgs, // The values for the WHERE clause
					null, // don't group the rows
					null, // don't filter by row groups
					null // The sort order
					);
			cur.moveToFirst();
			// Retrieve the right constructor for the specific class of a
			// model
			Constructor<?> constructor = classname.getConstructor(new Class[] {
					Context.class, Cursor.class });
			while (cur.isAfterLast() == false) {
				String uri = cur.getString(cur
						.getColumnIndex(Model.COLUMN_NAME_URI));
				/** try to select object from objects already loaded **/
				elements = instances.get((String) classname.getDeclaredField(
						"TABLE_NAME").get(null));
				model = elements.get(uri);
				if (model == null)
					model = (Model) constructor.newInstance(context, cur);
				mapModels.put(model.getUri(), model);
				cur.moveToNext();
			}

		} catch (IllegalArgumentException e) {

			e.printStackTrace();
		} catch (IllegalAccessException e) {

			e.printStackTrace();
		} catch (NoSuchFieldException e) {

			e.printStackTrace();
		} catch (NoSuchMethodException e) {

			e.printStackTrace();
		} catch (InstantiationException e) {

			e.printStackTrace();
		} catch (InvocationTargetException e) {

			e.printStackTrace();
		}

		return mapModels;
	}

	/**
	 * Save or update the entity on the db
	 * 
	 * @return false on failure, true on success
	 */
	public abstract boolean save();

	/**
	 * Delete the entity from the db
	 * 
	 * @return false on failure, true on success
	 */
	public boolean delete() {
		SQLiteDatabase db = DbHelper.getInstance(context).getWritableDatabase();
		String whereClause = COLUMN_NAME_URI + " = ?";
		String[] whereArgs = { this.getUri() };
		int numRowAffected = db.delete(this.getTableName(), whereClause,
				whereArgs);

		if (numRowAffected != 1) {
			return false;
		}
		return true;
	}

	protected abstract String getTableName();

	/**
	 * Used by Model.save when entry is already in the db
	 * 
	 * @return false on failure, true on success
	 */
	protected boolean update() {

		// TODO implementare update
		return false;
	}

	/********************************************************
	 * * GETTER AND SETTERS * *
	 ********************************************************/

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	/********************************************************
	 * * EQUALS AND HASHCODE * *
	 ********************************************************/

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((context == null) ? 0 : context.hashCode());
		result = prime * result + ((uri == null) ? 0 : uri.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Model other = (Model) obj;
		if (context == null) {
			if (other.context != null)
				return false;
		} else if (!context.equals(other.context))
			return false;
		if (uri == null) {
			if (other.uri != null)
				return false;
		} else if (!uri.equals(other.uri))
			return false;
		return true;
	}
	
	/** compute all paths from a source 
	 * *
	 * @param source
	 */
//	public static void computePaths(NavigationNode source) {
//		source.minDistance = 0.;
//		PriorityQueue<NavigationNode> vertexQueue = new PriorityQueue<NavigationNode>();
//		vertexQueue.add(source);
//
//		while (!vertexQueue.isEmpty()) {
//			NavigationNode u = vertexQueue.poll();
//
//			// Visit each edge exiting u
//			for (NavigationLink e : u.getOutLinks().values()) {
//				NavigationNode v = e.getToNode();
//				double weight = e.getWeight();
//				double distanceThroughU = u.minDistance + weight;
//				if (distanceThroughU < v.minDistance) {
//					vertexQueue.remove(v);
//					v.minDistance = distanceThroughU;
//					v.previous = u;
//					vertexQueue.add(v);
//				}
//			}
//		}
//	}
//	
	/**
	 * give shortest path between 2 two nodes
	 * @param from
	 * @param to
	 * @return
	 */
//	public static List<NavigationNode> getShortestPath(NavigationNode from, NavigationNode to) {
//		//computePaths(from);
//		computePath(from,to);
//		List<NavigationNode> path = new ArrayList<NavigationNode>();
//		for (NavigationNode vertex = to; vertex != null; vertex = vertex.previous)
//			path.add(vertex);
//		Collections.reverse(path);
//		
//		return path;
//	}
	
	public static List<NavigationLink> getShortestPathLink(NavigationNode from, NavigationNode to) {
		List<NavigationNode> toClean = computePath(from,to);
		List<NavigationLink> path = new LinkedList<NavigationLink>();
		for (NavigationNode vertex = to; vertex != from; vertex = vertex.getPrevious())
			path.add(vertex.getLinkToPrevious());
		Collections.reverse(path);
		cleanPath(toClean);
		return path;
	}
	
	private static void cleanPath(List<NavigationNode> toClean) {
		for(NavigationNode nn: toClean)	{
			nn.setPrevious(null);
			nn.setLinkToPrevious(null);
			nn.minDistance = Double.POSITIVE_INFINITY;
		}
	}

	public static List<NavigationNode> computePath(NavigationNode source,NavigationNode target) {
		source.minDistance = 0.;
		List<NavigationNode>  touched = new LinkedList<NavigationNode>();
		PriorityQueue<NavigationNode> vertexQueue = new PriorityQueue<NavigationNode>();
		vertexQueue.add(source);

		while (!vertexQueue.isEmpty()) {
			NavigationNode u = vertexQueue.poll();
			touched.add(u);
//			if (u.equals(target))
//		    	break;
			// Visit each edge exiting u
			for (NavigationLink e : u.getOutLinks().values()) {
				NavigationNode v = e.getToNode();
				double weight = e.getWeight();
				double distanceThroughU = u.minDistance + weight;
				if (distanceThroughU < v.minDistance) {
					vertexQueue.remove(v);
					v.minDistance = distanceThroughU;
					v.setPrevious(u);
					v.setLinkToPrevious(e);
					vertexQueue.add(v);
				}
			}
		}
		return touched;
	}
	
	public static void initApp(Context ctx) {
		Log.w("ARIANNA", "starting  app");
		try {
			SQLiteDatabase db = DbHelper.getInstance(ctx)
					.getWritableDatabase();
			DbHelper.getInstance(ctx).onUpgrade(db,
					db.getVersion(), db.getVersion() + 1);
		} catch (Exception e) {
			Log.w("ARIANNA", e);
		}
		/**
		 * EVENT
		 */
		Event[] events = { new Event(ctx, "E0", "name",
				"address", "image", "mail", "phoneNumber") };
		for (Event event : events) {
			Assert.assertTrue(event.save());
		}
		/**
		 * MAP LEVEL
		 */
		MapLevel[] maps = {
				new MapLevel(ctx, "M0", "name",
						"map_image", events[0]),
				new MapLevel(ctx, "M1", "name",
						"map_image", events[0]),
				new MapLevel(ctx, "M2", "name",
						"map_image", events[0]) };
		for (MapLevel mapLevel : maps) {
			Assert.assertTrue(mapLevel.save());
		}
		/**
		 * NAV NODE
		 */
		NavigationNode[] navNodes = {
				new NavigationNode(ctx, "NN0", 660, 75,
						maps[0]),// entrata
				new NavigationNode(ctx, "NN1", 720, 585,
						maps[0]),// 1bivio
				new NavigationNode(ctx, "NN2", 300, 525,
						maps[0]),// minosse
				new NavigationNode(ctx, "NN3", 300, 225,
						maps[0]),// ristorante
				new NavigationNode(ctx, "NN4", 540, 1185,
						maps[0]), // dedalo
				new NavigationNode(ctx, "NN5", 1260, 870,
						maps[0]), // cesso
				new NavigationNode(ctx, "NN6", 720, 1275,
						maps[0]),// uscita
		};
		for (NavigationNode navigationNode : navNodes) {
			Assert.assertTrue(navigationNode.save());
		}
		/**
		 * NAV LINK
		 */
		NavigationLink[] navLinks = {
				new NavigationLink(
						ctx,
						"NL0",
						navNodes[0],
						navNodes[1],
						maps[0],
						"[{\"x\": 660,\"y\": 75}, {\"x\": 660,\"y\": 105}, {\"x\": 780,\"y\": 105}, {\"x\": 780,\"y\": 165}, {\"x\": 660,\"y\": 165}, {\"x\": 660,\"y\": 345}, {\"x\": 600,\"y\": 345}, {\"x\": 600,\"y\": 405}, {\"x\": 720,\"y\": 405}, {\"x\": 720,\"y\": 345}, {\"x\": 780,\"y\": 345}, {\"x\": 780,\"y\": 405}, {\"x\": 900,\"y\": 405}, {\"x\": 900,\"y\": 525}, {\"x\": 840,\"y\": 525}, {\"x\": 840,\"y\": 465}, {\"x\": 660,\"y\": 465}, {\"x\": 660,\"y\": 525}, {\"x\": 780,\"y\": 525}, {\"x\": 780,\"y\": 585}, {\"x\": 720,\"y\": 585}]"),
				new NavigationLink(
						ctx,
						"NL1",
						navNodes[1],
						navNodes[2],
						maps[0],
						"[{\"x\": 720,\"y\": 585}, {\"x\": 600,\"y\": 585}, {\"x\": 600,\"y\": 525}, {\"x\": 300,\"y\": 525}]"),
				new NavigationLink(
						ctx,
						"NL2",
						navNodes[2],
						navNodes[3],
						maps[0],
						"[{\"x\": 300,\"y\": 525}, {\"x\": 300,\"y\": 345}, {\"x\": 360,\"y\": 345}, {\"x\": 360,\"y\": 285}, {\"x\": 300,\"y\": 285}, {\"x\": 300,\"y\": 225}]"),
				new NavigationLink(
						ctx,
						"NL3",
						navNodes[2],
						navNodes[4],
						maps[0],
						"[{\"x\": 300,\"y\": 525}, {\"x\": 240,\"y\": 525}, {\"x\": 240,\"y\": 765}, {\"x\": 300,\"y\": 765}, {\"x\": 300,\"y\": 825}, {\"x\": 240,\"y\": 825}, {\"x\": 240,\"y\": 885}, {\"x\": 300,\"y\": 885}, {\"x\": 300,\"y\": 945}, {\"x\": 180,\"y\": 945}, {\"x\": 180,\"y\": 1125}, {\"x\": 240,\"y\": 1125}, {\"x\": 240,\"y\": 1245}, {\"x\": 480,\"y\": 1245}, {\"x\": 480,\"y\": 1185}, {\"x\": 540,\"y\": 1185}]"),
				new NavigationLink(
						ctx,
						"NL4",
						navNodes[4],
						navNodes[6],
						maps[0],
						"[{\"x\": 540,\"y\": 1185}, {\"x\": 540,\"y\": 1245}, {\"x\": 660,\"y\": 1245}, {\"x\": 660,\"y\": 1185}, {\"x\": 720,\"y\": 1185}, {\"x\": 720,\"y\": 1275}]"),
				new NavigationLink(
						ctx,
						"NL5",
						navNodes[1],
						navNodes[5],
						maps[0],
						"[{\"x\": 720,\"y\": 585}, {\"x\": 720,\"y\": 645}, {\"x\": 660,\"y\": 645}, {\"x\": 660,\"y\": 825}, {\"x\": 600,\"y\": 825}, {\"x\": 600,\"y\": 945}, {\"x\": 660,\"y\": 945}, {\"x\": 660,\"y\": 885}, {\"x\": 780,\"y\": 885}, {\"x\": 780,\"y\": 825}, {\"x\": 840,\"y\": 825}, {\"x\": 840,\"y\": 945}, {\"x\": 1080,\"y\": 945}, {\"x\": 1080,\"y\": 825}, {\"x\": 1140,\"y\": 825}, {\"x\": 1140,\"y\": 1005}, {\"x\": 1200,\"y\": 1005}, {\"x\": 1200,\"y\": 945}, {\"x\": 1260,\"y\": 945}, {\"x\": 1260,\"y\": 870}]"),
				new NavigationLink(
						ctx,
						"NL6",
						navNodes[1],
						navNodes[0],
						maps[0],
						"[{\"x\": 720,\"y\": 585}, {\"x\": 780,\"y\": 585}, {\"x\": 780,\"y\": 525}, {\"x\": 660,\"y\": 525}, {\"x\": 660,\"y\": 465}, {\"x\": 840,\"y\": 465}, {\"x\": 840,\"y\": 525}, {\"x\": 900,\"y\": 525}, {\"x\": 900,\"y\": 405}, {\"x\": 780,\"y\": 405}, {\"x\": 780,\"y\": 345}, {\"x\": 720,\"y\": 345}, {\"x\": 720,\"y\": 405}, {\"x\": 600,\"y\": 405}, {\"x\": 600,\"y\": 345}, {\"x\": 660,\"y\": 345}, {\"x\": 660,\"y\": 165}, {\"x\": 780,\"y\": 165}, {\"x\": 780,\"y\": 105}, {\"x\": 660,\"y\": 105}, {\"x\": 660,\"y\": 75}]"),
				new NavigationLink(
						ctx,
						"NL7",
						navNodes[2],
						navNodes[1],
						maps[0],
						"[{\"x\": 300,\"y\": 525}, {\"x\": 600,\"y\": 525}, {\"x\": 600,\"y\": 585}, {\"x\": 720,\"y\": 585}]"),
				new NavigationLink(
						ctx,
						"NL8",
						navNodes[3],
						navNodes[2],
						maps[0],
						"[{\"x\": 300,\"y\": 225}, {\"x\": 300,\"y\": 285}, {\"x\": 360,\"y\": 285}, {\"x\": 360,\"y\": 345}, {\"x\": 300,\"y\": 345}, {\"x\": 300,\"y\": 525}]"),
				new NavigationLink(
						ctx,
						"NL9",
						navNodes[4],
						navNodes[2],
						maps[0],
						"[{\"x\": 540,\"y\": 1185}, {\"x\": 480,\"y\": 1185}, {\"x\": 480,\"y\": 1245}, {\"x\": 240,\"y\": 1245}, {\"x\": 240,\"y\": 1125}, {\"x\": 180,\"y\": 1125}, {\"x\": 180,\"y\": 945}, {\"x\": 300,\"y\": 945}, {\"x\": 300,\"y\": 885}, {\"x\": 240,\"y\": 885}, {\"x\": 240,\"y\": 825}, {\"x\": 300,\"y\": 825}, {\"x\": 300,\"y\": 765}, {\"x\": 240,\"y\": 765}, {\"x\": 240,\"y\": 525}, {\"x\": 300,\"y\": 525}]"),
				new NavigationLink(
						ctx,
						"NL10",
						navNodes[6],
						navNodes[4],
						maps[0],
						"[{\"x\": 720,\"y\": 1275}, {\"x\": 720,\"y\": 1185}, {\"x\": 660,\"y\": 1185}, {\"x\": 660,\"y\": 1245}, {\"x\": 540,\"y\": 1245}, {\"x\": 540,\"y\": 1185}]"),
				new NavigationLink(
						ctx,
						"NL11",
						navNodes[5],
						navNodes[1],
						maps[0],
						"[{\"x\": 1260,\"y\": 870}, {\"x\": 1260,\"y\": 945}, {\"x\": 1200,\"y\": 945}, {\"x\": 1200,\"y\": 1005}, {\"x\": 1140,\"y\": 1005}, {\"x\": 1140,\"y\": 825}, {\"x\": 1080,\"y\": 825}, {\"x\": 1080,\"y\": 945}, {\"x\": 840,\"y\": 945}, {\"x\": 840,\"y\": 825}, {\"x\": 780,\"y\": 825}, {\"x\": 780,\"y\": 885}, {\"x\": 660,\"y\": 885}, {\"x\": 660,\"y\": 945}, {\"x\": 600,\"y\": 945}, {\"x\": 600,\"y\": 825}, {\"x\": 660,\"y\": 825}, {\"x\": 660,\"y\": 645}, {\"x\": 720,\"y\": 645}, {\"x\": 720,\"y\": 585}]"), };
		for (NavigationLink navigationLink : navLinks) {
			Assert.assertTrue(navigationLink.save());
		}
		/**
		 * CATEGORY
		 */
		Category[] cats = {
				new Category(ctx, "C0", "Yellow", "ic_poi",
						false),
				new Category(ctx, "C1", "Red",
						"ic_toilets", true),
				new Category(ctx, "C2", "Blue", "ic_exit",
						true), };
		for (Category category : cats) {
			Assert.assertTrue(category.save());
		}
		/**
		 * POI
		 */
		PointOfInterest[] pois = {
				new PointOfInterest(ctx, "P1", "Entrance",
						"Please, do not feed the Minotaur",
						"This is the entrance of the labirinth", "entry.jpg",
						cats[2], navNodes[0], 660, 75),
				new PointOfInterest(ctx, "P2",
						"Ariadne", "subtitle", "description",
						"ariadne.png", cats[0], navNodes[2], 450, 495),
				new PointOfInterest(ctx, "P3", "Minotaur",
						"subtitle", "description", "minotaurDetail.png", cats[0],
						navNodes[2], 400, 598),
				new PointOfInterest(ctx, "P4",
						"'Chez le Minotaur'", "subtitle",
						"description", "minotaurChef.png", cats[1], navNodes[3], 300,
						225),
				new PointOfInterest(ctx, "P5",
						"Theseus", "subtitle", "description",
						"teseo_poi.png", cats[0], navNodes[2], 240, 450),
				new PointOfInterest(ctx, "P6", "Dedalo",
						"subtitle", "description", "icarus.jpg", cats[0],
						navNodes[4], 540, 1185),
				new PointOfInterest(ctx, "P7", "Toilets",
						"subtitle", "description", "toilet.jpg", cats[1],
						navNodes[5], 1260, 870),
				new PointOfInterest(ctx, "P8", "Exit",
						"subtitle", "description", "exit.jpg", cats[2],
						navNodes[6], 720, 1275),

		};
		for (PointOfInterest poi : pois)
			poi.setDescription("Lorem ipsum dolor sit amet, consectetur adipisicing elit, "
					+ "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, "
					+ "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute "
					+ "irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. "
					+ "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim "
					+ "id est laborum.");
		for (PointOfInterest pointOfInterest : pois) {
			Assert.assertTrue(pointOfInterest.save());

		}
		/**
		 * PATH
		 */
		Path[] paths = {
				new Path(ctx, "PATH0", "Throught the maze",
						"descrizione", events[0]),
				new Path(ctx, "PATH1", "Scaring but yummy!",
						"Lorem ipsum dolor sit amet, consectetur adipisicing elit, "
					+ "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, "
					+ "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.", events[0]),
				new Path(ctx, "PATH2", "Ruuuuuuuuun",
						"descrizione", events[0]) };
		List<PointOfInterest> l = new ArrayList<PointOfInterest>();
		l.add(pois[0]);
		l.add(pois[2]);
		l.add(pois[7]);
		paths[0].setPOIs(l);
		
		l = new ArrayList<PointOfInterest>();
		l.add(pois[0]);
		l.add(pois[1]);
		l.add(pois[2]);
		l.add(pois[4]);
		l.add(pois[3]);
		paths[1].setPOIs(l);
		
		l = new ArrayList<PointOfInterest>();
		l.add(pois[5]);
		l.add(pois[2]);
		l.add(pois[6]);
		paths[2].setPOIs(l);
		Assert.assertTrue(l.equals(paths[2].getPOIs()));
		for (Path path : paths) {
			Assert.assertTrue(path.save());
		}
	}
}
