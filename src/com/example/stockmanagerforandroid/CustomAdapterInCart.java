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
		// TODO 自動生成されたコンストラクター・スタブ
		layoutInflater_ = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自動生成されたメソッド・スタブ
		//特定の行(position)のデータを得る
		CustomDialogInCart item = (CustomDialogInCart)getItem(position);
		
		//convertViewは使い回しされている可能性があるのでnullの時だけ新しく作る
		if (null == convertView) {
			convertView = layoutInflater_.inflate(R.layout.itemlist_in_cart_layout, null);
		}
		
		//CustomDailogInCartのデータをViewの各Widgetにセットする
		ImageView image = (ImageView)convertView.findViewById(R.id.imageInCart_dialog);
		image.setImageResource(item.getImageId());
		
		TextView itemId = (TextView)convertView.findViewById(R.id.itemIdInCart);
		itemId.setText(item.getItemId());
		
		TextView itemName = (TextView)convertView.findViewById(R.id.itemNameInCart);
		itemName.setText(item.getItemName());
		
		TextView itemData = (TextView)convertView.findViewById(R.id.itemDataInCart);
		itemData.setText(item.getItemData());
		
		TextView itemNum = (TextView)convertView.findViewById(R.id.itemNumInCart);
		itemNum.setText("個数：" + item.getItemNum());
		
		TextView unitPrice = (TextView)convertView.findViewById(R.id.unitPrice);
		unitPrice.setText("単価：" + item.getItemUnitPrice());
		
		TextView minTotal = (TextView)convertView.findViewById(R.id.minTotalInCart);
		minTotal.setText("小計：" + item.getItemMinTotal());
		
		return convertView;
	}
	
	

}
