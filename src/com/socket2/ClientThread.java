package com.socket2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class ClientThread implements Runnable {
	private Socket s;
	// ������UI�̷߳�����Ϣ��Handler����
	Handler handler;
	// �������UI�̵߳�Handler����
	Handler revHandler;
	// ���̴߳���Socket�����õ����������
	BufferedReader br = null;
	OutputStream os = null;

	public ClientThread(Handler handler) {
		this.handler = handler;
	}

	@Override
	public void run() {
		s = new Socket();
		try {
			s.connect(new InetSocketAddress("192.168.191.1", 1234), 5000);
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			os = s.getOutputStream();
			// ����һ�����߳�����ȡ��������Ӧ������
			new Thread() {

				@Override
				public void run() {
					String content = null;
					// ���ϵĶ�ȡSocket������������
					try {
						while ((content = br.readLine()) != null) {
							// ÿ����ȡ�����Է�����������֮�󣬷��͵���Ϣ֪ͨ����
							// ������ʾ������
							Message msg = new Message();
							msg.what = 0x123;
							msg.obj = content;
							handler.sendMessage(msg);
						}
					} catch (IOException io) {
						io.printStackTrace();
					}
				}

			}.start();
			// Ϊ��ǰ�̳߳�ʼ��Looper
			Looper.prepare();
			// ����revHandler����
			revHandler = new Handler() {

				@Override
				public void handleMessage(Message msg) {
					// ���յ�UI�̵߳����û����������
					if (msg.what == 0x345) {
						// ���û����ı������������д������
						try {
							os.write((msg.obj.toString()).getBytes("gbk"));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}

			};
			// ����Looper
			Looper.loop();

		} catch (SocketTimeoutException e) {
			Message msg = new Message();
			msg.what = 0x123;
			msg.obj = "�������ӳ�ʱ��";
			handler.sendMessage(msg);
		} catch (IOException io) {
			io.printStackTrace();
		}

	}
}
