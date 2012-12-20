package com.example.stockmanagerforandroid;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class HistoryViewActivity extends Activity {

	public String ownerId;
	public String userId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history_layout);
		
		Intent intent = getIntent();
		ownerId = intent.getStringExtra("ownerId_");
		userId = intent.getStringExtra("userId_");
		//onCreate()されたら配列に"発注履歴"を格納
		setHistory();
		
		//setStatusClick();
	}
	ListView hisListView;
	String[] _status = new String[30];
	Integer[] _orderId = new Integer[30];
	String[] _userId = new String[30];
	Integer[] _totalValue = new Integer[30];
	//onCreate()されたら配列に"発注履歴"を格納
	public void setHistory() {
		HistoryDBHelper historyDB = new HistoryDBHelper(this);
		SQLiteDatabase db_hisDB = historyDB.getWritableDatabase();
		String sql = "SELECT status, orderId, userId, totalValue FROM historyDBTable;";
		Cursor c = db_hisDB.rawQuery(sql, null);
		c.moveToFirst();
		for (int i = 0;i < c.getCount();i++) {
			_status[i] = c.getString(0);
			_orderId[i] = c.getInt(1);
			_userId[i] = c.getString(2);
			_totalValue[i] = c.getInt(3);
			c.moveToNext();
		}
		historyDB.close();
		//_orderIdを元にorderDBからownerIdを取ってくる
		setHisOwnerId();
		//_userIdを元にuserDBからcompanyを取ってくる
		setCompany();
		//onCreateで履歴を表示
		showHistory();
	}
	Integer count = 0;
	//_orderIdを元にorderDBからownerIdを取ってくる
	String[] _ownerId = new String[30];
	public void setHisOwnerId() {
		OrderSetDBHelper orderDB = new OrderSetDBHelper(this);
		SQLiteDatabase db_orderDB = orderDB.getReadableDatabase();
		String sql = "SELECT ownerId FROM orderSetDBTable GROUP BY orderId;";
		Cursor c = db_orderDB.rawQuery(sql, null);
		c.moveToFirst();
		for (int i = 0;i < c.getCount();i++) {
			_ownerId[i] = c.getString(0);
			c.moveToNext();
			count++;
		}
		orderDB.close();
	}
	
	//_userIdを元にuserDBからcompanyを取ってくる
	String[] _company = new String[30];
	public void setCompany() {
		UserDBHelper userDB = new UserDBHelper(this);
		SQLiteDatabase db_userDB = userDB.getReadableDatabase();
		String sql = "SELECT company FROM userDBTable WHERE userId = ?;";
		for (int i = 0;i < count;i++) {
			Cursor c = db_userDB.rawQuery(sql, new String[] { _userId[i], });
			c.moveToFirst();
			_company[i] = c.getString(0);
		}
		userDB.close();
		
	}
	
	//onCreateで履歴を表示
	public void showHistory() {
		hisListView = (ListView)findViewById(R.id.orderList);
		
		List<CustomHistoryList> objects = new ArrayList<CustomHistoryList>();
		CustomHistoryList item = new CustomHistoryList();
		for (int i = 0;i < count;i++) {
			item.setOrderId(_orderId[i]);
			item.setOwnerId(_ownerId[i]);
			item.setStatus(_status[i]);
			item.setTotalValue(_totalValue[i]);
			item.setUserId(_company[i]);
			objects.add(item);
		}
		CustomAdapterInHis adapter = new CustomAdapterInHis(this,0,objects);
		hisListView.setAdapter(adapter);
	}
	
//	//"ステータス"にクリックリスナー
//	public void setStatusClick() {
//		TextView statusView = (TextView)findViewById(R.id.statusView);
//		statusView.setOnClickListener(new OnClickListener() {
//
//			public void onClick(View view) {
//				// TODO 自動生成されたメソッド・スタブ
//				statusClick();
//			}
//		});
//	}
//	
//	//"ステータス"をクリックしたあとの処理
//	public void statusClick() {
//		
//	}

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
		//"設定"用ダイアログ変数
		final EditText url = new EditText(this);
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
					/* http通信のテスト */
					new Thread( new Runnable() {
						public void run() {
							HttpConnection httpConect = new HttpConnection();
							String response = httpConect.doGet("http://" + url.getText().toString());
							System.out.println("Response : " + response);
						}
					}).start();
					/* http通信のテスト終了 */
				}
			}).show();
	    	return true;
	    }
	    return false;
	}
	
	//
}
