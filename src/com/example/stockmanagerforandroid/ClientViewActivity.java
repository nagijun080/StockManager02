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

	//ClientView_layout�̗v�f�z��
	private int[] userViewId = { R.id.companyName, R.id.chargeName,
								R.id.contactName, R.id.dateName,
								R.id.postName, R.id.addressName };
	
	//���q�l���
	public String[][] USERDATA = { { "00001", "00002" },
								{"�����������", "���[�R�["},
								{"�����@���Y", "�����@�D�q"},
								{"092-475-8639", "092-457-8342"},
								{"����20��", "����20��"},
								{"812-8639", "812-0043"},
								{"�����s�����挘���R���ڂP�U�ԂP�S��", "�����s�����挘���R���ڂP�U�ԂP�U��"}};

	
	
	public AlertDialog.Builder dialog;
	private final Integer CHECKNUM = 4;
	//���q�l�f�[�^�x�[�X����
	public UserDBHelper userDBHel;
	//�����ݒ�f�[�^�x�[�X����
	public OrderSetDBHelper orderSetDBHel;
	/*�����Ҕԍ�*/
	public String ownerId = "";
	//���q�l�����ʂ���ID
	public Integer userId;
	//���q�l���������āA���q�l����View�ɕ\�������Ă邩�`�F�b�N����
	private boolean checkDiaBtn = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clientview_layout);
		
		//���q�l�f�[�^���f�[�^�x�[�X�ɕۑ�
		saveUserDB();
		
		//"�����{�^��"�ɃN���b�N���X�i�[
		setSerchBtn_Click();
	}
	
	//"����"�{�^���ɃN���b�N���X�i�[
	public void setSerchBtn_Click() {
		Button serchButton = (Button)findViewById(R.id.serchButton);
		serchButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO �����������ꂽ���\�b�h�E�X�^�u
				showUserDialog();
			}
		});
	}
	//"����"�{�^�������������Ƃ̏���
	//"���q�l�̖��O"�̃_�C�A���O��\��������
	public void showUserDialog() {
		dialog = new AlertDialog.Builder(this);
		dialog.setTitle(R.string.guest_name);
		//�_�C�A���O���\������Ă��q�l�ݒ�{�^���������ꂽ���̏���
		//layout��userId�̂��q�l�̏���\��
		dialog.setPositiveButton(R.string.guest_setting, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO �����������ꂽ���\�b�h�E�X�^�u
				setClientView();
				checkDiaBtn = true;
			}
		});
		//listView�𐶐����āA�Z�b�e�B���O������
		dialog.setView(getListView());
		dialog.create();
		dialog.show();
	}
	//"����"�{�^�������������ɕ\��������_�C�A���O
	//���̒��ɂ���ListView��Ԃ�
	public ListView getListView() {
		//"����"�{�^�������������Ƀ_�C�A���O�ŕ\�������镶����
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, USERDATA[1]);
		ListView listView = new ListView(this);
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
		return listView;
	}

	
	//onCreate�ł��q�l�f�[�^���f�[�^�x�[�X�ɓ����
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


	//�����Ҕԍ���������4�����̐����œ����Ă��邩�`�F�b�N����ownerId�ɓ����
	public void setCheckOwnerId() {
		final Integer NUM = 4;
		EditText ownerIdEt = (EditText)findViewById(R.id.ownerId);
		String ownerIdSt = ownerIdEt.getText().toString();
		Log.d("checkOwnerId()","01 : " + ownerIdSt);
		//ownerId�ɕ�������ł͂Ȃ��Ƃ�(���炩�̕����͓����Ă���)
		if (!("".equals(ownerIdSt))) {
			//ownerId�̕�����4�����łȂ��Ƃ�
			if (!(NUM.equals(ownerIdSt.length()))) {
				Toast.makeText(this, "�����Ҕԍ��̕�������4�����ɂ��Ă��������B", Toast.LENGTH_SHORT).show();
			//ownerId�̕�����4�����̎�
			} else {
				//�����Ҕԍ���ownerId�ɓ����
				ownerId = ownerIdEt.getText().toString();
			}
		//ownerId���󕶎��̂Ƃ�
		} else {
			Toast.makeText(this, "�����Ҕԍ������Ă�������", Toast.LENGTH_SHORT).show();
		}
		
	}
	//
	public void setClientView() {
		userDBHel = new UserDBHelper(this);
		SQLiteDatabase dbUser = userDBHel.getReadableDatabase();
		
		String sql = "SELECT company, name, tenNumber, date, postNumber, address FROM userDBTable WHERE userId = ?;";
		String[] selectionArgs = { USERDATA[0][userId], };
		Cursor userCur = dbUser.rawQuery(sql, selectionArgs);
		userCur.moveToFirst();
		
		for (int i = 0;i < userCur.getColumnCount();i++) {
			//��������10�����ȏゾ��������s���ĕ\��
			//�d�b�ԍ��͉��s���Ȃ�
			if (!(USERDATA[3][userId].equals(userCur.getString(i))) && userCur.getString(i).length() > 11) {
				String textBreakFi = userCur.getString(i).substring(0, 9);//10������؂�
				String textBreakSe = userCur.getString(i).substring(9);	//11�����ڂ���Ō�܂�
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
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.menu_layout, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d("�ݒ�{�^��","01");
		setCheckOwnerId();
		if (!(CHECKNUM.equals(ownerId.length())) && !checkDiaBtn) {
			return false;
		}
////		if ((ownerId.equals("") || ownerId.length() != 4) && R.id.setting != item.getItemId()) {
////			Log.d("�ݒ�{�^��", "�����Ҕԍ���4���������Ɠ����Ă��Ȃ�");
////			return false;
////		} else if (userId == null && R.id.setting != item.getItemId()) {
////			Toast.makeText(this, "���q�l��񂪓����Ă��܂���B", Toast.LENGTH_SHORT);
////			return false;
////		} /*else {
//			setOrderSetDB();
//		}*/
		
		final EditText url = new EditText(this);
		//"�ݒ�"�p�_�C�A���O�ϐ�
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

}
