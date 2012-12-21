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

	/* ���i�f�[�^�z��@*/
	//mac�̏��i�摜ID�z��
	public Integer[] itemImageId = { R.drawable.imac_215, R.drawable.imac_27,
								R.drawable.ipad_mini_bk, R.drawable.ipad_mini_whi,
								R.drawable.macbook_13, R.drawable.macbook_13_retina };
	public String[] itemId = { "20001", "20002", "20003", "20004", "20005", "20006", };
	//mac�̏��i���z��(itemId�ƑΉ�������)
	public String[]  itemName = { "21.5�C���`iMac", "27�C���`iMac", 
			"iPadmini �u���b�N���X�g���[�g","iPadmini �z���C�g���V���o�[", 
			"MacBookPro �P�R�C���`", "MacBookPro �P�R�C���` Retina", };
	//���i�̒l�i�z��
	public Integer[] itemValue = { 128800, 144000, 28800, 30800, 130000, 150000, };
	//���i���z��
	public String[] itemData = { "�N�A�b�h�R�AIntel Core i5", "�N�A�b�h�R�AIntel Core i5",
								"�f���A���R�AA5�`�b�v", "�f���A���R�AA5�`�b�v",
								"�f���A���R�AIntel Core i7", "�f���A���R�AIntel Core i7",	};
	//���i�̃W�������z��
	public String[] genre = { "�Ȃ�", "DeskTop", "Tablet", "NoteBook" };
	/* ���i�f�[�^�z�񂱂��܂Ł@*/
	
	ItemDBHelper itemDBH;
	
	public ArrayAdapter<String> genAdapter;
	public ArrayAdapter<Integer> valueAdapter;
	
	//���i����������{�^��
	public ImageButton genreButton;
	public ImageButton valueButton;
	public ImageButton rowButton;
	//���i����������{�^�����N���b�N������_�C�A���O��\������C���X�^���X
	public AlertDialog.Builder genreDialog;
	public Builder valueDialog;
	public AlertDialog.Builder rowDialog;
	
	//Dialog�̃��X�g����ID
	Integer genreId = 0;
	Integer valueId;
	Integer alignId = 0;
	
	//�ǂ̃_�C�A���O���g���Ă��邩�`�F�b�N����
	//�_�C�A���O��\������{�^�������������ɌŒ�l������
	int checkNum;
	//�_�C�A���O�i���o�[(�Œ�l)
	private final int GENRE = 0;
	private final int VALUE = 1;
	private final int ROW = 2;
	
	//itemDB�f�[�^�x�[�X���g���Č�������Ƃ��̕ϐ�
	public String select;	
	public String[] selectArgs;
	public String orderBy;
	//���i�ѐݒ�Ɏg���ϐ�
	private String min = "";
	private String max = "";
	public View itemValVw;
	public View itemRowVw;
	
	public EditText url;
	//"���i�̏ڍ�"�_�C�A���O�BListView���N���b�N������\��
	public View buyView;
	//intent��ClientView����ownerId��userId��������Ă���
	public String ownerId;
	public String userId;
	
	//�J�[�g�{�^�������������̃_�C�A���O
	AlertDialog.Builder cartDialog;
	View cartView;
	//�����������ɐݒ肷��ID�B�������ƂɈႤ
	//1000����X�^�[�g����
	public Integer orderId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.item_view_layout);
		//itemDB�Ƀf�[�^������
		saveItemDB();
		//itemDB�e�[�u���ɓ����Ă���f�[�^��ListView�ɕ\��
		showItemDB(null, null, null);
		urlSt = "172.16.80.35/android/index.php/?";
		//����ID�B�������邽�т�orderId�Ɂ{�P�����
		setOrderId();
		//Layout��valueView�������s����
		TextView valueV = (TextView)findViewById(R.id.valueView);
		valueV.setText("���i��\n�i�荞��");
		
		//�J�[�g�_�C�A���O�\���{�^��
		cartDialog = new AlertDialog.Builder(this);
		//"���i���X�g"�ɃN���b�N���X�i�[
		itemListClick();
		//"�J�[�g"�{�^���ɃN���b�N���X�i�[
		cartClick();
		//ClientViewActivity����ownerId��userId�������Ă���
		Intent intent = getIntent();
		ownerId = intent.getStringExtra("ownerId_");
		userId = intent.getStringExtra("userId_");
		
		//���i����������{�^���ɃN���b�N���X�i�[�o�^����
		genreButton = (ImageButton)findViewById(R.id.genruButton);
		genreButton.setOnClickListener(this);
		valueButton = (ImageButton)findViewById(R.id.valueButton);
		valueButton.setOnClickListener(this);
		rowButton = (ImageButton)findViewById(R.id.rowButton);
		rowButton.setOnClickListener(this);
		
	}
	
	//ItemDBHelper��itemDB�e�[�u����insert����
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
	
	//itemDB�e�[�u�����珤�i�����o���āA�\��
	//����1:select �����J����
	//����2:selectArgs�@�����J�����ɑ�����镶����
	/*genre�ʂ�*/
	public void showItemDB(String select, String[] selectArgs, String orderBy){
		ItemDBHelper itemDBH = new ItemDBHelper(this);
		SQLiteDatabase db = itemDBH.getWritableDatabase();
		ListView imageList = (ListView)findViewById(R.id.imageList);
		String[] colmns = { "itemId", "itemImageId", "itemName", "itemValue", "itemData", "genre"};
		Cursor c = db.query("itemDB", colmns, select, selectArgs, null, null, orderBy);
		//���i�̏�񂪑S���������Q�����z��
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
			//�f�[�^�̍쐬
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
	//Dialog��\������{�^���̏���
	public void onClick(View view) {
		//���i����������{�^�����N���b�N������_�C�A���O��\������		
		if (genreButton.equals(view)) {
			//�W�������{�^�������������̏���
			checkNum = GENRE;
			showGenDia();
		} else if (valueButton.equals(view)) {
			//���������i�{�^���ɂ������̐ݒ�
			checkNum = VALUE;
			Log.d("onClick().valueButton","01");
			showValDia();
		} else if (rowButton.equals(view)) {
			//���i���т�ς���{�^�������������̏���
			checkNum = ROW;
			showRowDia();
		}
	}
	
	//�_�C�A���O�̒��ɂ���{�^���̏���(���i�ݒ�̎��͂Ȃ�)
	public void onClick(DialogInterface dialog, int which) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		//�����ꂽ�_�C�A���O�{�^�����W�������{�^���������Ƃ�
		Log.d("Dialog in button", "first dialog : " + dialog.toString());
		if (checkNum == GENRE) {
			//listView�̂ǂꂪ�I�����ꂽ�����擾(genreId�ɓ����Ă���)
			//�I�����ꂽ���ڂ̃W������������f�[�^���f�[�^�x�[�X���玝���Ă���
			//�f�[�^��layout.imageList�ɕ\��
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
			//�ǂ̃W��������termGenre�ɕ\��������
			TextView text = (TextView)findViewById(R.id.termGenre);
			text.setText("���� : " + genre[genreId]);
			showItemDB(select, selectArgs, orderBy);
		} else if (checkNum == VALUE) {
			setValView();
			showItemDB(select, selectArgs, orderBy);
		} else if (checkNum == ROW) {
			//���i�̕��ёւ��_�C�A���O�̒��ɂ���{�^�������������̏���
			showItemDB(select,selectArgs,orderBy);
		}
	}

	//"�W�������{�^��"�����������̏���
	public void showGenDia() {
		genreDialog = new AlertDialog.Builder(this);
		genAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,genre);
		genreDialog.setPositiveButton("�W�������I��", this);
		//genreDialog�̕\��,genAdapter��set
		ListView listView = new ListView(this);
		listView.setAdapter(genAdapter);
		listView.setScrollingCacheEnabled(false);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			//"�W���������X�g"�̍��ڂ��N���b�N���ꂽ���̏���
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long id) {
				//�I�����ꂽ���ڂ̏ꏊ���擾
				genreId = position;
				Toast.makeText(ItemViewActivity.this, genre[genreId] + "�ł��B", Toast.LENGTH_SHORT).show();
			}
		});
		genreDialog.setView(listView);
		genreDialog.create();
		genreDialog.show();
	}
	
	//���i�{�^�������������̏���
	/*valueDialog��term_value_layout��set*/
	public void showValDia() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		Log.d("showValDia()", "01");
		valueDialog = new AlertDialog.Builder(this);
		LayoutInflater inflater = LayoutInflater.from(this);
		itemValVw = inflater.inflate(R.layout.term_value_layout, (ViewGroup)findViewById(R.id.termValueLl));
		valueDialog.setPositiveButton("���i�ѐݒ�", this);
		valueDialog.setView(itemValVw);
		
		valueDialog.create();
		valueDialog.show();
	}
	//���i�̕��я���ς��鏈��
	public void showRowDia() {
		rowDialog = new AlertDialog.Builder(this);
		LayoutInflater inflater = LayoutInflater.from(this);
		itemRowVw = inflater.inflate(R.layout.row_layout, (ViewGroup)findViewById(R.id.alignLl));
		String[] alignListSt = { "�Ȃ�", "���i���F����", "���i���F����", };
		ArrayAdapter<String> alignAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,alignListSt);
		rowDialog.setPositiveButton("���ёւ��ݒ�", this);
		rowDialog.setView(itemRowVw);
		ListView alignList = (ListView)itemRowVw.findViewById(R.id.alignList);
		alignList.setAdapter(alignAdapter);
		alignList.setBackgroundColor(Color.WHITE);
		alignList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			//���я����Ƃ̏���
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long id) {
				// TODO �����������ꂽ���\�b�h�E�X�^�u
				alignId = position;
				switch(position) {
				case 0://���я��Ȃ�
					orderBy = null;
					break;
				case 1://����
					orderBy = "itemValue ASC";
					break;
				case 2://�~��
					orderBy = "itemValue DESC";
					break;
				}
			}
			
		});
		
		TextView alignText = (TextView)findViewById(R.id.termRow);
		alignText.setText("���� : " + alignListSt[alignId]);
		rowDialog.create();
		rowDialog.show();
	}
	
	//���i�ݒ�_�C�A���O�ōŏ��l����ő�l�܂ł�listView�ɕ\��������
	public void setValView() {
		
		EditText minEditVw = (EditText)itemValVw.findViewById(R.id.minText);
		EditText maxEditVw = (EditText)itemValVw.findViewById(R.id.maxText);
		Log.d("setValView()", minEditVw.getText().toString());
		min = minEditVw.getText().toString();
		max = maxEditVw.getText().toString();
		
		//�W���������Ȃ��ꍇ
		if (genreId == 0) {
			select = "itemValue >= ? and itemValue <= ?";
			selectArgs = new String[]{ min, max };
			//�ő�l���O�܂��͍ŏ��l�ƍő�l�ɖ�����
			//�S���\��
			if (max.equals("0") || min.equals("") && max.equals("")) {
				select = null;
				selectArgs = null;
			} else if (max == null) {
				//�ő�l�������͂���Ă��Ȃ�
				select = "itemValue >= ?";
				selectArgs = new String[] { min, };
			}
		} else {
			//�W������������ꍇ
			select = "itemValue >= ? and itemValue <= ? and genre = ?";
			selectArgs = new String[] { min, max, genre[genreId] };
			//�W���������ڂ����\��
			if (max.equals("0") || min.equals("") && max.equals("")) {
				select = "genre = ?";
				selectArgs = new String[] { genre[genreId], };;
			} else if (max.equals("")) {
				//�ő�l�������͂���Ă��Ȃ�
				select = "itemValue >= ? and genre = ?";
				selectArgs = new String[] { min, genre[genreId]};
			}
		}
		//textView�ɉ��i���ǂꂾ�����\��������
		TextView valText = (TextView)findViewById(R.id.termValue);
		valText.setText("���� : \n" + min + " �` " + max);
	}
	
	//"���i���X�g"�ɃN���b�N���X�i�[
	public void itemListClick() {
		ListView itemList = (ListView)findViewById(R.id.imageList);
		itemList.setOnItemClickListener(new OnItemClickListener() {
			//����View��ListView��View
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long id) {
				// TODO �����������ꂽ���\�b�h�E�X�^�u
				//���i�̏ڍ׃_�C�A���O��\��
				showListDialog();
				setListDiaView(view);
			}
			
		});
		
	}
	//���X�g���N���b�N�������_�C�A���O��\��������
	//buyView��inflate���ă��C�A�E�g���g����悤�ɂ��Ă���
	public void showListDialog() {
		LayoutInflater inflater = LayoutInflater.from(this);
		//"���i�̏ڍ׃_�C�A���O"��View
		buyView = inflater.inflate(R.layout.item_buy_layout, (ViewGroup)findViewById(R.id.itemBuyLayout));
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setView(buyView);
		//"�������J�S�ɒǉ�"�{�^��
		alertDialog.setPositiveButton("�������J�S�ɒǉ�", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO �����������ꂽ���\�b�h�E�X�^�u
				setOrder_DB();
				setTotalNumView();
			}
			
		});
		alertDialog.create();
		alertDialog.show();
	}
	
	//���i�_�C�A���O���o�������Ƀf�[�^�x�[�X����
	//���i�̏����Ƃ��ă_�C�A���O�ɕ\��
	//����:ListView��View
	//view�ɕ\��������
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
	
	//�����F�f�[�^�x�[�X�̃J�[�\��
	//�J�[�\����get�Ńf�[�^���擾����
	//���̃f�[�^��layout�ɔ��f������
	//itemId, itemImageId, itemValue, itemName, itemData�̏���
	//itemValue�������v�Ɏg���̂ŕʕϐ��ɓ����
	Integer value;
	//���i�摜�������Ă��郊�\�[�XID
	Integer item_ImageId;
	//���i�̌�������ϐ�
	public Integer item_count = 1;
	//���i��ID������ϐ�
	public String item_ID;
	//���i�̏��v
	public Integer item_mTotalValue;
	public void setBuyLayout(Cursor c) {
		TextView item_numView = (TextView)buyView.findViewById(R.id.itemCount);
		item_numView.setText("���F" + item_count);
		
		TextView itemIdView = (TextView)buyView.findViewById(R.id.itemNumber);
		item_ID = c.getString(0);
		itemIdView.setText("�i��:" + item_ID);
		
		ImageView itemImageVw = (ImageView)buyView.findViewById(R.id.itemImage);
		item_ImageId = c.getInt(1);
		itemImageVw.setImageResource(item_ImageId);
		
		//���v�Ɏg���̂ŕʕϐ��ɓ����
		TextView valueView = (TextView)buyView.findViewById(R.id.value);
		value = c.getInt(2);
		valueView.setText("�P���F" + value.toString());
		
		//���i�̌���ύX����
		item_Num_ValueChange();
		TextView minTotalVw = (TextView)buyView.findViewById(R.id.minTotal);
		minTotalVw.setText("���v�F" + item_mTotalValue.toString());
		
		TextView nameView = (TextView)buyView.findViewById(R.id.itemName);
		nameView.setText(c.getString(3));
		
		TextView dataView = (TextView)buyView.findViewById(R.id.itemData);
		dataView.setText(c.getString(4));
	}
	//"���i�ڍ׃_�C�A���O"��"���ύX"�{�^���̏���
	public void item_Num_ValueChange() {
		item_mTotalValue = value * item_count;
		Button downButton = (Button)buyView.findViewById(R.id.downButton);
		downButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO �����������ꂽ���\�b�h�E�X�^�u
				if (!(item_count <= 1)) {
					item_count--;
				}
				item_mTotalValue = value * item_count;				
			}
		});
		
		Button upButton = (Button)buyView.findViewById(R.id.upButton);
		upButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO �����������ꂽ���\�b�h�E�X�^�u
				item_count++;
				item_mTotalValue = value * item_count;
			}
		});
	}
	
	//orderSetDBTable��insert����
	//"���i�ڍ׃_�C�A���O"��"�������J�S�ɒǉ�"�{�^���Ńf�[�^�x�[�X��insert����
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
	
	//"���i�ڍ׃_�C�A���O"��"�������J�S�ɒǉ�"�{�^����
	//"���i���X�g"layout��"�������J�S"View�Ɍ��𔽉f������
	public void setTotalNumView() {
		OrderSetDBHelper orderSet_DBH = new OrderSetDBHelper(this);
		SQLiteDatabase db_order = orderSet_DBH.getReadableDatabase();
		
		String sql = "SELECT ownerId, itemId FROM orderSetDBTable WHERE ownerId = ? GROUP BY itemId;";
		Cursor cur = db_order.rawQuery(sql, new String[] { ownerId, });
		Integer itemType_num = cur.getCount();
		Log.d("setTotalNumView","���i�̎�ޕʌ�" + String.valueOf(itemType_num));
		orderSet_DBH.close();			
		
		TextView itemNum_View = (TextView)findViewById(R.id.itemNum_view);
		itemNum_View.setText(itemType_num.toString());
		
	}
	
	//"�J�[�g"�{�^�������������Ƃ̏���
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
		
		//"���̓��e�Ŕ������M"�{�^���̏���
		sendOrder();
		//"�[�i�ݒ�"�{�^���̏���
		orderItem_set();
		//"�����ɕۑ�"�{�^���̏���
		saveHistoryDB();
		//"���ׂč폜"�{�^���̏���
		setAllDel_Click();
		
	}

	//��������������OrderSetDB��������ϐ�
	Integer[] itemId_In_OrderDB = new Integer[10];
	Integer[] itemNum_In_OrderDB = new Integer[10];
	Integer[] minTotal_In_OrderDB = new Integer[10];
	//imageId_In_OrderDB������ItemDBHelper������Ɠ���
	String[] itemId_In_ItemDB = new String[10];
	Integer[] itemImageId_ItemDB = new Integer[10];
	String[] itemName_In_ItemDB = new String[10];
	String[] itemData_In_ItemDB = new String[10];
	String[] unitPrice_In_ItemDB = new String[10];
	//�J�[�g�̒��ɓ����Ă��鏤�i��//http�ʐM������� = �J�[�g�ɓ��ꂽ��
	int orderCount = 0;
	//"�J�[�g"�{�^�������������A�f�[�^�x�[�X���甭�����������Ă���
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
	
	//imageId_In_OrderDB�ɓ����Ă��郊�\�[�XID������itemDBHelper����
	//���i����z��ɃZ�b�g����
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
	
	//"�J�[�g"�{�^���ɃN���b�N���X�i-�o�^
	public void cartClick() {
		Button cartButton = (Button)findViewById(R.id.btnCart);
		cartButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO �����������ꂽ���\�b�h�E�X�^�u	
				//"�J�[�g"�̒��ɓ����Ă��鏤�i����U�z��ɓ����
				setOrderDB_In_Cart();
				//"�J�[�g"�_�C�A���O��\��
				showCartDialog();
			}
		});
	}
	Boolean httpBool = false;
	//"�J�[�g"����"���̓��e�Ŕ������M"�{�^���ɃN���b�N���X�i�[
	public void sendOrder() {
		Button sendOrderBtn = (Button)cartView.findViewById(R.id.sendOrderBtn);
		sendOrderBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO �����������ꂽ���\�b�h�E�X�^�u
				//"�����f�[�^�x�[�X"�ɔ�������ۑ�
				saveHistoryDB();
				/* http�ʐMThread */
				new Thread( new Runnable() {
					public void run() {
						for (int i = 0;i < orderCount;i++) {
							HttpConnection httpConect = new HttpConnection();
							String url_ = "http://" + urlSt;
							String uri = "orderId:=" + orderId.toString() + "&" + "ownerId:=" + ownerId + "&" + "itemId:=" + itemId_In_ItemDB[i] + "&"
										+ "unitPrice:=" + unitPrice_In_ItemDB[i] + "&" + "itemNum:=" + itemNum_In_OrderDB[i] 
										+ "&" + "itemMinTotal:=" + minTotal_In_OrderDB[i];
							String response = httpConect.doGet(url_ + uri);
							//�ʐM���ł�����true,�ł��Ȃ�������false
							if (response != null) {
								httpBool = true;
							} else {
								httpBool = false;
							}
							System.out.println("Response : " + response + "httpBool:" + httpBool);
						}
					}
				}).start();
				/* http�ʐMThread�I�� */
				setAllDel_Click();
				//�V��������ID���擾
				setOrderId();
			}			
		});
	}
	
	//�ݒ��URL��orderSetDBTable�ɕۑ�����
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
	//"�J�[�g"�{�^��push��
	//�_�C�A���O�\����
	//"�[�i�ݒ�"�{�^���ɃN���b�N���X�i�[��
	/* "�[�i�ݒ�"�{�^�������������Ƃ̏��� */
	public void orderItem_set() {
		//dateListView�̃N���b�N���ꂽ���t�f�[�^������
		LayoutInflater inflater = LayoutInflater.from(this);
		//View cart_dialogView = inflater.inflate(R.layout.cart_dialog_view, (ViewGroup)findViewById(R.id.dialogInCart_ll));
		orderSet_View = inflater.inflate(R.layout.order_set_dialog_layout, (ViewGroup)findViewById(R.id.orderSet_dialog_Ll));
		orderDateView = (TextView)orderSet_View.findViewById(R.id.sendOrder_date);
		Log.d("orderItem_set()", "01");
		Button deliBtn = (Button)cartView.findViewById(R.id.deliveryBtn);
		deliBtn.setOnClickListener(new OnClickListener() {	
			//"�[�i�ݒ�"�{�^��
			public void onClick(View v) {
				Log.d("orderItem_set()", "02");
				// TODO �����������ꂽ���\�b�h�E�X�^�u
				orderSet_Dialog = new AlertDialog.Builder(ItemViewActivity.this);
				orderSet_Dialog.setPositiveButton("�[�i����ݒ�", new DialogInterface.OnClickListener() {
					//"�[�i����ݒ�"�{�^��
					public void onClick(DialogInterface dialog, int which) {
						// TODO �����������ꂽ���\�b�h�E�X�^�u
						//���͂����𔭒��f�[�^�x�[�X�Ɋi�[
						orderDate_setDB(); 
						
					}
				});
				//ownerId�����ɊeWidget�Ƀf�[�^�x�[�X���玝���Ă���set
				setOrderView_from_userDB(orderSet_View);
				//orderSet_View�ɔ[�i�ݒ��layout���Z�b�g
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
		//"�z�B���ύX"�{�^��
		Button haitatsuChange_Btn = (Button)orderSet_View.findViewById(R.id.haitatsubi_change);
		haitatsuChange_Btn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO �����������ꂽ���\�b�h�E�X�^�u
				orderDateChange_Dialog.create();
				orderDateChange_Dialog.show();
			}
			
		});
		orderDateChange_Dialog.setPositiveButton("���͂����ݒ�", new DialogInterface.OnClickListener() {
			//"���͂����ݒ�"�{�^��
			public void onClick(DialogInterface dialog, int which) {
				// TODO �����������ꂽ���\�b�h�E�X�^�u
				orderDateView.setText("�z�B��]���@" + orderDateTx);
			}
		});
		
		dateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			//"�z�B���ύX"�{�^�������������Ƃ�ListViewItemClick
			public void onItemClick(AdapterView<?> adapterView, View view, int position,
					long id) {
				// TODO �����������ꂽ���\�b�h�E�X�^�u
				ListView listView = (ListView) adapterView;
                // �N���b�N���ꂽ�A�C�e�����擾���܂�
                orderDateTx = (String) listView.getItemAtPosition(position);
			}
			
		});
	}
		
	String orderDateTx;
	
	//"�[�i�ݒ�"�{�^���������ꂽ���A�f�[�^�x�[�X���玝���Ă���View�ɃZ�b�g����
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
	//���t����
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
	//"�[�i����ݒ�"�{�^�������������̏���
	//�z�B����orderSetDBTable�Ɋi�[
	public void orderDate_setDB() {
		OrderSetDBHelper orderSetDB = new OrderSetDBHelper(this);
		SQLiteDatabase db_order = orderSetDB.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("orderDate", orderDateTx);
		db_order.update("orderSetDBTable", values, "ownerId = ?", new String[]{ ownerId, });
		orderSetDB.close();
	}
	
	//"�J�[�g"�{�^�������������Ƃ̃_�C�A���O�̒��̏���
	//"���ׂč폜"�{�^���ɃN���b�N���X�i�[
	public void setAllDel_Click() {
		Button allClearBtn = (Button)cartView.findViewById(R.id.allClear);
		allClearBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO �����������ꂽ���\�b�h�E�X�^�u
				orderCount = 0;
				item_count = 1;
				allClear();
				setTotalNumView();
			}
			
		});
	}
	
	//"���ׂč폜"�{�^�������������Ƃ̏���
	//"�J�[�g"�̒��ƃf�[�^�x�[�X�Ɣz�񂩂珤�i���폜����
	public void allClear() {
		ListView cartList = (ListView)cartView.findViewById(R.id.itemListInCart);
		ArrayAdapter adapter = (ArrayAdapter)cartList.getAdapter();
		adapter.clear();
		
		OrderSetDBHelper orderDBH = new OrderSetDBHelper(this);
		SQLiteDatabase db_order = orderDBH.getWritableDatabase();
		db_order.delete("orderSetDBTable", "orderId = ?", new String[] { orderId.toString(), });
		orderDBH.close();
		
		//��������������OrderSetDB��������ϐ�
		itemId_In_OrderDB = new Integer[10];
		itemNum_In_OrderDB = new Integer[10];
		minTotal_In_OrderDB = new Integer[10];
		//imageId_In_OrderDB������ItemDBHelper������Ɠ���
		itemId_In_ItemDB = new String[10];
		itemImageId_ItemDB = new Integer[10];
		itemName_In_ItemDB = new String[10];
		itemData_In_ItemDB = new String[10];
		unitPrice_In_ItemDB = new String[10];
	}
	
	//"�J�[�g"�{�^��������������"�J�[�g"�̒��ɂ��邷�ׂĂ̍��v�l��Ԃ�
	public Integer getTotal_In_Cart() {
		Integer total = 0;
		for (int i = 0;i < orderCount;i++) {
			total += minTotal_In_OrderDB[i];
		}
		return total;
	}
	
	//"�J�[�g"���̍��v�l��"�J�[�g"�_�C�A���O�ɕ\��
	public void showCartTotal() {
		TextView totalView = (TextView)cartView.findViewById(R.id.totalView);
		totalView.setText("���v�F" + getTotal_In_Cart());
	}
	
	//���i�̔���ID��orderId�ɃZ�b�g
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
	
	//"�����ɕۑ�"�{�^���ɃN���b�N���X�i�[
	public void setHistory_Click() {
		Button historyBtn = (Button)cartView.findViewById(R.id.historySaveBtn);
		historyBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO �����������ꂽ���\�b�h�E�X�^�u
				saveHistoryDB();
			}
			
		});
	}
	
	//"�����ɕۑ�"�{�^����HistoryDB�Ɋi�[
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
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.menu_layout, menu);
		return true;
	}
	String urlSt;
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		url = new EditText(this);
		url.setText(urlSt);
		//"�ݒ�"�p�_�C�A���O�ϐ�
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
					urlSt = url.getText().toString();
				}
	    	}).show();
	    	return true;
	    }
	    return false;
	}
}