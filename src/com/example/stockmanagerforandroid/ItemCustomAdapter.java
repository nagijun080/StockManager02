package com.example.stockmanagerforandroid;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemCustomAdapter extends ArrayAdapter<CustomData> {
	private LayoutInflater layoutInflater_;
	
	public ItemCustomAdapter(Context context, int textViewResourceId,
			List<CustomData> objects) {
		super(context, textViewResourceId, objects);
		// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
		layoutInflater_ = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	//ListView�̊e�s�ɕ\������A�C�e����Ԃ�
	//�e�s��\�����悤�Ƃ������ɌĂ΂��
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		// ����̍s(position)�̃f�[�^�𓾂�
		CustomData item = (CustomData)getItem(position);
		
		//convertView�͎g���񂵂���Ă���\��������̂�null�̎������V�������
		//layout��list_item��View�ɂ��Ă���
		if (null == convertView) {
			convertView = layoutInflater_.inflate(R.layout.list_item, null);
		}
		
		//CustomData�̃f�[�^��View�̊eview�ɃZ�b�g����
		ImageView imageView = (ImageView)convertView.findViewById(R.id.imageView);
		TextView idText = (TextView)convertView.findViewById(R.id.itemId);
		TextView nameText = (TextView)convertView.findViewById(R.id.itemName);
		TextView valueText = (TextView)convertView.findViewById(R.id.itemValue);
		TextView dataText = (TextView)convertView.findViewById(R.id.itemData);
		
		idText.setText(item.getItemId());
		imageView.setImageResource(item.getItemImageId());
		nameText.setText(item.getItemName());
		valueText.setText(item.getItemValue().toString());
		dataText.setText(item.getItemData());
		
		return convertView;
	}

	
}
