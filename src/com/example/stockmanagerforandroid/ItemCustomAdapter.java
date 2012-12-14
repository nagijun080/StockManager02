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
		// TODO 自動生成されたコンストラクター・スタブ
		layoutInflater_ = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	//ListViewの各行に表示するアイテムを返す
	//各行を表示しようとした時に呼ばれる
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自動生成されたメソッド・スタブ
		// 特定の行(position)のデータを得る
		CustomData item = (CustomData)getItem(position);
		
		//convertViewは使い回しされている可能性があるのでnullの時だけ新しく作る
		//layoutのlist_itemをViewにしている
		if (null == convertView) {
			convertView = layoutInflater_.inflate(R.layout.list_item, null);
		}
		
		//CustomDataのデータをViewの各viewにセットする
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
