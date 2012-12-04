package com.example.stockmanagerforandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
								{"福岡市博多区堅粕３丁目１６番３６号", "福岡市博多区堅粕３丁目１６番１４号"}};
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
	

}
