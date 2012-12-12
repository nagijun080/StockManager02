package com.example.stockmanagerforandroid;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDBHelper extends SQLiteOpenHelper {
	
	private static final String DB_NAME	= "userDataBase.db";
	private static final int DB_VERSION = 1;
	private static UserDBHelper userDBHelper;

	public UserDBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		String sql = "CREATE TABLE userDBTable( _ID INTEGER PRIMARY KEY AUTOINCREMENT, "
					 + " ownerId INTEGER UNIQUE, company TEXT, name TEXT, telNumber TEXT, date TEXT, "
					 + "postNumber TEXT, address TEXT);";
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u

	}

}
