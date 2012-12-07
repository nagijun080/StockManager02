package com.example.stockmanagerforandroid;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ItemViewActivity extends Activity implements OnClickListener, DialogInterface.OnClickListener{

	//mac�̏��i�摜ID�z��
	public Integer[] itemId = { R.drawable.imac_215, R.drawable.imac_27,
								R.drawable.ipad_mini_bk, R.drawable.ipad_mini_whi,
								R.drawable.macbook_13, R.drawable.macbook_13_retina };
	//mac�̏��i���z��(itemId�ƑΉ�������)
	public String[]  itemName = { "21.5�C���`iMac", "27�C���`iMac", 
			"iPadmini �u���b�N���X�g���[�g","iPadmini �z���C�g���V���o�[", 
			"MacBookPro �P�R�C���`", "MacBookPro �P�R�C���` Retina", };
	//���i�̒l�i�z��
	public Integer[] itemValue = { 128800, 144000, 28800, 28800, 128800, 150000, };
	//���i���z��
	public String[] itemData = { "�N�A�b�h�R�AIntel Core i5", "�N�A�b�h�R�AIntel Core i5",
								"�f���A���R�AA5�`�b�v", "�f���A���R�AA5�`�b�v",
								"�f���A���R�AIntel Core i7", "�f���A���R�AIntel Core i7",	};
	//���i�̃W�������z��
	public String[] genre = { "�Ȃ�", "DeskTop", "Tablet", "NoteBook" };
	
	ItemDBHelper itemDBH;
	ItemGenreDBHelper itemGenreDBH;
	
	public ArrayAdapter<String> genAdapter;
	
	//���i����������{�^��
	public ImageButton genreButton;
	public ImageButton valueButton;
	public ImageButton rowButton;
	//���i����������{�^�����N���b�N������_�C�A���O��\������C���X�^���X
	public AlertDialog.Builder genreDialog;
	public AlertDialog.Builder valueDialog;
	public AlertDialog.Builder rowDialog;
	
	//Dialog�̃��X�g����ID
	int genreId;
	
	//�ǂ̃_�C�A���O���g���Ă��邩�`�F�b�N����
	//�_�C�A���O��\������{�^�������������ɌŒ�l������
	int checkNum;
	//�_�C�A���O�i���o�[(�Œ�l)
	private final int GENRE = 0;
	private final int VALUE = 1;
	private final int ROW = 2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.item_view_layout);
		
		//���i����������{�^���ɃN���b�N���X�i�[�o�^����
		genreButton = (ImageButton)findViewById(R.id.genruButton);
		genreButton.setOnClickListener(this);
		valueButton = (ImageButton)findViewById(R.id.valueButton);
		valueButton.setOnClickListener(this);
		rowButton = (ImageButton)findViewById(R.id.rowButton);
		rowButton.setOnClickListener(this);
		
		saveItemDB();
		//itemDB�e�[�u���̃f�[�^�x�[�X�ɓ����Ă���f�[�^��S���o��
		showItemDB(null, null);
	}

	@Override
	protected void onStop() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onStop();
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
	//ItemDBHelper��itemDB�e�[�u����insert����
	public void saveItemDB() {
		Log.d("saveItemDB()","first");
		for (int i = 0;i < itemId.length;i++) {	
			itemDBH = new ItemDBHelper(this);
			SQLiteDatabase db = itemDBH.getWritableDatabase();
			Log.d("saveItemDB()","0" + i);
			ContentValues values = new ContentValues();
			values.put("itemId", itemId[i]);
			values.put("itemName", itemName[i]);
			values.put("itemValue", itemValue[i]);
			values.put("itemData", itemData[i]);
			//itemName[0]��itemName[1]��genre[1]
			//itemName[2]��itemName[3]��genre[2]
			//itemName[4]��itemName[5]��genre[3]
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
			db.insertOrThrow("itemDB", null, values);
			Log.d("saveItemDB()", "insert end");
			itemDBH.close();
		}
	}
	
	//itemDB�e�[�u�����珤�i�����o���āA�\��
	//����:select  
	public void showItemDB(String select, String[] selectArgs) {
		SQLiteDatabase db = itemDBH.getWritableDatabase();
		ListView listView1 = (ListView)findViewById(R.id.imageList);
		String[] colmns = { "itemId", "itemName", "itemValue", "itemData", "genre"};
		Cursor c = db.query("itemDB", colmns, select, selectArgs, null, null, null);
		c.moveToFirst();
		Log.d("showItemDB()","12" + c.getString(1));
		//���i�̏�񂪑S���������Q�����z��
		String[][] item = new String[c.getCount()][colmns.length];
		for (int i = 0;i < c.getCount();i++) {
			for (int j = 0;j < colmns.length;j++) {
				item[i][j] = c.getString(j);
			}
			c.moveToNext();
		}
		//�f�[�^�̍쐬
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
		listView1.setAdapter(itemCurAda);
	}
	//Dialog��\������{�^���̏���
	public void onClick(View view) {
		ImageButton button = (ImageButton)view;
		//���i����������{�^�����N���b�N������_�C�A���O��\������		
		if (genreButton.equals(button)) {
			//�W�������{�^�������������̏���
			checkNum = GENRE;
			showGenDia();
		}
	}
	
	//�_�C�A���O�̒��ɂ���{�^���̏���
	public void onClick(DialogInterface dialog, int which) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		//�����ꂽ�_�C�A���O�{�^�����W�������{�^���������Ƃ�
		Log.d("Dialog in button", "first dialog : " + dialog.toString());
		if (checkNum == GENRE) {
			//listView�̂ǂꂪ�I�����ꂽ�����擾(genreId�ɓ����Ă���)
			//�I�����ꂽ���ڂ̃W������������f�[�^���f�[�^�x�[�X���玝���Ă���
			//�f�[�^��layout.imageList�ɕ\��
			String select = "genre = ?";
			String[] selectArgs = { genre[genreId], };
			Log.d("Dailog in Button", String.valueOf(genreId));
			if (genreId == 0) {
				select = null;
			}
			showItemDB(select, selectArgs);
		}
	}
	
	//�W�������Ə��i���f�[�^�x�[�X�ɓ����
	public void saveGenreDB() {
		itemGenreDBH = new ItemGenreDBHelper(this);
		SQLiteDatabase db = itemGenreDBH.getWritableDatabase();
		ContentValues values = new ContentValues();
		for (int i = 0;i < itemName.length;i++) {
			values.put("itemName", itemName[i]);
			//itemName[0]��itemName[1]��genre[1]
			//itemName[2]��itemName[3]��genre[2]
			//itemName[4]��itemName[5]��genre[3]
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
	
	//�W�������{�^�������������̏���
	public void showGenDia() {
		//���i�f�[�^�x�[�X����W�����������o��
		genreDialog = new AlertDialog.Builder(this);
		genAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,genre);
		genreDialog.setPositiveButton("�W�������I��", this);
		//genreDialog�̕\��,genAdapter��set
		ListView listView = new ListView(this);
		listView.setAdapter(genAdapter);
		listView.setScrollingCacheEnabled(false);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			//���X�g���ڂ��N���b�N���ꂽ���̏���
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long id) {
				//�I�����ꂽ���ڂ̏ꏊ���擾
				genreId = position;
				Toast.makeText(ItemViewActivity.this, "list position" + position, Toast.LENGTH_LONG).show();
			}
		});
		genreDialog.setView(listView);
		genreDialog.create();
		genreDialog.show();
	}
}
