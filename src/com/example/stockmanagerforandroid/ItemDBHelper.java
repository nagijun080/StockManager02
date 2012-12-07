package com.example.stockmanagerforandroid;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ItemDBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "itemDatabase.db";
	private static final int DATABASE_VERSION = 1;
	private static ItemDBHelper itemDBHelper;
	
	//mac�̏��i�摜ID�z��
	public Integer[] itemId = { R.drawable.imac_215, R.drawable.imac_27,
								R.drawable.ipad_mini_bk, R.drawable.ipad_mini_whi,
								R.drawable.macbook_13, R.drawable.macbook_13_retina };
	//mac�̏��i���z��(itemId�ƑΉ�������)
	public String[]  itemName = { "21.5�C���`iMac", "27�C���`iMac", 
			"iPadmini �u���b�N���X�g���[�g","iPadmini �z���C�g���V���o�[", 
			"MacBookPro �P�R�C���`", "MacBookPro �P�R�C���` Retina", };
	//���i�̒l�i�z��
	public Integer[] itemValue = { 128800, 144000, 28800, 28800, 128800, 150000, };
	//���i���z��
	public String[] itemData = { "�N�A�b�h�R�AIntel Core i5", "�N�A�b�h�R�AIntel Core i5",
								"�f���A���R�AA5�`�b�v", "�f���A���R�AA5�`�b�v",
								"�f���A���R�AIntel Core i7", "�f���A���R�AIntel Core i7",	};
	//���i�̃W�������z��
	public String[] genre = { "�Ȃ�", "DeskTop", "Tablet", "NoteBook" };
	
	public ItemDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		String sql = "CREATE TABLE itemDB ( _ID INTEGER PRIMARY KEY AUTOINCREMENT,"
											+ " itemId INTEGER, itemName TEXT, itemValue INTEGER, itemData TEXT, genre TEXT);";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u

	}

}
