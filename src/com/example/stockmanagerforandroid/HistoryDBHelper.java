package com.example.stockmanagerforandroid;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class HistoryDBHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "historyDB.db";
	private static final int DB_VERSION = 1;
	private static HistoryDBHelper historyDBHelper;

	public HistoryDBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		String sql = "CREATE TABLE historyDBTable ( _ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"status TEXT, orderId INTEGER UNIQUE, ownerId TEXT, userId TEXT);";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u

	}

}
