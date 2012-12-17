package com.example.stockmanagerforandroid;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapterInCart extends ArrayAdapter<CustomDialogInCart> {

	private LayoutInflater layoutInflater_;
	public CustomAdapterInCart(Context context, int textViewResourceId,
			List<CustomDialogInCart> objects) {
		super(context, textViewResourceId, objects);
		// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
		layoutInflater_ = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		//����̍s(position)�̃f�[�^�𓾂�
		CustomDialogInCart item = (CustomDialogInCart)getItem(position);
		
		//convertView�͎g���񂵂���Ă���\��������̂�null�̎������V�������
		if (null == convertView) {
			convertView = layoutInflater_.inflate(R.layout.itemlist_in_cart_layout, null);
		}
		
		//CustomDailogInCart�̃f�[�^��View�̊eWidget�ɃZ�b�g����
		ImageView image = (ImageView)convertView.findViewById(R.id.imageInCart_dialog);
		image.setImageResource(item.getImageId());
		
		TextView itemId = (TextView)convertView.findViewById(R.id.itemIdInCart);
		itemId.setText(item.getItemId());
		
		TextView itemName = (TextView)convertView.findViewById(R.id.itemNameInCart);
		itemName.setText(item.getItemName());
		
		TextView itemData = (TextView)convertView.findViewById(R.id.itemDataInCart);
		itemData.setText(item.getItemData());
		
		TextView itemNum = (TextView)convertView.findViewById(R.id.itemNumInCart);
		itemNum.setText("���F" + item.getItemNum());
		
		TextView unitPrice = (TextView)convertView.findViewById(R.id.unitPrice);
		unitPrice.setText("�P���F" + item.getItemUnitPrice());
		
		TextView minTotal = (TextView)convertView.findViewById(R.id.minTotalInCart);
		minTotal.setText("���v�F" + item.getItemMinTotal());
		
		return convertView;
	}
	
	

}
