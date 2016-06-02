package com.socket2;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	// ��������ϵ������ı���
	EditText input;
	TextView show;
	// ��������ϵ�һ����ť
	Button send;
	Button connect;
	Handler handler;
	// �����������ͨ�ŵ����߳�
	ClientThread clientThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		input = (EditText) findViewById(R.id.input);
		show = (TextView) findViewById(R.id.show);
		send = (Button) findViewById(R.id.send);
		connect = (Button) findViewById(R.id.connect);
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// �����Ϣ�������߳�
				if (msg.what == 0x123) {
					// ����ȡ������׷����ʾ���ı�����
					show.append("\n" + msg.obj.toString());
//					show.setText(msg.obj.toString());
				}
			}
		};
		clientThread = new ClientThread(handler);
		// �ͻ�������ClientThread�̴߳����������ӡ���ȡ���Է�����������
		new Thread(clientThread).start();
		//connect��ť��������
		connect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View a) {
				new Thread(clientThread).start();

			}
		});

		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					// ���û����°�ť֮�󣬽��û���������ݷ�װ��Message
					// Ȼ���͸����߳�Handler
					Message msg = new Message();
					msg.what = 0x345;
					msg.obj = input.getText().toString();
					clientThread.revHandler.sendMessage(msg);
					input.setText("");

				} catch (Exception e) {

				}
			}
		});
	}
}
