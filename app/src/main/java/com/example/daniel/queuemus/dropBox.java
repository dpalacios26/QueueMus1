package com.example.daniel.queuemus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class dropBox extends AppCompatActivity {
    private static final String TAG = "dropBox";
    final static String DROPBOX_APP_KEY = "a1lzhhr5rh6b9kp";
    final static String DROPBOX_APP_SECRET = "30lvi9h7ey9y0nd";
    final static String ACCESSTOKEN = "gRQEFmmDYGAAAAAAAAAAC70e2JMuddsCWxCSkKELWtcUOvkfV2DN0NPCwOZ_ryK2";
    private DropboxAPI<AndroidAuthSession> mApi;
    final static int REQUEST_CODE = 1;
    private String TITLE="Choose a file";
    private ListView lv;
    public MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drop_box);
        Initialize_DropBox();
        AccessSDCard();
        NoFileSelected();


    }

    protected void AccessSDCard(){
        Button chooser = (Button) findViewById(R.id.upload);
        chooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chooseFile;
                Intent intent;
                chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
                chooseFile.setType("*/*");
                intent = Intent.createChooser(chooseFile, TITLE);
                startActivityForResult(intent, REQUEST_CODE);

            }
        });

    }
    protected void NoFileSelected(){
        Button uploadList = (Button) findViewById(R.id.UpdateList);
        uploadList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(dropBox.this,"No file selected",Toast.LENGTH_SHORT).show();
            }
        });




    }

    protected void Initialize_DropBox(){
        //store app key and seceret
        AppKeyPair appKeys = new AppKeyPair(DROPBOX_APP_KEY,DROPBOX_APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        //pass appKey and secretKey to new dropBox obj.
        mApi = new DropboxAPI<AndroidAuthSession>(session);
        //start Session
        Button drop = (Button) findViewById(R.id.drop);
        drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mApi.getSession().startOAuth2Authentication(dropBox.this);

            }
        });
    }

    //finish authentication after user returns to screen
    @Override
    protected void onResume() {
        super.onResume();

        if(mApi.getSession().authenticationSuccessful()){
            try{
                // Required to complete auth, sets the access token on the session
                mApi.getSession().finishAuthentication();
                Toast.makeText(getApplication(), "Finished Authentication", Toast.LENGTH_LONG).show();


                String accessToken = mApi.getSession().getOAuth2AccessToken();

            }catch (IllegalStateException e){
                Log.i("DbAuthLog", "Error authenticating", e);
            }
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CODE:
                //If file selection was successfull
                if(resultCode == RESULT_OK){
                    if (data != null){
                        //get uri of the selected file
                        final Uri uri = data.getData();

                        Log.i(TAG,"Uri = "+ uri.toString());
                        try{
                            //get file path from Uri
                            Toast.makeText(dropBox.this, "File Selected: "+ uri.getPath(), Toast.LENGTH_SHORT).show();
                            final TextView selectedFile = (TextView)findViewById(R.id.Uri);
                            selectedFile.setText("Selected File: "+ uri.getPath());


                            //update List

                            final  ArrayList<Uri>FileUpload = new ArrayList<Uri>();

                            final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,FileUpload);
                            lv=(ListView)findViewById(R.id.ListViewUpload);
                         //   lv.setAdapter(adapter);






                            //Upload to list button with selected file
                            Button uploadList = (Button) findViewById(R.id.UpdateList);
                            uploadList.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    adapter.add(uri.getPath());
                                    lv.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();

                                    Toast.makeText(dropBox.this, "File Added", Toast.LENGTH_SHORT).show();



                                }
                            });



/*
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                    try {
                                        mediaPlayer.start();
                                        if(mediaPlayer.isPlaying()){
                                            Toast.makeText(dropBox.this,"Playing...", Toast.LENGTH_SHORT).show();
                                        }else if(!mediaPlayer.isPlaying()){
                                            Toast.makeText(dropBox.this,"ERROR Can not play music.", Toast.LENGTH_SHORT).show();

                                        }
                                    }catch (Exception e){
                                        Log.e("MediaPlayer", "Error to Play Music");
                                    }

                                }
                            });

*/


                        }


                        catch (Exception e){
                            Log.e("FileSelectorActivity", "File select error",e);
                        }


                    }


                }
                break;
        }
        super.onActivityResult(requestCode,resultCode,data);
    }


}





