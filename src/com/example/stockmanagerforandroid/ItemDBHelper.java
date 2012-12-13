package com.example.stockmanagerforandroid;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ItemDBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "itemDatabase.db";
	private static final int DATABASE_VERSION = 1;
	private static ItemDBHelper itemDBHelper;
	
	
	public ItemDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO 自動生成されたメソッド・スタブ
		String sql = "CREATE TABLE itemDB ( _ID INTEGER PRIMARY KEY AUTOINCREMENT,"
											+ " itemId TEXT UNIQUE, itemImageId INTEGER UNIQUE," +
											" itemName TEXT, itemValue INTEGER, itemData TEXT, genre TEXT);";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
