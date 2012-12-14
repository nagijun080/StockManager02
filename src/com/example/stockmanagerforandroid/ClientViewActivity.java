package com.example.stockmanagerforandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ClientViewActivity extends Activity {

	//ClientView_layoutの配列
	private int[] userViewId = { R.id.companyName, R.id.chargeName,
								R.id.contactName, R.id.dateName,
								R.id.postName, R.id.addressName };
	
	//お客様情報
	public String[][] USERDATA = { { "00001", "00002" },
								{"福博綜合印刷", "ユーコー"},
								{"福博　太郎", "福博　優子"},
								{"092-475-8639", "092-457-8342"},
								{"毎月20日", "毎月20日"},
								{"812-8639", "812-0043"},
								{"福岡市博多区堅粕３丁目１６番１４号", "福岡市博多区堅粕３丁目１６番１６号"}};

	
	
	public AlertDialog.Builder dialog;
	public ListView listView;
	public ArrayAdapter<String> adapter;
	
	//お客様データベース引数
	public UserDBHelper userDBHel;
	//発注設定データベース引数
	public OrderSetDBHelper orderSetDBHel;
	/*発注者番号*/
	public String ownerId = "";
	//お客様を識別するID
	public Integer userId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clientview_layout);
		
		//お客様データをデータベースに保存
		saveUserDB();
		
		Log.d("ClientViewActivity()", "01");
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, USERDATA[1]);
		Log.d("ClientViewActivity()", adapter.getItem(0));
		//serchButtonにOnClickListener登録
		final Button serchButton = (Button)findViewById(R.id.serchButton);
		serchButton.setOnClickListener(new OnClickListener() {
			//serchButtonを押したらDialogが表示される
			public void onClick(View v) {
				Log.d("serchButton.onClick()", "01");
				// TODO 自動生成されたメソッド・スタブ
				dialog = new AlertDialog.Builder(ClientViewActivity.this);
				dialog.setTitle(R.string.guest_name);
				//ダイアログが表示されてお客様設定ボタンが押された時の処理
				//layoutにuserIdのお客様の情報を表示
				dialog.setPositiveButton(R.string.guest_setting, new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO 自動生成されたメソッド・スタブ
						showClientView();
					}
				});
				//listViewを生成して、セッティングをする
				listViewSetting();
				dialog.setView(listView);
				dialog.create();
				dialog.show();
			}
		});
	}
	
	public void listViewSetting() {
		listView = new ListView(this);
		listView.setAdapter(adapter);
		listView.setScrollingCacheEnabled(false);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			//リスト項目がクリックされた時の処理
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long id) {
				// TODO 自動生成されたメソッド・スタブ
				userId = position;
				Toast.makeText(ClientViewActivity.this, "dialog position : " + position, Toast.LENGTH_LONG).show();
			}
			
		});
	}
	/*public void listViewInDialog(ArrayAdapter<String> adapter) {
		ListView listView = (ListView)findViewById(R.id.listView);
		listView.setAdapter(adapter);
		listView.setScrollingCacheEnabled(false);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			//リスト項目がクリックされた時の処理
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long itemId) {
				// TODO 自動生成されたメソッド・スタブ
				
				Toast.makeText(ClientViewActivity.this, "dialog", Toast.LENGTH_LONG).show();
			}
			
		});
	}*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.menu_layout, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d("設定ボタン","01");
		checkOwnerId();
		if ((ownerId.equals("") || ownerId.length() != 4) && R.id.setting != item.getItemId()) {
			Log.d("設定ボタン", "発注者番号が4文字ちゃんと入っていない");
			return false;
		} else if (userId == null && R.id.setting != item.getItemId()) {
			Toast.makeText(this, "お客様情報が入っていません。", Toast.LENGTH_SHORT);
			return false;
		} /*else {
			setOrderSetDB();
		}*/
		
		//"設定"用ダイアログ変数
		final EditText url = new EditText(this);
		AlertDialog.Builder alDia_Buil = new AlertDialog.Builder(this);
		
	    switch (item.getItemId()) {
	    case R.id.ownerData:
	    	Intent clientIntent = new Intent();
    		clientIntent.setClass(this, ClientViewActivity.class);
    		clientIntent.putExtra("ownerId_", ownerId);
    		clientIntent.putExtra("userId_", USERDATA[0][userId]);
    		startActivity(clientIntent);
	        return true;
	    case R.id.itemList:
	    	Intent itemViewIntent = new Intent();
    		itemViewIntent.setClass(this, ItemViewActivity.class);
    		itemViewIntent.putExtra("ownerId_", ownerId);
    		itemViewIntent.putExtra("userId_", USERDATA[0][userId]);
    		startActivity(itemViewIntent);
	        return true;
	    case R.id.ownerHistory:
	    	Intent historyViewIntent = new Intent();
	    	historyViewIntent.setClass(this, HistoryViewActivity.class);
	    	historyViewIntent.putExtra("ownerId_", ownerId);
	    	historyViewIntent.putExtra("userId_", USERDATA[0][userId]);
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
	//onCreateでお客様データをとりあえずデータベースに入れる
	public void saveUserDB() {
		userDBHel = new UserDBHelper(this);
		SQLiteDatabase db = userDBHel.getWritableDatabase();
		Log.d("saveUserDB()","01");
		for (int i = 0;i < USERDATA[0].length;i++) {
			ContentValues values = new ContentValues();
			values.put("userId", USERDATA[0][i]);
			values.put("company", USERDATA[1][i]);
			values.put("name", USERDATA[2][i]);
			values.put("telNumber", USERDATA[3][i]);
			values.put("date", USERDATA[4][i]);
			values.put("postNumber", USERDATA[5][i]);
			values.put("address", USERDATA[6][i]);
			Log.d("saveUserDB()","02");
			try {
				db.insertOrThrow("userDBTable", null, values);
				Log.d("saveUserDB()","insert");
			} catch (SQLiteConstraintException e) {
				db.update("userDBTable", values, "userId = ?", new String[] { USERDATA[0][i], });
				Log.d("saveUserDB()","update");
			}
		}
		userDBHel.close();
	}


	//ownerIdがちゃんと4文字の数字で入っているかチェックする
	public void checkOwnerId() {
		Integer NUM = 4;
		EditText ownerIdVw = (EditText)findViewById(R.id.ownerId);
		Log.d("checkOwnerId()","01 : " + String.valueOf(ownerIdVw.getText().toString().length()));
		try {
		//入っている場合4文字か？
			if (!(NUM.equals(ownerIdVw.getText().toString().length()))) {
				Toast.makeText(this, "発注者番号の文字数は4文字にしてください。", Toast.LENGTH_SHORT).show();
			} else {
				/*setOrderSetDB();*/
				//発注者番号をownerIdに入れる
				ownerId = ownerIdVw.getText().toString();
			}
		} catch (Exception e) {
		//何も入ってない場合(null)
			Toast.makeText(this, "発注者番号を入れてください", Toast.LENGTH_SHORT).show();
		}
		Log.d("checkOwnerId()", "02 : " + ownerId);
	}
	/*//発注者番号とお客様番号を発注設定データベースに保存
	//これが実行される時、ownerIdがちゃんと入ってない場合実行しない
	public void setOrderSetDB() {
		orderSetDBHel = new OrderSetDBHelper(this);
		SQLiteDatabase dbOrder = orderSetDBHel.getWritableDatabase();
		
		ContentValues orderVal = new ContentValues();
		orderVal.put("ownerId", ownerId);
		orderVal.put("userId", USERDATA[0][userId]);
		dbOrder.insertOrThrow("orderSetDBTable", null, orderVal);
		Log.d("setOrderSetDB()", "01 : inserOk");
		
		orderSetDBHel.close();
	}*/
	//お客様情報をデータベースから持ってきて、表示させるメソッド
	public void showClientView() {
		userDBHel = new UserDBHelper(this);
		SQLiteDatabase dbUser = userDBHel.getReadableDatabase();
		String[] userColumns = { /*"userId",*/ "company", "name", "telNumber", "date", "postNumber", "address", };
		String select = "userId = ?";
		
		Cursor userCur = dbUser.query("userDBTable", userColumns, select, new String[] { USERDATA[0][userId] }, null, null, null);
		userCur.moveToFirst();
		
		for (int i = 0;i < userColumns.length;i++) {
			//文字数が10文字以上だったら改行して表示
			//電話番号は改行しない
			if (!(USERDATA[3][userId].equals(userCur.getString(i))) && userCur.getString(i).length() > 11) {
				String textBreakFi = userCur.getString(i).substring(0, 9);//10文字区切る
				String textBreakSe = userCur.getString(i).substring(9);	//11文字目から最後まで
				Log.d("textBreakSe", textBreakSe);
				textBreakFi = textBreakFi + "\n" + textBreakSe;
				Log.d("textBreak", textBreakFi);
				TextView text = (TextView)findViewById(userViewId[i]);
				text.setText(textBreakFi);
			} else {
				TextView text = (TextView)findViewById(userViewId[i]);
				text.setText(userCur.getString(i));
			}
		}
		userCur.close();
		userDBHel.close();
		
	}
	
	//このActivityが走ったときにお客様情報を表示する
	//

}
