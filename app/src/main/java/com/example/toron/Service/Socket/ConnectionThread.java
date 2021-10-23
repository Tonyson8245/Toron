package com.example.toron.Service.Socket;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ConnectionThread extends Thread{
    String IpAddress;
    int port;
    Socket socket;// 자바에서 소켓 Constructor 에서 받은 데이터로서 소켓 생성
    String user_user_idx,TAG = "ConnectionThread";

    public Socket getSocket() {
        return socket;
    }

    public ConnectionThread(String ipAddress, int port, String user_user_idx){
        IpAddress = ipAddress;
        this.port = port;
        this.user_user_idx = user_user_idx;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(IpAddress,port);
            OutputStream os = socket.getOutputStream(); //소켓 OutputStream os 선언
            DataOutputStream dos = new DataOutputStream(os); //  DataOutputStream dos 선언
            dos.writeUTF(user_user_idx); //dos 를 톻해 접속시 닉네임 전송
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

