package com.example.stockmanagerforandroid;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ItemDBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "itemDatabase.db";
	private static final int DATABASE_VERSION = 1;
	private static ItemDBHelper itemDBHelper;
	
	//macの商品画像ID配列
	public Integer[] itemId = { R.drawable.imac_215, R.drawable.imac_27,
								R.drawable.ipad_mini_bk, R.drawable.ipad_mini_whi,
								R.drawable.macbook_13, R.drawable.macbook_13_retina };
	//macの商品名配列(itemIdと対応させる)
	public String[]  itemName = { "21.5インチiMac", "27インチiMac", 
			"iPadmini ブラック＆ストレート","iPadmini ホワイト＆シルバー", 
			"MacBookPro １３インチ", "MacBookPro １３インチ Retina", };
	//商品の値段配列
	public Integer[] itemValue = { 128800, 144000, 28800, 28800, 128800, 150000, };
	//商品情報配列
	public String[] itemData = { "クアッドコアIntel Core i5", "クアッドコアIntel Core i5",
								"デュアルコアA5チップ", "デュアルコアA5チップ",
								"デュアルコアIntel Core i7", "デュアルコアIntel Core i7",	};
	//商品のジャンル配列
	public String[] genre = { "なし", "DeskTop", "Tablet", "NoteBook" };
	
	public ItemDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO 自動生成されたメソッド・スタブ
		String sql = "CREATE TABLE itemDB ( _ID INTEGER PRIMARY KEY AUTOINCREMENT,"
											+ " itemId INTEGER, itemName TEXT, itemValue INTEGER, itemData TEXT, genre TEXT);";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
