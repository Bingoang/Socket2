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

	// 定义界面上的两个文本框
	EditText input;
	TextView show;
	// 定义界面上的一个按钮
	Button send;
	Button connect;
	Handler handler;
	// 定义与服务器通信的子线程
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
				// 如果消息来自子线程
				if (msg.what == 0x123) {
					// 将读取的内容追加显示在文本框中
					show.append("\n" + msg.obj.toString());
//					show.setText(msg.obj.toString());
				}
			}
		};
		clientThread = new ClientThread(handler);
		// 客户端启动ClientThread线程创建网络连接、读取来自服务器的数据
		new Thread(clientThread).start();
		//connect按钮触发连接
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
					// 当用户按下按钮之后，将用户输入的数据封装成Message
					// 然后发送给子线程Handler
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
