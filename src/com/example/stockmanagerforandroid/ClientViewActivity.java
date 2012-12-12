package com.example.stockmanagerforandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ClientViewActivity extends Activity {

	//ClientView_layout�̔z��
	private int[] userDataId = { R.id.companyName, R.id.chargeName,
								R.id.contactName, R.id.dateName,
								R.id.postName, R.id.addressName };
	
	//�Ƃ肠�����z��ŁB����SQLite�Ńf�[�^�x�[�X����o��
	private int NUM = 2;
	public String[][] USERDATA = {{"�����������", "���[�R�["},
								{"�����@���Y", "�����@�D�q"},
								{"092-475-8639", "092-457-8342"},
								{"����20��", "����20��"},
								{"812-8639", "812-0043"},
								{"�����s�����挘���R���ڂP�U�ԂP�S��", "�����s�����挘���R���ڂP�U�ԂP�U��"}};
	//�����܂Ŕz��ŁA����SQLite
	
	//���q�l�����ʂ���ID
	public int userId = 0;
	
	public AlertDialog.Builder dialog;
	public ListView listView;
	public ArrayAdapter<String> adapter;
	
	//���q�l�f�[�^�x�[�X����
	public UserDBHelper userDBHel;
	public String ownerId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clientview_layout);
		
		//�����Ҕԍ���ownerId�ɓ����
		EditText ownerIdVw = (EditText)findViewById(R.id.ownerId);
		ownerId = ownerIdVw.getText().toString();
		
		//���q�l�f�[�^���f�[�^�x�[�X�ɕۑ�
		saveUserDB();
		
		Log.d("ClientViewActivity()", "01");
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, USERDATA[0]);
		Log.d("ClientViewActivity()", adapter.getItem(0));
		//serchButton��OnClickListener�o�^
		final Button serchButton = (Button)findViewById(R.id.serchButton);
		serchButton.setOnClickListener(new OnClickListener() {
			//serchButton����������Dialog���\�������
			public void onClick(View v) {
				Log.d("serchButton.onClick()", "01");
				// TODO �����������ꂽ���\�b�h�E�X�^�u
				dialog = new AlertDialog.Builder(ClientViewActivity.this);
				dialog.setTitle(R.string.guest_name);
				//�_�C�A���O���\������Ă��q�l�ݒ�{�^���������ꂽ���̏���
				//layout��userId�̂��q�l�̏���\��
				dialog.setPositiveButton(R.string.guest_setting, new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO �����������ꂽ���\�b�h�E�X�^�u
						for (int i = 0;i < userDataId.length;i++) {
							TextView text = (TextView)findViewById(userDataId[i]);
							text.setText(USERDATA[i][userId]);
							//�Z����10�����ȏゾ��������s���ĕ\��
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
				//listView�𐶐����āA�Z�b�e�B���O������
				listViewSetting();
				dialog.setView(listView);
				dialog.create();
				dialog.show();
			}
		});
	}
	

	@Override
	protected void onStop() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onStop();
	}
	
	public void listViewSetting() {
		listView = new ListView(this);
		listView.setAdapter(adapter);
		listView.setScrollingCacheEnabled(false);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			//���X�g���ڂ��N���b�N���ꂽ���̏���
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long id) {
				// TODO �����������ꂽ���\�b�h�E�X�^�u
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
			//���X�g���ڂ��N���b�N���ꂽ���̏���
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long id) {
				// TODO �����������ꂽ���\�b�h�E�X�^�u
				
				Toast.makeText(ClientViewActivity.this, "dialog", Toast.LENGTH_LONG).show();
			}
			
		});
	}*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.menu_layout, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//"�ݒ�"�p�_�C�A���O�ϐ�
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
	    	alDia_Buil.setTitle("�ڑ���A�h���X�����");
	    	alDia_Buil.setView(url);
	    	alDia_Buil.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				// �ݒ�̒��ɂ���{�^�����N���b�N�Ńl�b�g���[�N�ɐڑ�
				public void onClick(DialogInterface dialog, int which) {
					// TODO �����������ꂽ���\�b�h�E�X�^�u
					/* http�ʐM�̃e�X�g */
					new Thread( new Runnable() {
						public void run() {
							HttpConnection httpConect = new HttpConnection();
							String response = httpConect.doGet("http://" + url.getText().toString());
							System.out.println("Response : " + response);
						}
					}).start();
					/* http�ʐM�̃e�X�g�I�� */
				}
			}).show();
	    	return true;
	    }
	    return false;
	}
	//onCreate�ł��q�l�f�[�^���Ƃ肠�����f�[�^�x�[�X�ɓ����
	public void saveUserDB() {
		userDBHel = new UserDBHelper(this);
		SQLiteDatabase db = userDBHel.getWritableDatabase();
		ContentValues values = new ContentValues();
		for (int i = 0;i < USERDATA[0].length;i++) {
			values.put("ownerId", ownerId);
			values.put("company", USERDATA[0][i]);
			values.put("name", USERDATA[1][i]);
			values.put("telNumber", USERDATA[2][i]);
			values.put("date", USERDATA[3][i]);
			values.put("postNumber", USERDATA[4][i]);
			values.put("address", USERDATA[5][i]);
		}
		try {
			db.insertOrThrow("userDBTable", null, values);
		} catch (SQLiteConstraintException e) {
			db.update("userDBTable", values, "ownerId = ?", new String[] { ownerId, });
		}
	}
	

}
