package com.example.stockmanagerforandroid;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpConnection {

	public String doGet(String url) {
		try {
			//URL������Ă���
			HttpGet method = new HttpGet(url);
			DefaultHttpClient client = new DefaultHttpClient();
			
			//�w�b�_��ݒ肷��
			method.setHeader("Connection", "Keep-Alive");
			//GET���\�b�h�̎��s
			//���X�|���X�R�[�h���A���Ă���
			HttpResponse response = client.execute(method);
			int status = response.getStatusLine().getStatusCode();
			if (status != HttpStatus.SC_OK) {
				throw new Exception("");
			}
			return EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (Exception e) {
			return null;
		}
	}
}
