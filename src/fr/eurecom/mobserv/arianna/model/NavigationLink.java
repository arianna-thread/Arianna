package fr.eurecom.mobserv.arianna.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * @author dani
 *
 */
public class NavigationLink extends Model implements BaseColumns{
	public static final String TABLE_NAME = "navigation_link";
	public static final String COLUMN_NAME_FROM_NODE = "from_node";
	public static final String COLUMN_NAME_TO_NODE = "to_node";
	public static final String COLUMN_NAME_MAP_LEVEL = "map_level";
    public static final String COLUMN_NAME_POINTS="points";
    public static final String[] COLUMNS_NAME={Model.COLUMN_NAME_URI,COLUMN_NAME_FROM_NODE,COLUMN_NAME_TO_NODE,
    	COLUMN_NAME_MAP_LEVEL,COLUMN_NAME_POINTS};
	
    public static final String SQL_CREATE_TABLE =
    	    "CREATE TABLE " + NavigationLink.TABLE_NAME + " (" +
			    Path._ID + Model.PRIMARY_KEY + Model.COMMA_SEP +
			    Model.COLUMN_NAME_URI + Model.TEXT_TYPE + UNIQUE+Model.COMMA_SEP +
			    NavigationLink.COLUMN_NAME_TO_NODE + Model.TEXT_TYPE + Model.COMMA_SEP +
			    NavigationLink.COLUMN_NAME_FROM_NODE + Model.TEXT_TYPE  + Model.COMMA_SEP +
			    NavigationLink.COLUMN_NAME_MAP_LEVEL + Model.TEXT_TYPE  + Model.COMMA_SEP +
			    NavigationLink.COLUMN_NAME_POINTS + Model.TEXT_TYPE + Model.COMMA_SEP +
			    "FOREIGN KEY("+NavigationLink.COLUMN_NAME_FROM_NODE+") REFERENCES "+ NavigationNode.TABLE_NAME +"("+Model.COLUMN_NAME_URI+")"+COMMA_SEP+ 
			    "FOREIGN KEY("+NavigationLink.COLUMN_NAME_TO_NODE+") REFERENCES "+ NavigationNode.TABLE_NAME +"("+Model.COLUMN_NAME_URI+")"+ COMMA_SEP+
			    "FOREIGN KEY("+NavigationLink.COLUMN_NAME_MAP_LEVEL+") REFERENCES "+ MapLevel.TABLE_NAME +"("+Model.COLUMN_NAME_URI+")"+ 
	    		")";
    
    private NavigationNode fromNode;
    private NavigationNode toNode;
    private MapLevel mapLevel;
    private String points;
   
    private double weight;
    
	
	/**
	 * @param context
	 * @param uri
	 * @param fromNode
	 * @param toNode
	 * @param mapLevel
	 * @param points
	 */
	public NavigationLink(Context context, String uri, NavigationNode fromNode,
			NavigationNode toNode, MapLevel mapLevel, String points) {
		super(context, uri);
		this.fromNode = fromNode;
		this.toNode = toNode;
		this.mapLevel = mapLevel;
		this.points = points;
		
		this.weight=1;
		
	}

	/**
	 * @param context
	 * @param cursor result from query to map DB entry and JAVA object
	 */
	public NavigationLink(Context context, Cursor cursor) {
		super(context,cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_URI)));
		this.toNode = (NavigationNode) Model.getByURI(NavigationNode.class, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_TO_NODE)),this.getContext());
		this.fromNode = (NavigationNode) Model.getByURI(NavigationNode.class, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_FROM_NODE)),this.getContext());
		this.mapLevel = (MapLevel) Model.getByURI(MapLevel.class, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_MAP_LEVEL)),this.getContext());;
		this.points = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_POINTS));
		
		this.weight=1;
		
	}
	
	
	
	/**
	 * @return the fromNode
	 */
	public NavigationNode getFromNode() {
		return fromNode;
	}

	/**
	 * @param fromNode the fromNode to set
	 */
	public void setFromNode(NavigationNode fromNode) {
		this.fromNode = fromNode;
	}

	/**
	 * @return the toNode
	 */
	public NavigationNode getToNode() {
		return toNode;
	}

	/**
	 * @param toNode the toNode to set
	 */
	public void setToNode(NavigationNode toNode) {
		this.toNode = toNode;
	}

	/**
	 * @return the mapLevel
	 */
	public MapLevel getMapLevel() {
		return mapLevel;
	}

	/**
	 * @param mapLevel the mapLevel to set
	 */
	public void setMapLevel(MapLevel mapLevel) {
		this.mapLevel = mapLevel;
	}

	@Override
	public boolean save() {
		// Gets the data repository in write mode
		SQLiteDatabase db = DbHelper.getInstance(this.getContext()).getWritableDatabase();
	
		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(Model.COLUMN_NAME_URI,this.getUri());
		values.put(NavigationLink.COLUMN_NAME_FROM_NODE,this.getFromNode().getUri());
		values.put(NavigationLink.COLUMN_NAME_TO_NODE,this.getToNode().getUri());
		values.put(NavigationLink.COLUMN_NAME_MAP_LEVEL,this.getMapLevel().getUri());
		values.put(NavigationLink.COLUMN_NAME_POINTS,this.getPoints());
		// Insert the new row, returning the primary key value of the new row
		long newRowId;
		newRowId = db.insert(
		         NavigationLink.TABLE_NAME,
		         NavigationLink.COLUMN_NAME_MAP_LEVEL,
		         values);
		return newRowId>=0;
	}
	
	@Override
	protected String getTableName() {
		return TABLE_NAME;
	}
	/**
	 * @return the points of a node link
	 */
	public String getPoints() {
		return points;
	}
	/**
	 * @param points the points to set
	 */
	public void setPoints(String points) {
		this.points = points;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((fromNode == null) ? 0 : fromNode.hashCode());
		result = prime * result
				+ ((mapLevel == null) ? 0 : mapLevel.hashCode());
		result = prime * result + ((points == null) ? 0 : points.hashCode());
		result = prime * result + ((toNode == null) ? 0 : toNode.hashCode());
		return result;
	}

	/**
	 * @return the weight
	 */
	public double getWeight() {
		return this.weight;
	}

	/**
	 * @param weight the weight to set
	 */
	public void setWeight(double weight) {
		this.weight = weight;
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
		NavigationLink other = (NavigationLink) obj;
		if (fromNode == null) {
			if (other.fromNode != null)
				return false;
		} else if (!fromNode.equals(other.fromNode))
			return false;
		if (mapLevel == null) {
			if (other.mapLevel != null)
				return false;
		} else if (!mapLevel.equals(other.mapLevel))
			return false;
		if (points == null) {
			if (other.points != null)
				return false;
		} else if (!points.equals(other.points))
			return false;
		if (toNode == null) {
			if (other.toNode != null)
				return false;
		} else if (!toNode.equals(other.toNode))
			return false;
		return true;
	}


}
