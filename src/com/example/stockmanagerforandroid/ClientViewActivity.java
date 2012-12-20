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

	//ClientView_layoutの要素配列
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
	private final Integer CHECKNUM = 4;
	//お客様データベース引数
	public UserDBHelper userDBHel;
	//発注設定データベース引数
	public OrderSetDBHelper orderSetDBHel;
	/*発注者番号*/
	public String ownerId = "";
	//お客様を識別するID
	public Integer userId;
	//お客様を検索して、お客様情報をViewに表示させてるかチェックする
	private boolean checkDiaBtn = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clientview_layout);
		
		//お客様データをデータベースに保存
		saveUserDB();
		
		//"検索ボタン"にクリックリスナー
		setSerchBtn_Click();
	}
	
	//"検索"ボタンにクリックリスナー
	public void setSerchBtn_Click() {
		Button serchButton = (Button)findViewById(R.id.serchButton);
		serchButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO 自動生成されたメソッド・スタブ
				showUserDialog();
			}
		});
	}
	//"検索"ボタンを押したあとの処理
	//"お客様の名前"のダイアログを表示させる
	public void showUserDialog() {
		dialog = new AlertDialog.Builder(this);
		dialog.setTitle(R.string.guest_name);
		//ダイアログが表示されてお客様設定ボタンが押された時の処理
		//layoutにuserIdのお客様の情報を表示
		dialog.setPositiveButton(R.string.guest_setting, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO 自動生成されたメソッド・スタブ
				setClientView();
				checkDiaBtn = true;
			}
		});
		//listViewを生成して、セッティングをする
		dialog.setView(getListView());
		dialog.create();
		dialog.show();
	}
	//"検索"ボタンを押した時に表示させるダイアログ
	//その中にあるListViewを返す
	public ListView getListView() {
		//"検索"ボタンを押した時にダイアログで表示させる文字列
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, USERDATA[1]);
		ListView listView = new ListView(this);
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
		return listView;
	}

	
	//onCreateでお客様データをデータベースに入れる
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


	//発注者番号がちゃんと4文字の数字で入っているかチェックしてownerIdに入れる
	public boolean getCheckOwnerId() {
		final Integer NUM = 4;
		EditText ownerIdEt = (EditText)findViewById(R.id.ownerId);
		String ownerIdSt = ownerIdEt.getText().toString();
		Log.d("checkOwnerId()","01 : " + ownerIdSt);
		//ownerIdに文字が空ではないとき(何らかの文字は入っている)
		if (!("".equals(ownerIdSt))) {
			//ownerIdの文字が4文字でないとき
			if (!(NUM.equals(ownerIdSt.length()))) {
				Toast.makeText(this, "発注者番号の文字数は4文字にしてください。", Toast.LENGTH_SHORT).show();
				return false;
			//ownerIdの文字が4文字の時
			} else {
				//発注者番号をownerIdに入れる
				ownerId = ownerIdEt.getText().toString();
				return true;
			}
		//ownerIdが空文字のとき
		} else {
			Toast.makeText(this, "発注者番号を入れてください", Toast.LENGTH_SHORT).show();
			return false;
		}
		
	}
	//
	public void setClientView() {
		userDBHel = new UserDBHelper(this);
		SQLiteDatabase dbUser = userDBHel.getReadableDatabase();
		
		String sql = "SELECT company, name, telNumber, date, postNumber, address FROM userDBTable WHERE userId = ?;";
		String[] selectionArgs = { USERDATA[0][userId], };
		Cursor userCur = dbUser.rawQuery(sql, selectionArgs);
		userCur.moveToFirst();
		
		for (int i = 0;i < userCur.getColumnCount();i++) {
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.menu_layout, menu);
		return true;
	}
	
	EditText urlEt;
	String urlSt = "http://172.16.80.35/android/index.php/?";
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d("設定ボタン","01");
		if (!getCheckOwnerId() && R.id.setting != item.getItemId()) {
			return false;
		}
		
		urlEt = new EditText(this);
		urlEt.setText(urlSt);
		//"設定"用ダイアログ変数
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
	    	alDia_Buil.setView(urlEt);
	    	alDia_Buil.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				// 設定の中にあるボタンをクリックでネットワークに接続
				public void onClick(DialogInterface dialog, int which) {
					// TODO 自動生成されたメソッド・スタブ
					urlSt = urlEt.getText().toString();
				}
			}).show();
	    	
	    	return true;
	    }
	    return false;
	}

}
