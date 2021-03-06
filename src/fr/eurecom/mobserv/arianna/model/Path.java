package fr.eurecom.mobserv.arianna.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * @author dani
 * 
 */
public class Path extends Model implements BaseColumns {
	public static final String TABLE_NAME = "path";
	public static final String COLUMN_NAME_TITLE = "title";
	public static final String COLUMN_NAME_DESC = "description";
	public static final String COLUMN_NAME_EVENT_URI = "event";

	public static final String[] COLUMNS_NAME = { Model.COLUMN_NAME_URI,
			Path.COLUMN_NAME_TITLE, Path.COLUMN_NAME_DESC,
			Path.COLUMN_NAME_EVENT_URI };

	public static final String SQL_CREATE_TABLE = "CREATE TABLE "
			+ Path.TABLE_NAME + " (" + Path._ID + Model.PRIMARY_KEY
			+ Model.COMMA_SEP + Model.COLUMN_NAME_URI + Model.TEXT_TYPE
			+ UNIQUE + Model.COMMA_SEP + Path.COLUMN_NAME_TITLE
			+ Model.TEXT_TYPE + Model.COMMA_SEP + Path.COLUMN_NAME_DESC
			+ Model.TEXT_TYPE + COMMA_SEP + Path.COLUMN_NAME_EVENT_URI
			+ Model.TEXT_TYPE + COMMA_SEP + "FOREIGN KEY("
			+ COLUMN_NAME_EVENT_URI + ") REFERENCES " + Event.TABLE_NAME + "("
			+ Model.COLUMN_NAME_URI + ")" + ")";

	private String title;
	private String description;
	private List<PointOfInterest> POIs;
	private Event event;

	/**
	 * Path of PointOfInterest
	 * 
	 * @param context
	 * @param uri
	 * @param title
	 * @param description
	 * @param event
	 */
	public Path(Context context, String uri, String title, String description,
			Event e) {
		super(context, uri);
		this.title = title;
		this.description = description;
		this.POIs = new ArrayList<PointOfInterest>();
		this.event = e;
	}

	/**
	 * @param context
	 *            of app
	 * @param cursor
	 *            result from query to map DB entry and JAVA object
	 */
	public Path(Context context, Cursor cursor) {
		super(context, cursor.getString(cursor
				.getColumnIndexOrThrow(COLUMN_NAME_URI)));
		this.title = cursor.getString(cursor
				.getColumnIndexOrThrow(COLUMN_NAME_TITLE));
		this.description = cursor.getString(cursor
				.getColumnIndexOrThrow(COLUMN_NAME_DESC));
		this.loadPOIs();
		this.event = (Event) Model.getByURI(Event.class,
				cursor.getString(cursor
						.getColumnIndexOrThrow(COLUMN_NAME_EVENT_URI)), this
						.getContext());

	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the POIs
	 */
	public List<PointOfInterest> loadPOIs() {
		if (this.POIs == null || this.POIs.isEmpty()) {
			this.POIs = new ArrayList<PointOfInterest>();
			SQLiteDatabase db = DbHelper.getInstance(this.getContext())
					.getReadableDatabase();
			String table = Model.TABLE_NAME_POI_PATH_RELATION;
			String[] projection = { Model.COLUMN_NAME_POI_URI,Model.COLUMN_NAME_POSITION };
			// Define 'where' part of query.
			String selection = Model.COLUMN_NAME_PATH_URI + "=?";
			// Specify arguments in placeholder order.
			String[] selectionArgs = { this.getUri() };
			String order = Model.COLUMN_NAME_POSITION;
			Cursor cur = db.query(table, // The table to query
					projection, // The columns to return
					selection, // The columns for the WHERE clause
					selectionArgs, // The values for the WHERE clause
					null, // don't group the rows
					null, // don't filter by row groups
					order // The sort order
					);
			cur.moveToFirst();
			while (cur.isAfterLast() == false) {
				String uriPoi = cur.getString(cur
						.getColumnIndex(Model.COLUMN_NAME_POI_URI));
				this.POIs.add((PointOfInterest) Model.getByURI(
						PointOfInterest.class, uriPoi, this.getContext()));
				cur.moveToNext();
			}
		}
		return this.POIs;
	}

	/**
	 * @param POIs
	 *            the POIs to set
	 */
	public boolean savePOIs(List<PointOfInterest> pOIs) {
		boolean result=true;
		if (pOIs != null && !pOIs.isEmpty()) {
			SQLiteDatabase db = DbHelper.getInstance(this.getContext())
					.getWritableDatabase();
			db.beginTransaction();
			try {
				int i=0;
				for (PointOfInterest pointOfInterest : pOIs) {
					// Create a new map of values, where column names are the
					// keys
					ContentValues value = new ContentValues();
					value.put(Model.COLUMN_NAME_POI_URI,
							pointOfInterest.getUri());
					value.put(Model.COLUMN_NAME_PATH_URI, this.getUri());
					value.put(Model.COLUMN_NAME_POSITION, i++);
					long newID = db.insert(Model.TABLE_NAME_POI_PATH_RELATION,
							null, value);
					if (newID <= 0) {
							result=false;
						//throw new SQLException("Failed to insert row into "
						//		+ Model.TABLE_NAME_POI_PATH_RELATION);
					}
				}
				db.setTransactionSuccessful();
			} finally {
				db.endTransaction();
			}
			this.POIs = new ArrayList<PointOfInterest>(pOIs);
		}
		return result;
	}

	@Override
	public boolean save() {
		// Gets the data repository in write mode
		SQLiteDatabase db = DbHelper.getInstance(this.getContext())
				.getWritableDatabase();

		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(Model.COLUMN_NAME_URI, this.getUri());
		values.put(Path.COLUMN_NAME_TITLE, this.getTitle());
		values.put(Path.COLUMN_NAME_DESC, this.getDescription());
		values.put(Path.COLUMN_NAME_EVENT_URI, this.getEvent().getUri());
		// Insert the new row, returning the primary key value of the new row
		//TODO
		boolean result = this.savePOIs(this.loadPOIs());
		long newRowId;
		newRowId = db.insert(Path.TABLE_NAME, null, values);
		return (newRowId >= 0) && result;
	}

	@Override
	protected String getTableName() {
		return TABLE_NAME;
	}

	/**
	 * @return the pOIs
	 */
	public List<PointOfInterest> getPOIs() {
		return POIs;
	}

	/**
	 * @param pOIs the pOIs to set
	 */
	public void setPOIs(List<PointOfInterest> pOIs) {
		POIs = pOIs;
	}

	/**
	 * @return the event
	 */
	public Event getEvent() {
		return event;
	}

	/**
	 * @param event
	 *            the event to set
	 */
	public void setEvent(Event event) {
		this.event = event;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((POIs == null) ? 0 : POIs.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((event == null) ? 0 : event.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Path other = (Path) obj;
		if (POIs == null) {
			if (other.POIs != null)
				return false;
		} else if (!POIs.equals(other.POIs))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (event == null) {
			if (other.event != null)
				return false;
		} else if (!event.equals(other.event))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}
	
	public List<NavigationLink> getNavigationList(){
		List<NavigationLink> returnList = new LinkedList<NavigationLink>();
		List<PointOfInterest> pois = this.getPOIs();
		
		for( int i = 0; i < pois.size()-1; i++){
			NavigationNode from = pois.get(i).getNavNode();
			NavigationNode to = pois.get(i+1).getNavNode();
			List<NavigationLink> current = Model.getShortestPathLink(from, to);
			returnList.addAll(current);
		}
		
		return returnList;
	}
	
}
