package com.example.stockmanagerforandroid;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ItemGenreDBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "itemGenreDB.db";
	private static final int DATABASE_VERSION = 1;
	private static ItemGenreDBHelper itemGenreDBHelper;
	
	public ItemGenreDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		String sql = "CREATE TABLE itemGenreDB ( _ID INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ " genre TEXT, itemValue INTEGER);";
		db.execSQL(sql);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u

	}
	

}
