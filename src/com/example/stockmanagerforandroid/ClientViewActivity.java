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

	//�Ƃ肠�����z��ŁB����SQLite�Ńf�[�^�x�[�X����o��
	private int NUM = 2;
	public String[] USERNAME = {"�����������", "���[�R�["};
	public String[] CHARGENAME = {"�����@���Y", "�����@�D�q"};
	public String[] CONTACTNAME = {"092-475-8639", "092-457-8342"};
	public String[] DATENAME = {"����20��", "����20��"};
	public String[] POSTNUMBER = {"812-8639", "812-0043"};
	public String[] ADDRESSNAME = {"�����s�����挘���R���ڂP�U�ԂR�U��", "�����s�����挘���R���ڂP�U�ԂP�S��"};
	//�����܂Ŕz��ŁA����SQLite
	
	//���X�g���ڂ������ꂽ�Ƃ��ɍ��ڂɊ֘A���閼�O������ϐ�
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
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clientview_layout);
		
		Log.d("ClientViewActivity()", "01");
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, USERNAME);
		Log.d("ClientViewActivity()", adapter.getItem(0));
		//listViewInDialog(adapter);
		//serchButton����������Dialog���\�������
		//serchButton��OnClickListener�o�^
		Button serchButton = (Button)findViewById(R.id.serchButton);
		serchButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO �����������ꂽ���\�b�h�E�X�^�u
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
				
				Toast.makeText(ClientViewActivity.this, "dialog", Toast.LENGTH_LONG).show();
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
	

}
