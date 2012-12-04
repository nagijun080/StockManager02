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
import android.widget.Toast;

public class ClientViewActivity extends Activity {

	//とりあえず配列で。今後SQLiteでデータベースから出す
	private int NUM = 2;
	public String[] USERNAME = {"福博綜合印刷", "ユーコー"};
	public String[] CHARGENAME = {"福博　太郎", "福博　優子"};
	public String[] CONTACTNAME = {"092-475-8639", "092-457-8342"};
	public String[] DATENAME = {"毎月20日", "毎月20日"};
	public String[] POSTNUMBER = {"812-8639", "812-0043"};
	public String[] ADDRESSNAME = {"福岡市博多区堅粕３丁目１６番３６号", "福岡市博多区堅粕３丁目１６番１４号"};
	//ここまで配列で、今後SQLite
	
	//リスト項目が押されたときに項目に関連する名前を入れる変数
	private String userName;
	private String chargeName;
	private String contackName;
	private String dateName;
	private String postNumber;
	private String addressName;
	
	public AlertDialog.Builder dialog;
	public ListView listView;
	public ArrayAdapter<String> adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clientview_layout);
		
		Log.d("ClientViewActivity()", "01");
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, USERNAME);
		Log.d("ClientViewActivity()", adapter.getItem(0));
		//listViewInDialog(adapter);
		//serchButtonを押したらDialogが表示される
		//serchButtonにOnClickListener登録
		Button serchButton = (Button)findViewById(R.id.serchButton);
		serchButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO 自動生成されたメソッド・スタブ
				dialog = new AlertDialog.Builder(ClientViewActivity.this);
				dialog.setTitle(R.string.guest_name);
				dialog.setPositiveButton(R.string.guest_setting, null);
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
				
				Toast.makeText(ClientViewActivity.this, "dialog", Toast.LENGTH_LONG).show();
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
