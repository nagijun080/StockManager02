package com.example.stockmanagerforandroid;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ItemViewActivity extends Activity implements OnClickListener, DialogInterface.OnClickListener{

	//macの商品画像ID配列
	public Integer[] itemId = { R.drawable.imac_215, R.drawable.imac_27,
								R.drawable.ipad_mini_bk, R.drawable.ipad_mini_whi,
								R.drawable.macbook_13, R.drawable.macbook_13_retina };
	//macの商品名配列(itemIdと対応させる)
	public String[]  itemName = { "21.5インチiMac", "27インチiMac", 
			"iPadmini ブラック＆ストレート","iPadmini ホワイト＆シルバー", 
			"MacBookPro １３インチ", "MacBookPro １３インチ Retina", };
	//商品の値段配列
	public Integer[] itemValue = { 128800, 144000, 28800, 30800, 130000, 150000, };
	//商品情報配列
	public String[] itemData = { "クアッドコアIntel Core i5", "クアッドコアIntel Core i5",
								"デュアルコアA5チップ", "デュアルコアA5チップ",
								"デュアルコアIntel Core i7", "デュアルコアIntel Core i7",	};
	//商品のジャンル配列
	public String[] genre = { "なし", "DeskTop", "Tablet", "NoteBook" };
	
	ItemDBHelper itemDBH;
	ItemGenreDBHelper itemGenreDBH;
	
	public ArrayAdapter<String> genAdapter;
	public ArrayAdapter<Integer> valueAdapter;
	
	//商品を検索するボタン
	public ImageButton genreButton;
	public ImageButton valueButton;
	public ImageButton rowButton;
	//商品を検索するボタンをクリックしたらダイアログを表示するインスタンス
	public AlertDialog.Builder genreDialog;
	public Builder valueDialog;
	public AlertDialog.Builder rowDialog;
	
	//Dialogのリスト項目ID
	Integer genreId = 0;
	Integer valueId;
	Integer alignId = 0;
	
	//どのダイアログが使われているかチェックする
	//ダイアログを表示するボタンを押した時に固定値を入れる
	int checkNum;
	//ダイアログナンバー(固定値)
	private final int GENRE = 0;
	private final int VALUE = 1;
	private final int ROW = 2;
	
	//itemDBデータベースを使って検索するときの変数
	public String select;	
	public String[] selectArgs;
	public String orderBy;
	//価格帯設定に使う変数
	private String min;
	private String max;
	public View itemValVw;
	
	public View itemRowVw;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreate(savedInstanceState);	
		//LayoutのvalueViewだけ改行処理
		setContentView(R.layout.item_view_layout);
		TextView valueV = (TextView)findViewById(R.id.valueView);
		valueV.setText("価格を\n絞り込む");
		
		//商品を検索するボタンにクリックリスナー登録する
		genreButton = (ImageButton)findViewById(R.id.genruButton);
		genreButton.setOnClickListener(this);
		valueButton = (ImageButton)findViewById(R.id.valueButton);
		valueButton.setOnClickListener(this);
		rowButton = (ImageButton)findViewById(R.id.rowButton);
		rowButton.setOnClickListener(this);
		//itemDBにデータを入れる
		saveItemDB();
		
		//itemDBテーブルのデータベースに入っているデータを全部出す
		showItemDB(null, null, null);
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
	//ItemDBHelperのitemDBテーブルにinsertする
	public void saveItemDB() {
		Log.d("saveItemDB()","first");
		
		for (int i = 0;i < itemId.length;i++) {				
			Log.d("saveItemDB()","0" + i);
			ContentValues values = new ContentValues();
			values.put("itemId", itemId[i]);
			values.put("itemName", itemName[i]);
			values.put("itemValue", itemValue[i]);
			values.put("itemData", itemData[i]);
			//itemName[0]とitemName[1]がgenre[1]
			//itemName[2]とitemName[3]がgenre[2]
			//itemName[4]とitemName[5]がgenre[3]
			Log.d("saveItemDB", "01");
			if (itemName[0].equals(itemName[i]) || itemName[1].equals(itemName[i])) {
				values.put("genre", genre[1]);
				Log.d("itemName[0or1}", itemName[i]);
			} else if (itemName[2].equals(itemName[i]) || itemName[3].equals(itemName[i])) {
				values.put("genre", genre[2]);
				Log.d("itemName[2or3}", itemName[i]);
			} else if (itemName[4].equals(itemName[i]) || itemName[5].equals(itemName[i])) {
				values.put("genre", genre[3]);
				Log.d("itemName[4or5}", itemName[i]);
			}
			
			itemDBH = new ItemDBHelper(this);
			SQLiteDatabase db = itemDBH.getWritableDatabase();
			Log.d("itemId[i]", itemId[i].toString());
			Integer checkRec = 0;
			try  {
				checkRec = Long.valueOf(db.insertOrThrow("itemDB", null, values)).intValue();
				Log.d("db.insert","checkIns : " + String.valueOf(checkRec));
			} catch (SQLiteConstraintException e) {
				checkRec = db.update("itemDB", values, "itemId = ?", new String[] { itemId[i].toString(), });
				Log.d("db.update","checkRec : " + String.valueOf(checkRec));
			}
			itemDBH.close();
//			Integer checkRec = null;
//			if (cur.getString(0).equals(itemId[i])) {
//				checkRec = (Integer)db.update("itemDB", values, "itemId = ?", new String[] { itemId[i].toString(), });
//				Log.d("db.update","checkRec : " + String.valueOf(checkRec));
//			} else {
//				checkRec = Long.valueOf(db.insert("itemDB", null, values)).intValue();				
//				Log.d("db.insert","checkIns : " + String.valueOf(checkRec));
//			}
//			cur.moveToNext();
		}
	}
	
	//itemDBテーブルから商品を取り出して、表示
	//引数1:select 条件カラム
	//引数2:selectArgs　条件カラムに代入する文字列
	/*genre別に*/
	public void showItemDB(String select, String[] selectArgs, String orderBy){
		SQLiteDatabase db = itemDBH.getWritableDatabase();
		ListView imageList = (ListView)findViewById(R.id.imageList);
		String[] colmns = { "itemId", "itemName", "itemValue", "itemData", "genre"};
		Cursor c = db.query("itemDB", colmns, select, selectArgs, null, null, orderBy);
		//商品の情報が全部入った２次元配列
		String[][] item = new String[c.getCount()][colmns.length];
		if (c != null && c.getCount() != 0) {
			c.moveToFirst();
			Log.d("showItemDB()","12" + c.getString(1));
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
					default:
						break;
					}
				}
				objects.add(customItem);
			}
			c.close();
			
			ItemCustomAdapter itemCurAda = new ItemCustomAdapter(this, 0, objects);
			imageList.setAdapter(itemCurAda);
		} else {
			imageList.setAdapter(null);
		}
	}
	//Dialogを表示するボタンの処理
	public void onClick(View view) {
		//商品を検索するボタンをクリックしたらダイアログを表示する		
		if (genreButton.equals(view)) {
			//ジャンルボタンを押した時の処理
			checkNum = GENRE;
			showGenDia();
		} else if (valueButton.equals(view)) {
			//条件を価格ボタンにした時の設定
			checkNum = VALUE;
			Log.d("onClick().valueButton","01");
			showValDia();
		} else if (rowButton.equals(view)) {
			//商品並びを変えるボタンを押した時の処理
			checkNum = ROW;
			showRowDia();
		}
	}
	
	//ダイアログの中にあるボタンの処理(価格設定の時はなし)
	public void onClick(DialogInterface dialog, int which) {
		// TODO 自動生成されたメソッド・スタブ
		//押されたダイアログボタンがジャンルボタンだったとき
		Log.d("Dialog in button", "first dialog : " + dialog.toString());
		if (checkNum == GENRE) {
			//listViewのどれが選択されたかを取得(genreIdに入っている)
			//選択された項目のジャンルがあるデータをデータベースから持ってくる
			//データをlayout.imageListに表示
			if (!(min.equals("") && max.equals(""))) {
				select = "itemValue >= ? and itemValue <= ? and genre = ?";
				selectArgs = new String[]{ min, max, genre[genreId] };
			} else if (min.equals("")){
				select = "itemValue <= ? and genre = ?";
				selectArgs = new String[]{ max,genre[genreId], };
			} else if (max.equals("")) {
				select = "itemValue >= ? and genre = ?";
				selectArgs = new String[]{ min,genre[genreId], };
			}
			Log.d("Dailog in Button", String.valueOf(genreId));
			if (genreId == 0) {
				select = null;
				selectArgs = null;
			}
			//どのジャンルかtermGenreに表示させる
			TextView text = (TextView)findViewById(R.id.termGenre);
			text.setText("条件 : " + genre[genreId]);
			showItemDB(select, selectArgs, orderBy);
		} else if (checkNum == VALUE) {
			setValView();
			showItemDB(select, selectArgs, orderBy);
		} else if (checkNum == ROW) {
			//商品の並び替えダイアログの中にあるボタンを押した時の処理
			showItemDB(select,selectArgs,orderBy);
		}
	}
	
	//ジャンルと商品をデータベースに入れる
	public void saveGenreDB() {
		itemGenreDBH = new ItemGenreDBHelper(this);
		SQLiteDatabase db = itemGenreDBH.getWritableDatabase();
		ContentValues values = new ContentValues();
		for (int i = 0;i < itemName.length;i++) {
			values.put("itemName", itemName[i]);
			//itemName[0]とitemName[1]がgenre[1]
			//itemName[2]とitemName[3]がgenre[2]
			//itemName[4]とitemName[5]がgenre[3]
			if (itemName[0].equals(itemName[i]) || itemName[1].equals(itemName[i])) {
				values.put("genre", genre[1]);
			} else if (itemName[2].equals(itemName[i]) || itemName[3].equals(itemName[i])) {
				values.put("genre", genre[2]);
			} else if (itemName[4].equals(itemName[i]) || itemName[5].equals(itemName[i])) {
				values.put("genre", genre[3]);
			}
		}
		db.insertOrThrow("itemGenreDB", null, values);
		itemGenreDBH.close();
	}
	
	//ジャンルボタンを押した時の処理
	public void showGenDia() {
		//商品データベースからジャンルを取り出す
		genreDialog = new AlertDialog.Builder(this);
		genAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,genre);
		genreDialog.setPositiveButton("ジャンル選択", this);
		//genreDialogの表示,genAdapterをset
		ListView listView = new ListView(this);
		listView.setAdapter(genAdapter);
		listView.setScrollingCacheEnabled(false);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			//リスト項目がクリックされた時の処理
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long id) {
				//選択された項目の場所を取得
				genreId = position;
				Toast.makeText(ItemViewActivity.this, genre[genreId] + "です。", Toast.LENGTH_SHORT).show();
			}
		});
		genreDialog.setView(listView);
		genreDialog.create();
		genreDialog.show();
	}
	
	//価格ボタンを押した時の処理
	/*valueDialogにterm_value_layoutをset*/
	public void showValDia() {
		// TODO 自動生成されたメソッド・スタブ
		Log.d("showValDia()", "01");
		valueDialog = new AlertDialog.Builder(this);
		LayoutInflater inflater = LayoutInflater.from(this);
		itemValVw = inflater.inflate(R.layout.term_value_layout, (ViewGroup)findViewById(R.id.termValueLl));
		valueDialog.setPositiveButton("価格帯設定", this);
		valueDialog.setView(itemValVw);
		
		valueDialog.create();
		valueDialog.show();
	}
	//商品の並び順を変える処理
	public void showRowDia() {
		rowDialog = new AlertDialog.Builder(this);
		LayoutInflater inflater = LayoutInflater.from(this);
		itemRowVw = inflater.inflate(R.layout.row_layout, (ViewGroup)findViewById(R.id.alignLl));
		String[] alignListSt = { "なし", "価格順：安い", "価格順：高い", };
		ArrayAdapter<String> alignAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,alignListSt);
		rowDialog.setPositiveButton("並び替え設定", this);
		rowDialog.setView(itemRowVw);
		ListView alignList = (ListView)itemRowVw.findViewById(R.id.alignList);
		alignList.setAdapter(alignAdapter);
		alignList.setBackgroundColor(Color.WHITE);
		alignList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			//並び順ごとの処理
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long id) {
				// TODO 自動生成されたメソッド・スタブ
				alignId = position;
				switch(position) {
				case 0://並び順なし
					orderBy = null;
					break;
				case 1://昇順
					orderBy = "itemValue ASC";
					break;
				case 2://降順
					orderBy = "itemValue DESC";
					break;
				}
			}
			
		});
		
		TextView alignText = (TextView)findViewById(R.id.termRow);
		alignText.setText("条件 : " + alignListSt[alignId]);
		rowDialog.create();
		rowDialog.show();
