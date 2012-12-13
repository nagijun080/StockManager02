package com.example.stockmanagerforandroid;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class OrderSetDBHelper extends SQLiteOpenHelper {
	
	private static final String DB_NAME = "orderSetDatabase.db";
	private static final int DB_VERSION = 1;
	private static OrderSetDBHelper orderSetDBHelper;

	public OrderSetDBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		String sql = "CREATE TABLE orderSetDBTable ( _ID INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ " ownerId INTEGER, userId INTEGER, itemId INTEGER, sumValue INTEGER);";
		db.execSQL(sql);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u

	}

}
