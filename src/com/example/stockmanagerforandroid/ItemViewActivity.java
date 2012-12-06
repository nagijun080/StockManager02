package com.example.stockmanagerforandroid;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ItemViewActivity extends Activity {

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
	
	ItemDBHelper itemDBH;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.item_view_layout);
		saveItemDB();
		showItemDB();
	}

	@Override
	protected void onStop() {
		// TODO 自動生成されたメソッド・スタブ
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.menu_layout, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.ownerData:
	    	Intent clientIntent = new Intent();
    		clientIntent.setClass(this, ClientViewActivity.class);
    		startActivity(clientIntent);
	        return true;
	    case R.id.itemList:
	    	Intent itemViewIntent = new Intent();
    		itemViewIntent.setClass(this, ItemViewActivity.class);
    		startActivity(itemViewIntent);
	        return true;
	    case R.id.ownerHistory:
	    	Intent historyViewIntent = new Intent();
	    	historyViewIntent.setClass(this, HistoryViewActivity.class);
	    	startActivity(historyViewIntent);
	    	return true;
	    }
	    return false;
	}
	
	public void saveItemDB() {
		Log.d("saveItemDB()","01");
		for (int i = 0;i < itemId.length;i++) {	
			itemDBH = new ItemDBHelper(this);
			SQLiteDatabase db = itemDBH.getWritableDatabase();
			Log.d("saveItemDB()","0" + i);
			ContentValues values = new ContentValues();
			values.put("itemId", itemId[i]);
			values.put("itemName", itemName[i]);
			values.put("itemValue", itemValue[i]);
			values.put("itemData", itemData[i]);
			db.insertOrThrow("itemDB", null, values);
			itemDBH.close();
		}
	}
	
	
	public void showItemDB() {
		Log.d("showItemDB()","11");
		SQLiteDatabase db = itemDBH.getWritableDatabase();
		ListView listView1 = (ListView)findViewById(R.id.imageList);
		String[] colmns = { "itemId", "itemName", "itemValue", "itemData" };
		Cursor c = db.query("itemDB", colmns, null, null, null, null, null);
		c.moveToFirst();
		Log.d("showItemDB()","12" + c.getString(1));
		//商品の情報が全部入った２次元配列
		String[][] item = new String[c.getCount()][colmns.length];
		for (int i = 0;i < c.getCount();i++) {
			for (int j = 0;j < colmns.length;j++) {
				item[i][j] = c.getString(j);
			}
			c.moveToNext();
		}
		//データの作成
		List<CustomData> objects = new ArrayList<CustomData>();
		for (int i = 0;i < c.getCount();i++) {
			CustomData customItem = new CustomData();
			for (int j = 0;j < colmns.length;j++) {
				switch (j) {
				case 0:
					customItem.setItemId(Integer.valueOf(item[i][j]));
					break;
				case 1:
					customItem.setItemName(item[i][j]);
					break;
				case 2:
					customItem.setItemValue(Integer.valueOf(item[i][j]));
					break;
				case 3:
					customItem.setItemData(item[i][j]);
					break;
				}
			}
			objects.add(customItem);
		}
		c.close();
		
		ItemCustomAdapter itemCurAda = new ItemCustomAdapter(this, 0, objects);
		listView1.setAdapter(itemCurAda);
	}
	
}
