package fr.eurecom.mobserv.arianna.model;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class Event extends Model implements BaseColumns {
	public final static String TABLE_NAME = "company";
	public final static String COLUMN_NAME_NAME = "name";
	public final static String COLUMN_NAME_ADDRESS = "address";
	public final static String COLUMN_NAME_IMAGE = "image";
	public final static String COLUMN_NAME_MAIL = "mail";
	public final static String COLUMN_NAME_PHONE_NUMBER = "phone_number";

	public static final String[] COLUMNS_NAME = { Model.COLUMN_NAME_URI,
			COLUMN_NAME_NAME, COLUMN_NAME_ADDRESS, COLUMN_NAME_IMAGE,
			COLUMN_NAME_MAIL, COLUMN_NAME_PHONE_NUMBER };

	public final static String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
			+ " (" + _ID + PRIMARY_KEY + COMMA_SEP + COLUMN_NAME_URI
			+ TEXT_TYPE + UNIQUE + COMMA_SEP + COLUMN_NAME_NAME + TEXT_TYPE
			+ COMMA_SEP + COLUMN_NAME_ADDRESS + TEXT_TYPE + COMMA_SEP
			+ COLUMN_NAME_IMAGE + TEXT_TYPE + COMMA_SEP + COLUMN_NAME_MAIL
			+ TEXT_TYPE + COMMA_SEP + COLUMN_NAME_PHONE_NUMBER + TEXT_TYPE
			+ " )";

	private String name;
	private String address;
	private String image;
	private String mail;
	private String phoneNumber;
	// Lazy initialization for levels
	// only when they are required you query db
	private Map<String, MapLevel> levels;
	private Map<String, Path> paths;
	private Map<String, PointOfInterest> pois;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param context
	 * @param uri
	 * @param name
	 * @param address
	 * @param image
	 * @param mail
	 * @param phoneNumber
	 */
	public Event(Context context, String uri, String name, String address,
			String image, String mail, String phoneNumber) {
		super(context, uri);
		this.name = name;
		this.address = address;
		this.image = image;
		this.mail = mail;
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @param context
	 * @param cursor
	 *            result from query to map DB entry and JAVA object
	 */
	public Event(Context context, Cursor cursor) {
		super(context, cursor.getString(cursor
				.getColumnIndexOrThrow(COLUMN_NAME_URI)));
		this.name = cursor.getString(cursor
				.getColumnIndexOrThrow(COLUMN_NAME_NAME));
		this.address = cursor.getString(cursor
				.getColumnIndexOrThrow(COLUMN_NAME_ADDRESS));
		this.image = cursor.getString(cursor
				.getColumnIndexOrThrow(COLUMN_NAME_IMAGE));
		this.mail = cursor.getString(cursor
				.getColumnIndexOrThrow(COLUMN_NAME_MAIL));
		this.phoneNumber = cursor.getString(cursor
				.getColumnIndexOrThrow(COLUMN_NAME_PHONE_NUMBER));
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param addres
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the image
	 */
	public String getImage() {
		return image;
	}

	/**
	 * @param image
	 *            the image to set
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * @return the mail
	 */
	public String getMail() {
		return mail;
	}

	/**
	 * @param mail
	 *            the mail to set
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber
	 *            the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @return the levels
	 */
	public Map<String, MapLevel> getLevels() {
		if (this.levels == null || this.levels.isEmpty()) {
			Map<String, MapLevel> mapLevels = (Map) Model.getByParam(
					MapLevel.class, MapLevel.COLUMN_NAME_EVENT_URI,
					this.getUri(), this.getContext());
			this.levels = mapLevels;
		}
		return this.levels;
	}

	/**
	 * @param levels
	 *            the levels to set
	 */
	public void setLevels(Map<String, MapLevel> levels) {
		this.levels = levels;
	}

	@Override
	public boolean save() {
		SQLiteDatabase db = DbHelper.getInstance(this.getContext())
				.getWritableDatabase();

		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(Model.COLUMN_NAME_URI, this.getUri());
		values.put(COLUMN_NAME_NAME, this.getName());
		values.put(COLUMN_NAME_ADDRESS, this.getAddress());
		values.put(COLUMN_NAME_IMAGE, this.getImage());
		values.put(COLUMN_NAME_MAIL, this.getMail());
		values.put(COLUMN_NAME_PHONE_NUMBER, this.getPhoneNumber());

		// Insert the new row, returning the primary key value of the new row
		long newRowId;
		newRowId = db.insert(Event.TABLE_NAME, null, values);
		return newRowId >= 0;
	}

	@Override
	protected String getTableName() {
		return TABLE_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((image == null) ? 0 : image.hashCode());
		result = prime * result + ((levels == null) ? 0 : levels.hashCode());
		result = prime * result + ((mail == null) ? 0 : mail.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
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
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (image == null) {
			if (other.image != null)
				return false;
		} else if (!image.equals(other.image))
			return false;
		if (levels == null) {
			if (other.levels != null)
				return false;
		} else if (!levels.equals(other.levels))
			return false;
		if (mail == null) {
			if (other.mail != null)
				return false;
		} else if (!mail.equals(other.mail))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (phoneNumber == null) {
			if (other.phoneNumber != null)
				return false;
		} else if (!phoneNumber.equals(other.phoneNumber))
			return false;
		return true;
	}

	/**
	 * @return the paths
	 */
	public Map<String, Path> getPaths() {
		if (this.paths == null || this.paths.isEmpty()) {
			Map<String, Path> paths = (Map) Model.getByParam(Path.class,
					Path.COLUMN_NAME_EVENT_URI, this.getUri(),
					this.getContext());
			this.paths = paths;
		}
		return paths;
	}

	/**
	 * @param paths
	 *            the paths to set
	 */
	public void setPaths(Map<String, Path> paths) {
		this.paths = paths;
	}

	/**
	 * @return the pois
	 */
	public Map<String, PointOfInterest> getPois() {
		if (this.pois == null || this.pois.isEmpty()) {
			Map<String, PointOfInterest> pois = new HashMap<String, PointOfInterest>();
			for (MapLevel mapLevel : this.getLevels().values()) {
				for (NavigationNode navigationNode : mapLevel
						.getNavigationNodes().values()) {
					pois.putAll(navigationNode.getPois());
				}
			}
			this.pois = pois;
		}
		return this.pois;
	}

	/**
	 * @param pois
	 *            the pois to set
	 */
	public void setPois(Map<String, PointOfInterest> pois) {
		this.pois = pois;
	}

}
