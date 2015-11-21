package com.fornys.hostingkart;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	// Database version
	private static final int DATABASE_VERSION = 1;

	// Database name
	private static final String DATABASE_NAME = "domainList";

	// Table name
	private static final String TABLE_DOMAIN = "domains";

	// domains table columns names
	private static final String KEY_ID = "id";
	private static final String KEY_DOMAIN = "domain";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Create table
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_DOMAIN_TABLE = "CREATE TABLE " + TABLE_DOMAIN + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_DOMAIN + " TEXT" + ")";
		db.execSQL(CREATE_DOMAIN_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if exist
		db.execSQL("DROP TABLE IF EXIST " + TABLE_DOMAIN);
		// create a table again
		onCreate(db);
	}

	// Adding new contact
	public void addDomain(Domain domain) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_DOMAIN, domain.getDomain()); // Domain Name

		// Inserting Row
		db.insert(TABLE_DOMAIN, null, values);
		db.close(); // Closing database connection
	}

	// Getting All Contacts
	public List<Domain> getAllContacts() {
		List<Domain> contactList = new ArrayList<Domain>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_DOMAIN
				+ " ORDER BY id DESC";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Domain domain = new Domain();
				domain.setID(Integer.parseInt(cursor.getString(0)));
				domain.setDomain(cursor.getString(1));
				// Adding contact to list
				contactList.add(domain);
			} while (cursor.moveToNext());
		}

		// db.close();

		return contactList;
	}

	// Deleting single contact
	public void deleteContact(Domain domain) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_DOMAIN, KEY_DOMAIN + " = ?",
				new String[] { String.valueOf(domain.getDomain()) });
		//System.out.println("Deleting");
		db.close();
	}

	// Getting contacts Count
	public int getContactsCount() {
		String countQuery = "SELECT * FROM " + TABLE_DOMAIN;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		// cursor.close();
		// return count
		return cursor.getCount();
	}

}
