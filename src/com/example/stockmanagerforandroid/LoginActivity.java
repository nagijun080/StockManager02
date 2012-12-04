package com.example.stockmanagerforandroid;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class LoginActivity extends Activity {
	private static final String PASSWORD = "password";
	private String passCheckStr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        //loginButton��onClickListener��o�^����
        ImageButton loginButton = (ImageButton)findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO �����������ꂽ���\�b�h�E�X�^�u
				passwordCheck();
			}
        });
    }

    //loginButton�����������Ƀp�X���[�h���`�F�b�N����
    //ClientViewActivity�ɑJ�ڂ���
    public void passwordCheck() {
    	//passCheckStr�Ƀp�X���[�h������
		EditText password = (EditText)findViewById(R.id.passwordView);
        passCheckStr = password.getText().toString();
    	if (PASSWORD.equals(passCheckStr)) {
    		Intent intent = new Intent();
    		intent.setClass(this, ClientViewActivity.class);
    		startActivity(intent);
    	}
    }
}
