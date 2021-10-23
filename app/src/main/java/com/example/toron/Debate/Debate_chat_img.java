package com.example.toron.Debate;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.toron.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;

import static android.content.ContentValues.TAG;

public class Debate_chat_img extends AppCompatActivity {

    Button btn_back,btn_img_download;
    ImageView Img_chat;

    private DownloadManager mDownloadManager;
    private Context mContext;

    private String outputFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/khs") + "/";
    private Long mDownloadQueueId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debate_chat_img);
        mContext = this;


        btn_back = findViewById(R.id.btn_back);
        btn_img_download = findViewById(R.id.btn_img_download);
        Img_chat = findViewById(R.id.Img_chat);

        Intent getData = getIntent();
        String url =  getData.getStringExtra("url");

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_img_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String downloadUri = url;
                String uri[] = downloadUri.split("/");
                URLDownloading(Uri.parse(downloadUri),uri[5]);
            }
        });

        Picasso.get().invalidate(url);
//        Picasso.get().load(url).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(Img_chat);
        Picasso.get().load(url).into(Img_chat);
    }

    @Override
    public void onResume(){
        super.onResume();
        IntentFilter completeFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downloadCompleteReceiver, completeFilter);
    }

    @Override
    public void onPause(){
        super.onPause();
        unregisterReceiver(downloadCompleteReceiver);
    }

    private BroadcastReceiver downloadCompleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            if(mDownloadQueueId == reference){

                DownloadManager.Query query = new DownloadManager.Query();


                query.setFilterById(reference);

                Cursor cursor = mDownloadManager.query(query);

                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);

                int status = cursor.getInt(columnIndex);

                cursor.close();
                switch (status){
                    case DownloadManager.STATUS_SUCCESSFUL :
                        Log.e(TAG, "STATUS_SUCCESSFUL!!");
                        Toast.makeText(context, "다운로드가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        break;

                    case DownloadManager.STATUS_PAUSED :
                        Log.e(TAG, "STATUS_PAUSED!!");
                        break;

                    case DownloadManager.STATUS_FAILED :
                        Log.e(TAG, "STATUS_FAILED!!");
                        break;
                }
            }
        }
    };



    private void URLDownloading(Uri url,String uri) {

        if (mDownloadManager == null) {
            mDownloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        }

        File outputFile = new File(outputFilePath+uri);

        if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdirs();
        }

        Uri downloadUri = url;

        DownloadManager.Request request = new DownloadManager.Request(downloadUri);

        request.setTitle("다운로드 항목");

        request.setDestinationUri(Uri.fromFile(outputFile));
        request.setAllowedOverMetered(true);

        mDownloadQueueId = mDownloadManager.enqueue(request);


    }
}