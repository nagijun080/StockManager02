package com.example.stockmanagerforandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ClientViewActivity extends Activity {

	//ClientView_layoutの配列
	private int[] userDataId = { R.id.companyName, R.id.chargeName,
								R.id.contactName, R.id.dateName,
								R.id.postName, R.id.addressName };
	
	//とりあえず配列で。今後SQLiteでデータベースから出す
	private int NUM = 2;
	public String[][] USERDATA = {{"福博綜合印刷", "ユーコー"},
								{"福博　太郎", "福博　優子"},
								{"092-475-8639", "092-457-8342"},
								{"毎月20日", "毎月20日"},
								{"812-8639", "812-0043"},
								{"福岡市博多区堅粕３丁目１６番１４号", "福岡市博多区堅粕３丁目１６番１６号"}};
	//ここまで配列で、今後SQLite
	
	//お客様を識別するID
	public int userId = 0;
	
	public AlertDialog.Builder dialog;
	public ListView listView;
	public ArrayAdapter<String> adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clientview_layout);
		
		Log.d("ClientViewActivity()", "01");
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, USERDATA[0]);
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
						for (int i = 0;i < userDataId.length;i++) {
							TextView text = (TextView)findViewById(userDataId[i]);
							text.setText(USERDATA[i][userId]);
							//住所が10文字以上だったら改行して表示
							if (i == 5 && USERDATA[5][userId].length() > 11) {
								String textBreakFi = USERDATA[5][userId].substring(0, 9);
								String textBreakSe = USERDATA[5][userId].substring(9);
								Log.d("textBreakSe", textBreakSe);
								textBreakFi = textBreakFi + "\n" + textBreakSe;
								Log.d("textBreak", textBreakFi);
								text.setText(textBreakFi);
							}
						}
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
	

	@Override
	protected void onStop() {
		// TODO 自動生成されたメソッド・スタブ
		super.onStop();
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
					long id) {
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
	

}
