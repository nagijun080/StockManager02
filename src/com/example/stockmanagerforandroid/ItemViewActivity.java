package com.example.stockmanagerforandroid;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ItemViewActivity extends Activity implements OnClickListener, DialogInterface.OnClickListener{

	/* 商品データ配列　*/
	//macの商品画像ID配列
	public Integer[] itemImageId = { R.drawable.imac_215, R.drawable.imac_27,
								R.drawable.ipad_mini_bk, R.drawable.ipad_mini_whi,
								R.drawable.macbook_13, R.drawable.macbook_13_retina };
	public String[] itemId = { "20001", "20002", "20003", "20004", "20005", "20006", };
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
	/* 商品データ配列ここまで　*/
	
	ItemDBHelper itemDBH;
	
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
	private String min = "";
	private String max = "";
	public View itemValVw;
	public View itemRowVw;
	
	public EditText url;
	//"商品の詳細"ダイアログ。ListViewをクリックしたら表示
	public View buyView;
	//intentでClientViewからownerIdとuserIdをもらってくる
	public String ownerId;
	public String userId;
	
	//カートボタンを押した時のダイアログ
	AlertDialog.Builder cartDialog;
	View cartView;
	//発注した時に設定するID。発注ごとに違う
	//1000からスタートする
	public Integer orderId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.item_view_layout);
		//itemDBにデータを入れる
		saveItemDB();
		//itemDBテーブルに入っているデータをListViewに表示
		showItemDB(null, null, null);
		urlSt = "172.16.80.35/android/index.php/?";
		//発注ID。発注するたびにorderIdに＋１される
		setOrderId();
		//LayoutのvalueViewだけ改行処理
		TextView valueV = (TextView)findViewById(R.id.valueView);
		valueV.setText("価格を\n絞り込む");
		
		//カートダイアログ表示ボタン
		cartDialog = new AlertDialog.Builder(this);
		//"商品リスト"にクリックリスナー
		itemListClick();
		//"カート"ボタンにクリックリスナー
		cartClick();
		//ClientViewActivityからownerIdとuserIdを持ってくる
		Intent intent = getIntent();
		ownerId = intent.getStringExtra("ownerId_");
		userId = intent.getStringExtra("userId_");
		
		//商品を検索するボタンにクリックリスナー登録する
		genreButton = (ImageButton)findViewById(R.id.genruButton);
		genreButton.setOnClickListener(this);
		valueButton = (ImageButton)findViewById(R.id.valueButton);
		valueButton.setOnClickListener(this);
		rowButton = (ImageButton)findViewById(R.id.rowButton);
		rowButton.setOnClickListener(this);
		
	}
	
	//ItemDBHelperのitemDBテーブルにinsertする
	public void saveItemDB() {
		Log.d("saveItemDB()","first");
		
		for (int i = 0;i < itemImageId.length;i++) {				
			Log.d("saveItemDB()","0" + i);
			ContentValues values = new ContentValues();
			values.put("itemId", itemId[i]);
			values.put("itemImageId", itemImageId[i]);
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
			Log.d("itemImageId[i]", itemImageId[i].toString());
			Integer checkRec = 0;
			try  {
				checkRec = Long.valueOf(db.insertOrThrow("itemDB", null, values)).intValue();
				Log.d("db.insert","checkIns : " + String.valueOf(checkRec));
			} catch (SQLiteConstraintException e) {
				checkRec = db.update("itemDB", values, "itemImageId = ?", new String[] { itemImageId[i].toString(), });
				Log.d("db.update","checkRec : " + String.valueOf(checkRec));
			}
			itemDBH.close();
		}
	}
	
	//itemDBテーブルから商品を取り出して、表示
	//引数1:select 条件カラム
	//引数2:selectArgs　条件カラムに代入する文字列
	/*genre別に*/
	public void showItemDB(String select, String[] selectArgs, String orderBy){
		ItemDBHelper itemDBH = new ItemDBHelper(this);
		SQLiteDatabase db = itemDBH.getWritableDatabase();
		ListView imageList = (ListView)findViewById(R.id.imageList);
		String[] colmns = { "itemId", "itemImageId", "itemName", "itemValue", "itemData", "genre"};
		Cursor c = db.query("itemDB", colmns, select, selectArgs, null, null, orderBy);
		//商品の情報が全部入った２次元配列
		String[][] item = new String[c.getCount()][colmns.length];
		if (c != null && c.getCount() != 0) {
			c.moveToFirst();
			Log.d("showItemDB()","12" + c.getString(0));
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
						customItem.setItemId(item[i][j]);
						break;
					case 1:
						customItem.setItemImageId(Integer.valueOf(item[i][j]));
						break;
					case 2:
						customItem.setItemName(item[i][j]);
						break;
					case 3:
						customItem.setItemValue(Integer.valueOf(item[i][j]));
						break;
					case 4:
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
		itemDBH.close();
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
			Log.d("min == max", min + ":" + max);
			if (("").equals(min) && ("").equals(max)) { 
				select = "genre = ?";
				selectArgs = new String[]{ genre[genreId] };
			} else if (min.equals("")){
				select = "itemValue <= ? and genre = ?";
				selectArgs = new String[]{ max,genre[genreId], };
			} else if (max.equals("")) {
				select = "itemValue >= ? and genre = ?";
				selectArgs = new String[]{ min,genre[genreId], };
			} else {
				select = "itemValue >= ? and itemValue <= ? and genre = ?";
				selectArgs = new String[]{ min, max, genre[genreId], };
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

	//"ジャンルボタン"を押した時の処理
	public void showGenDia() {
		genreDialog = new AlertDialog.Builder(this);
		genAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,genre);
		genreDialog.setPositiveButton("ジャンル選択", this);
		//genreDialogの表示,genAdapterをset
		ListView listView = new ListView(this);
		listView.setAdapter(genAdapter);
		listView.setScrollingCacheEnabled(false);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			//"ジャンルリスト"の項目がクリックされた時の処理
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
	}
	
	//価格設定ダイアログで最小値から最大値までをlistViewに表示させる
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
	
	//"商品リスト"にクリックリスナー
	public void itemListClick() {
		ListView itemList = (ListView)findViewById(R.id.imageList);
		itemList.setOnItemClickListener(new OnItemClickListener() {
			//このViewはListViewのView
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long id) {
				// TODO 自動生成されたメソッド・スタブ
				//商品の詳細ダイアログを表示
				showListDialog();
				setListDiaView(view);
			}
			
		});
		
	}
	//リストをクリックした時ダイアログを表示させる
	//buyViewにinflateしてレイアウトが使えるようにしている
	public void showListDialog() {
		LayoutInflater inflater = LayoutInflater.from(this);
		//"商品の詳細ダイアログ"のView
		buyView = inflater.inflate(R.layout.item_buy_layout, (ViewGroup)findViewById(R.id.itemBuyLayout));
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setView(buyView);
		//"買い物カゴに追加"ボタン
		alertDialog.setPositiveButton("買い物カゴに追加", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO 自動生成されたメソッド・スタブ
				setOrder_DB();
				setTotalNumView();
			}
			
		});
		alertDialog.create();
		alertDialog.show();
	}
	
	//商品ダイアログを出した時にデータベースから
	//商品の情報をとってダイアログに表示
	//引数:ListViewのView
	//viewに表示させる
	public void setListDiaView(View view) {
		TextView itemIdView = (TextView)view.findViewById(R.id.itemId);
		String itemId = itemIdView.getText().toString();
		itemDBH = new ItemDBHelper(this);
		SQLiteDatabase dbItem = itemDBH.getReadableDatabase();
		String sql = "SELECT itemId, itemImageId, itemValue, itemName, itemData"
					+ " FROM itemDB WHERE itemId = ?;";
		Cursor cur = dbItem.rawQuery(sql, new String[] { itemId, });
		cur.moveToFirst();
		Log.d("setListDiaView(view)","itemId:" + cur.getString(0));
		setBuyLayout(cur);
		
		itemDBH.close();
	}
	
	//引数：データベースのカーソル
	//カーソルのgetでデータを取得する
	//そのデータをlayoutに反映させる
	//itemId, itemImageId, itemValue, itemName, itemDataの順番
	//itemValueだけ小計に使うので別変数に入れる
	Integer value;
	//商品画像が入っているリソースID
	Integer item_ImageId;
	//商品の個数を入れる変数
	public Integer item_count = 1;
	//商品のIDを入れる変数
	public String item_ID;
	//価格の小計
	public Integer item_mTotalValue;
	public void setBuyLayout(Cursor c) {
		TextView item_numView = (TextView)buyView.findViewById(R.id.itemCount);
		item_numView.setText("個数：" + item_count);
		
		TextView itemIdView = (TextView)buyView.findViewById(R.id.itemNumber);
		item_ID = c.getString(0);
		itemIdView.setText("品番:" + item_ID);
		
		ImageView itemImageVw = (ImageView)buyView.findViewById(R.id.itemImage);
		item_ImageId = c.getInt(1);
		itemImageVw.setImageResource(item_ImageId);
		
		//小計に使うので別変数に入れる
		TextView valueView = (TextView)buyView.findViewById(R.id.value);
		value = c.getInt(2);
		valueView.setText("単価：" + value.toString());
		
		//商品の個数を変更する
		item_Num_ValueChange();
		TextView minTotalVw = (TextView)buyView.findViewById(R.id.minTotal);
		minTotalVw.setText("小計：" + item_mTotalValue.toString());
		
		TextView nameView = (TextView)buyView.findViewById(R.id.itemName);
		nameView.setText(c.getString(3));
		
		TextView dataView = (TextView)buyView.findViewById(R.id.itemData);
		dataView.setText(c.getString(4));
	}
	//"商品詳細ダイアログ"の"個数変更"ボタンの処理
	public void item_Num_ValueChange() {
		item_mTotalValue = value * item_count;
		Button downButton = (Button)buyView.findViewById(R.id.downButton);
		downButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO 自動生成されたメソッド・スタブ
				if (!(item_count <= 1)) {
					item_count--;
				}
				item_mTotalValue = value * item_count;				
			}
		});
		
		Button upButton = (Button)buyView.findViewById(R.id.upButton);
		upButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO 自動生成されたメソッド・スタブ
				item_count++;
				item_mTotalValue = value * item_count;
			}
		});
	}
	
	//orderSetDBTableにinsertする
	//"商品詳細ダイアログ"の"買い物カゴに追加"ボタンでデータベースにinsertする
	public void setOrder_DB() {
		OrderSetDBHelper orderSet_DBH = new OrderSetDBHelper(this);
		SQLiteDatabase db_order = orderSet_DBH.getWritableDatabase();
		ContentValues order_values = new ContentValues();
		order_values.put("orderId", orderId);
		order_values.put("ownerId", ownerId);
		order_values.put("userId", userId);
		order_values.put("itemId", item_ID);
		order_values.put("item_num", item_count);
		order_values.put("sumValue", item_mTotalValue);
		db_order.insertOrThrow("orderSetDBTable", null, order_values);
		db_order.close();
		orderSet_DBH.close();
	}
	
	//"商品詳細ダイアログ"の"買い物カゴに追加"ボタンで
	//"商品リスト"layoutの"買い物カゴ"Viewに個数を反映させる
	public void setTotalNumView() {
		OrderSetDBHelper orderSet_DBH = new OrderSetDBHelper(this);
		SQLiteDatabase db_order = orderSet_DBH.getReadableDatabase();
		
		String sql = "SELECT ownerId, itemId FROM orderSetDBTable WHERE ownerId = ? GROUP BY itemId;";
		Cursor cur = db_order.rawQuery(sql, new String[] { ownerId, });
		Integer itemType_num = cur.getCount();
		Log.d("setTotalNumView","商品の種類別個数" + String.valueOf(itemType_num));
		orderSet_DBH.close();			
		
		TextView itemNum_View = (TextView)findViewById(R.id.itemNum_view);
		itemNum_View.setText(itemType_num.toString());
		
	}
	
	//"カート"ボタンを押したあとの処理
	public void showCartDialog() {
		LayoutInflater inflate = LayoutInflater.from(this);
		cartView = inflate.inflate(R.layout.cart_dialog_view, (ViewGroup)findViewById(R.id.dialogInCart_ll));
		ListView listInCart = (ListView)cartView.findViewById(R.id.itemListInCart);
		
		List<CustomDialogInCart> objects = new ArrayList<CustomDialogInCart>();
		for (int i = 0;i < orderCount;i++) {
			CustomDialogInCart item = new CustomDialogInCart();
			item.setImageId(itemImageId_ItemDB[i]);
			item.setItemNum(itemNum_In_OrderDB[i].toString());
			item.setItemMinTotal(minTotal_In_OrderDB[i].toString());
			item.setItemData(itemData_In_ItemDB[i]);
			item.setItemId(itemId_In_ItemDB[i]);
			item.setItemName(itemName_In_ItemDB[i]);
			item.setItemUnitPrice(unitPrice_In_ItemDB[i]);
			objects.add(item);
		}
		CustomAdapterInCart adapter = new CustomAdapterInCart(this, 0, objects);
		listInCart.setAdapter(adapter);
		showCartTotal();
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setView(cartView);
		dialogBuilder.show();
		
		//"この内容で発注送信"ボタンの処理
		sendOrder();
		//"納品設定"ボタンの処理
		orderItem_set();
		//"履歴に保存"ボタンの処理
		saveHistoryDB();
		//"すべて削除"ボタンの処理
		setAllDel_Click();
		
	}

	//発注したい情報をOrderSetDBから入れる変数
	Integer[] itemId_In_OrderDB = new Integer[10];
	Integer[] itemNum_In_OrderDB = new Integer[10];
	Integer[] minTotal_In_OrderDB = new Integer[10];
	//imageId_In_OrderDBを元にItemDBHelperから情報と得る
	String[] itemId_In_ItemDB = new String[10];
	Integer[] itemImageId_ItemDB = new Integer[10];
	String[] itemName_In_ItemDB = new String[10];
	String[] itemData_In_ItemDB = new String[10];
	String[] unitPrice_In_ItemDB = new String[10];
	//カートの中に入っている商品数//http通信をする回数 = カートに入れた回数
	int orderCount = 0;
	//"カート"ボタンを押した時、データベースから発注情報を持ってくる
	public void setOrderDB_In_Cart() {
		Log.d("setOrderDB_In_Cart()", "ownerId:" + ownerId);
		OrderSetDBHelper orderDB = new OrderSetDBHelper(this);
		SQLiteDatabase db_order = orderDB.getReadableDatabase();
		String sql = "SELECT ownerId, itemId, item_num, sumValue FROM orderSetDBTable WHERE ownerId = ?;";
		Cursor cur = db_order.rawQuery(sql, new String[] { ownerId });
		cur.moveToFirst();
		orderCount = cur.getCount();
		for (int i = 0;i < orderCount;i++) {
			Log.d("setOrderDB_In_Cart()", String.valueOf(cur.getInt(1)));
			itemId_In_OrderDB[i] = cur.getInt(1);
			itemNum_In_OrderDB[i] = cur.getInt(2);
			Log.d("setOrderDB()","01 : " + cur.getInt(2));
			minTotal_In_OrderDB[i] = cur.getInt(3);
			cur.moveToNext();
		}
		orderDB.close(); 
		setItemDB_In_Cart();
	}
	
	//imageId_In_OrderDBに入っているリソースIDを元にitemDBHelper内の
	//商品情報を配列にセットする
	public void setItemDB_In_Cart()	 {
		ItemDBHelper itemDBH = new ItemDBHelper(this);
		SQLiteDatabase db_item = itemDBH.getReadableDatabase();
		String sql = "SELECT itemId, itemImageId, itemName, itemValue, itemData FROM itemDB WHERE itemId = ?;";
		for (int i = 0;i < orderCount;i++) {
			Cursor c = db_item.rawQuery(sql, new String[]{ itemId_In_OrderDB[i].toString(), });
			c.moveToFirst();
			itemId_In_ItemDB[i] = c.getString(0);
			itemImageId_ItemDB[i] = c.getInt(1);
			itemName_In_ItemDB[i] = c.getString(2);
			unitPrice_In_ItemDB[i] = c.getString(3);
			itemData_In_ItemDB[i] = c.getString(4);
		}
		itemDBH.close();
	}
	
	//"カート"ボタンにクリックリスナ-登録
	public void cartClick() {
		Button cartButton = (Button)findViewById(R.id.btnCart);
		cartButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO 自動生成されたメソッド・スタブ	
				//"カート"の中に入っている商品を一旦配列に入れる
				setOrderDB_In_Cart();
				//"カート"ダイアログを表示
				showCartDialog();
			}
		});
	}
	Boolean httpBool = false;
	//"カート"内の"この内容で発注送信"ボタンにクリックリスナー
	public void sendOrder() {
		Button sendOrderBtn = (Button)cartView.findViewById(R.id.sendOrderBtn);
		sendOrderBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO 自動生成されたメソッド・スタブ
				//"履歴データベース"に発注情報を保存
				saveHistoryDB();
				/* http通信Thread */
				new Thread( new Runnable() {
					public void run() {
						for (int i = 0;i < orderCount;i++) {
							HttpConnection httpConect = new HttpConnection();
							String url_ = "http://" + urlSt;
							String uri = "orderId:=" + orderId.toString() + "&" + "ownerId:=" + ownerId + "&" + "itemId:=" + itemId_In_ItemDB[i] + "&"
										+ "unitPrice:=" + unitPrice_In_ItemDB[i] + "&" + "itemNum:=" + itemNum_In_OrderDB[i] 
										+ "&" + "itemMinTotal:=" + minTotal_In_OrderDB[i];
							String response = httpConect.doGet(url_ + uri);
							//通信ができたらtrue,できなかったらfalse
							if (response != null) {
								httpBool = true;
							} else {
								httpBool = false;
							}
							System.out.println("Response : " + response + "httpBool:" + httpBool);
						}
					}
				}).start();
				/* http通信Thread終了 */
				setAllDel_Click();
				//新しい発注IDを取得
				setOrderId();
			}			
		});
	}
	
	//設定のURLをorderSetDBTableに保存する
	public void saveURL() {
		OrderSetDBHelper orderSetDBH = new OrderSetDBHelper(this);
		SQLiteDatabase db_orderDB = orderSetDBH.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("urlEt", urlSt);
		db_orderDB.update("orderSetDBTable", values, "ownerId = ?", new String[]{ ownerId, });
		orderSetDBH.close();
	}
	
	AlertDialog.Builder orderSet_Dialog;
	View orderSet_View;
	TextView orderDateView;
	//"カート"ボタンpush↓
	//ダイアログ表示↓
	//"納品設定"ボタンにクリックリスナー↓
	/* "納品設定"ボタンを押したあとの処理 */
	public void orderItem_set() {
		//dateListViewのクリックされた日付データが入る
		LayoutInflater inflater = LayoutInflater.from(this);
		//View cart_dialogView = inflater.inflate(R.layout.cart_dialog_view, (ViewGroup)findViewById(R.id.dialogInCart_ll));
		orderSet_View = inflater.inflate(R.layout.order_set_dialog_layout, (ViewGroup)findViewById(R.id.orderSet_dialog_Ll));
		orderDateView = (TextView)orderSet_View.findViewById(R.id.sendOrder_date);
		Log.d("orderItem_set()", "01");
		Button deliBtn = (Button)cartView.findViewById(R.id.deliveryBtn);
		deliBtn.setOnClickListener(new OnClickListener() {	
			//"納品設定"ボタン
			public void onClick(View v) {
				Log.d("orderItem_set()", "02");
				// TODO 自動生成されたメソッド・スタブ
				orderSet_Dialog = new AlertDialog.Builder(ItemViewActivity.this);
				orderSet_Dialog.setPositiveButton("納品情報を設定", new DialogInterface.OnClickListener() {
					//"納品情報を設定"ボタン
					public void onClick(DialogInterface dialog, int which) {
						// TODO 自動生成されたメソッド・スタブ
						//お届け日を発注データベースに格納
						orderDate_setDB(); 
						
					}
				});
				//ownerIdを元に各Widgetにデータベースから持ってきてset
				setOrderView_from_userDB(orderSet_View);
				//orderSet_Viewに納品設定のlayoutをセット
				orderSet_Dialog.setView(orderSet_View);
				orderSet_Dialog.create();
				orderSet_Dialog.show();
			}
			
		});
		
		final AlertDialog.Builder orderDateChange_Dialog = new AlertDialog.Builder(this);
		ListView dateListView = new ListView(this);
		
		setDateListView(dateListView);
		
		ViewGroup parent = (ViewGroup)dateListView.getParent();
		if ( parent != null) {
			parent.removeView(dateListView);
		}
		orderDateChange_Dialog.setView(dateListView);
		//"配達日変更"ボタン
		Button haitatsuChange_Btn = (Button)orderSet_View.findViewById(R.id.haitatsubi_change);
		haitatsuChange_Btn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO 自動生成されたメソッド・スタブ
				orderDateChange_Dialog.create();
				orderDateChange_Dialog.show();
			}
			
		});
		orderDateChange_Dialog.setPositiveButton("お届け日設定", new DialogInterface.OnClickListener() {
			//"お届け日設定"ボタン
			public void onClick(DialogInterface dialog, int which) {
				// TODO 自動生成されたメソッド・スタブ
				orderDateView.setText("配達希望日　" + orderDateTx);
			}
		});
		
		dateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			//"配達日変更"ボタンを押したあとのListViewItemClick
			public void onItemClick(AdapterView<?> adapterView, View view, int position,
					long id) {
				// TODO 自動生成されたメソッド・スタブ
				ListView listView = (ListView) adapterView;
                // クリックされたアイテムを取得します
                orderDateTx = (String) listView.getItemAtPosition(position);
			}
			
		});
	}
		
	String orderDateTx;
	
	//"納品設定"ボタンが押された時、データベースから持ってきてViewにセットする
	public void setOrderView_from_userDB(View view) {
		TextView ownerIdView = (TextView)view.findViewById(R.id.ownerId_View);
		ownerIdView.setText(ownerId);
		
		TextView[] orderSet_TxVw = new TextView[] { (TextView)view.findViewById(R.id.userID_view),
													(TextView)view.findViewById(R.id.company_name),
													(TextView)view.findViewById(R.id.tantoName_View),
													(TextView)view.findViewById(R.id.telNumber_View),
													(TextView)view.findViewById(R.id.endDate_View),
													(TextView)view.findViewById(R.id.postNumber),
													(TextView)view.findViewById(R.id.addressView),};
		
		UserDBHelper userDBH = new UserDBHelper(this);
		SQLiteDatabase db_userDB = userDBH.getReadableDatabase();
		String sql = "SELECT userId, company, name, telNumber, date, postNumber, address FROM userDBTable WHERE userId = ?;";
		String[] selectionArgs = new String[] { userId, };
		Cursor c = db_userDB.rawQuery(sql, selectionArgs);
		c.moveToFirst();
		Log.d("companyName", c.getString(1));
		for (int i = 0;i < orderSet_TxVw.length;i++) {
			orderSet_TxVw[i].setText(c.getString(i));
			Log.d("setOrderView_from_userDB()", "0" + i);
		}
		userDBH.close();
		
	}
	//日付処理
	public void setDateListView(ListView listView) {
		Calendar nowCalendar = Calendar.getInstance();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		for (int i = 0;i < 31;i++) {
			nowCalendar.add(Calendar.DATE, 1);
			DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
			adapter.add(df.format(nowCalendar.getTime()));
			listView.setAdapter(adapter);
		}
	}
	//"納品情報を設定"ボタンを押した時の処理
	//配達日をorderSetDBTableに格納
	public void orderDate_setDB() {
		OrderSetDBHelper orderSetDB = new OrderSetDBHelper(this);
		SQLiteDatabase db_order = orderSetDB.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("orderDate", orderDateTx);
		db_order.update("orderSetDBTable", values, "ownerId = ?", new String[]{ ownerId, });
		orderSetDB.close();
	}
	
	//"カート"ボタンを押したあとのダイアログの中の処理
	//"すべて削除"ボタンにクリックリスナー
	public void setAllDel_Click() {
		Button allClearBtn = (Button)cartView.findViewById(R.id.allClear);
		allClearBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO 自動生成されたメソッド・スタブ
				orderCount = 0;
				item_count = 1;
				allClear();
				setTotalNumView();
			}
			
		});
	}
	
	//"すべて削除"ボタンを押したあとの処理
	//"カート"の中とデータベースと配列から商品を削除する
	public void allClear() {
		ListView cartList = (ListView)cartView.findViewById(R.id.itemListInCart);
		ArrayAdapter adapter = (ArrayAdapter)cartList.getAdapter();
		adapter.clear();
		
		OrderSetDBHelper orderDBH = new OrderSetDBHelper(this);
		SQLiteDatabase db_order = orderDBH.getWritableDatabase();
		db_order.delete("orderSetDBTable", "orderId = ?", new String[] { orderId.toString(), });
		orderDBH.close();
		
		//発注したい情報をOrderSetDBから入れる変数
		itemId_In_OrderDB = new Integer[10];
		itemNum_In_OrderDB = new Integer[10];
		minTotal_In_OrderDB = new Integer[10];
		//imageId_In_OrderDBを元にItemDBHelperから情報と得る
		itemId_In_ItemDB = new String[10];
		itemImageId_ItemDB = new Integer[10];
		itemName_In_ItemDB = new String[10];
		itemData_In_ItemDB = new String[10];
		unitPrice_In_ItemDB = new String[10];
	}
	
	//"カート"ボタンを押した時に"カート"の中にあるすべての合計値を返す
	public Integer getTotal_In_Cart() {
		Integer total = 0;
		for (int i = 0;i < orderCount;i++) {
			total += minTotal_In_OrderDB[i];
		}
		return total;
	}
	
	//"カート"内の合計値を"カート"ダイアログに表示
	public void showCartTotal() {
		TextView totalView = (TextView)cartView.findViewById(R.id.totalView);
		totalView.setText("合計：" + getTotal_In_Cart());
	}
	
	//商品の発注IDをorderIdにセット
	public void setOrderId() {
		OrderSetDBHelper orderSetDBH = new OrderSetDBHelper(this);
		SQLiteDatabase db_orderDB = orderSetDBH.getWritableDatabase();
		
		String sql = "SELECT orderId FROM orderSetDBTable;";
		try { 
			Cursor c = db_orderDB.rawQuery(sql, null);
			c.moveToLast();
			orderId = c.getInt(0) + 1;
		} catch (CursorIndexOutOfBoundsException e) {
			Log.d("setOrderId_In_DB()", "01");
			orderId = 1000;
		}
		
		orderSetDBH.close();
	}
	
	//"履歴に保存"ボタンにクリックリスナー
	public void setHistory_Click() {
		Button historyBtn = (Button)cartView.findViewById(R.id.historySaveBtn);
		historyBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO 自動生成されたメソッド・スタブ
				saveHistoryDB();
			}
			
		});
	}
	
	//"履歴に保存"ボタンでHistoryDBに格納
	public void saveHistoryDB() {
		HistoryDBHelper historyDBH = new HistoryDBHelper(this);
		SQLiteDatabase db_hisDB = historyDBH.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("status", httpBool.toString());
		values.put("orderId", orderId);
		values.put("userId", userId);
		values.put("totalValue", getTotal_In_Cart());
		try {
			db_hisDB.insertOrThrow("historyDBTable", null, values);
		} catch (Exception e) {
			db_hisDB.update("historyDBTable", values, "orderId = ?", new String[] { orderId.toString(), });
		}
		historyDBH.close();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.menu_layout, menu);
		return true;
	}
	String urlSt;
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		url = new EditText(this);
		url.setText(urlSt);
		//"設定"用ダイアログ変数
		AlertDialog.Builder alDia_Buil = new AlertDialog.Builder(this);
		
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
	    case R.id.setting:
	    	alDia_Buil.setTitle("接続先アドレスを入力");
	    	alDia_Buil.setView(url);
	    	alDia_Buil.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				// 設定の中にあるボタンをクリックでネットワークに接続
				public void onClick(DialogInterface dialog, int which) {
					// TODO 自動生成されたメソッド・スタブ
					urlSt = url.getText().toString();
				}
	    	}).show();
	    	return true;
	    }
	    return false;
	}
}