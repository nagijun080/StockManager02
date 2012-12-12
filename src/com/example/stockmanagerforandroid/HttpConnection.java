package com.example.stockmanagerforandroid;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.AllClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class HttpConnection {

	public String doGet(String url) {
		try {
			//URLを取ってくる
			HttpGet method = new HttpGet(url);
			
			HttpParams httpParms = new BasicHttpParams();
	        httpParms.setIntParameter(AllClientPNames.CONNECTION_TIMEOUT, 3000);
	        httpParms.setIntParameter(AllClientPNames.SO_TIMEOUT, 10000);
	        DefaultHttpClient client = new DefaultHttpClient(httpParms);
			
//			//ヘッダを設定する
//			method.setHeader("Connection", "Keep-Alive");
			//GETメソッドの実行
			//レスポンスコードが帰ってくる
			HttpResponse response = client.execute(method);
			int status = response.getStatusLine().getStatusCode();
			if (status != HttpStatus.SC_OK) {
				throw new Exception("");
			}
			return EntityUtils.toString(response.getEntity(), "SJIS");
		} catch (Exception e) {
			Log.d("doGet()",e.toString());
			return "";
		}
	}
}
