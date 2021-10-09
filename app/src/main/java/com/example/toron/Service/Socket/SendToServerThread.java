package com.example.toron.Service.Socket;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SendToServerThread extends Thread{
    Socket socket;
    String msg,TAG = "SendToServerThread:";
    DataOutputStream dos;

    public SendToServerThread(Socket socket, String msg){
        try{
            this.socket=socket;
            this.msg=msg;
            OutputStream os=socket.getOutputStream();
            dos=new DataOutputStream(os);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            // 서버로 데이터를 보낸다.
            dos.writeUTF(msg);
            Log.d(TAG,msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}