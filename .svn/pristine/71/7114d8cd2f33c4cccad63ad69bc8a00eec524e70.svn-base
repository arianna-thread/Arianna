/**
 * 
 */
package fr.eurecom.mobserv.arianna.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author artoale
 * 
 */
public class DbHelper extends SQLiteOpenHelper {
	private static DbHelper instance = null;

	/**
	 * Private constructor for the Singleton pattern
	 * 
	 * @param context
	 *            The android application context
	 */
	private DbHelper(Context context) {
		super(context, Model.DATABASE_NAME, null, Model.DATABASE_VERSION);
	}

	/**
	 * @author uccio
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		Model.createModels();
		for (String statement : Model.SQLCreateTableStatements) {
			db.execSQL(statement);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// This database is only a cache for online data, so its upgrade policy
		// is
		// to simply to discard the data and start over
		if (newVersion > oldVersion){
			for (String statement : Model.SQLDropTableStatements) {
				db.execSQL(statement);
			}
		onCreate(db);
		}
	}

	/**
	 * Access the DbHelper singleton instance
	 * 
	 * @param context
	 *            The Application
	 * @return the singleton instance
	 */
	public static DbHelper getInstance(Context context) {
		if (instance == null) {
			instance = new DbHelper(context);
		}
		return instance;
	}

}
