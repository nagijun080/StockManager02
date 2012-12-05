package com.example.stockmanagerforandroid;

import android.content.Context;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;

public class ItemCursorAdapter extends SimpleCursorAdapter {

	public ItemCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);
		// TODO 自動生成されたコンストラクター・スタブ
	}

}
