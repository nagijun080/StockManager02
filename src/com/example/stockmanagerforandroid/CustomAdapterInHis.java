package com.example.stockmanagerforandroid;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomAdapterInHis extends ArrayAdapter<CustomHistoryList> {
	
	private LayoutInflater layoutInflater_;
	public CustomAdapterInHis(Context context, int textViewResourceId,
			List<CustomHistoryList> objects) {
		super(context, textViewResourceId, objects);
		// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
		layoutInflater_ = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		//����̍s(position)�̃f�[�^�𓾂�
		CustomHistoryList item = (CustomHistoryList)getItem(position);
		
		//convertView�͎g���񂵂���Ă���\��������̂�null�̎������V�������
		if (null == convertView) {
			convertView = layoutInflater_.inflate(R.layout.custom_history_list_view, null);
		}
		//CustomHistoryList�̃f�[�^��View�̊eWidget�ɃZ�b�g����@
		TextView statusView = (TextView)convertView.findViewById(R.id.status);
		statusView.setText(item.getStatus());
		
		TextView ownerView = (TextView)convertView.findViewById(R.id.owner);
		ownerView.setText(item.getOwnerId());
		
		TextView userView = (TextView)convertView.findViewById(R.id.user);
		userView.setText(item.getUserId());
		
		TextView orderView = (TextView)convertView.findViewById(R.id.orderID);
		orderView.setText(item.getOrderId());
		
		TextView totalView = (TextView)convertView.findViewById(R.id.totalValue);
		totalView.setText(item.getTotalValue());
		
		return convertView;
	}

}