//		//昇順
//		orderBy = "itemValue ASC";
//		showItemDB(null,null,orderBy);
//		TextView orderByText = (TextView)findViewById(R.id.termRow);
//		orderByText.setText("条件 : 昇順");
	}
	
	//価格設定ダイアログで最小値から最大値までをlistViewに表示させる
	/*ソート
	 * min=1000000,max=12
	 * itemValue>=1000000 and itemValue<=12
	 * 1000000<=itemValue<=12*/
	public void setValView() {
		
		EditText minEditVw = (EditText)itemValVw.findViewById(R.id.minText);
		EditText maxEditVw = (EditText)itemValVw.findViewById(R.id.maxText);
		Log.d("setValView()", minEditVw.getText().toString());
		min = minEditVw.getText().toString();
		max = maxEditVw.getText().toString();
		
		//ジャンルがない場合
		if (genreId == 0) {
			select = "itemValue >= ? and itemValue <= ?";
			selectArgs = new String[]{ min, max };
			//最大値が０または最小値と最大値に未入力
			//全部表示
			if (max.equals("0") || min.equals("") && max.equals("")) {
				select = null;
				selectArgs = null;
			} else if (max == null) {
				//最大値だけ入力されていない
				select = "itemValue >= ?";
				selectArgs = new String[] { min, };
			}
		} else {
			//ジャンルがある場合
			select = "itemValue >= ? and itemValue <= ? and genre = ?";
			selectArgs = new String[] { min, max, genre[genreId] };
			//ジャンル項目だけ表示
			if (max.equals("0") || min.equals("") && max.equals("")) {
				select = "genre = ?";
				selectArgs = new String[] { genre[genreId], };;
			} else if (max.equals("")) {
				//最大値だけ入力されていない
				select = "itemValue >= ? and genre = ?";
				selectArgs = new String[] { min, genre[genreId]};
			}
		}
		//textViewに価格がどれだけか表示させる
		TextView valText = (TextView)findViewById(R.id.termValue);
		valText.setText("条件 : \n" + min + " ～ " + max);
	}
	
	
}
 