package com.example.daniel.queuemus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Music_List extends AppCompatActivity {
    ListView lV;

    List<String> list;
    ListAdapter adapter;
    MediaPlayer mediaPlayer;
    Handler seekHandler = new Handler();
    Runnable runnable;



    private  boolean isShuffle = false;
    private boolean isRepeat = false;

    private int currentSongIndex = 0;
    private int seekForwardTime = 100000;
    private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music__list);



        final ImageButton repeat = (ImageButton)findViewById(R.id.repeat);
        final ImageButton shuffle = (ImageButton)findViewById(R.id.shuffle);
        ImageButton skip = (ImageButton)findViewById(R.id.skip);
        ImageButton back = (ImageButton)findViewById(R.id.back);
        ImageButton pause = (ImageButton) findViewById(R.id.pause);
        ImageButton play = (ImageButton)findViewById(R.id.playbutton);
        final SeekBar seekBar = (SeekBar)findViewById(R.id.songProg);



        lV = (ListView)findViewById(R.id.listView);
        list = new ArrayList<>();
        Field[] fields = R.raw.class.getFields();




        //get files from raw
        for(int i=0; i < fields.length; i++) {
            list.add(fields[i].getName());
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,list);
        lV.setAdapter(adapter);

        lV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(mediaPlayer != null){
                    mediaPlayer.release();
                }
                int realID = getResources().getIdentifier(list.get(i), "raw", getPackageName());
                mediaPlayer = MediaPlayer.create(Music_List.this, realID);
                mediaPlayer.start();
//                seekBar.setMax(mediaPlayer.getDuration());






            }
        });
























        //shuffle

        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShuffle){
                    isShuffle=false;
                    Toast.makeText(getApplicationContext(), "Shuffle is OFF", Toast.LENGTH_SHORT).show();
                    shuffle.setImageResource(R.drawable.ic_shuffle_black_24dp);
                }else{
                    //make repeat to true
                    isShuffle=true;
                    Toast.makeText(getApplicationContext(), "Shuffle is ON", Toast.LENGTH_SHORT).show();
                    //make shuffle to false
                    isRepeat = false;
                    shuffle.setImageResource(R.drawable.ic_shuffle_black_24dp);
                    repeat.setImageResource(R.drawable.ic_repeat_black_24dp);
                }

            }
        });

        //repeat
        repeat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(isRepeat){
                    isRepeat = false;
                    Toast.makeText(getApplicationContext(), "Repeat is OFF", Toast.LENGTH_SHORT).show();
                    repeat.setImageResource(R.drawable.ic_repeat_black_24dp);
                }else{
                    // make repeat to true
                    isRepeat = true;
                    Toast.makeText(getApplicationContext(), "Repeat is ON", Toast.LENGTH_SHORT).show();
                    // make shuffle to false
                    isShuffle = false;
                    repeat.setImageResource(R.drawable.ic_repeat_black_24dp);
                    shuffle.setImageResource(R.drawable.ic_shuffle_black_24dp);
                }
            }
        });






        //forward

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int curPos = mediaPlayer.getCurrentPosition();

                if (curPos + seekForwardTime <= mediaPlayer.getDuration()){
                    mediaPlayer.seekTo(curPos +seekForwardTime);

                }else{
                    mediaPlayer.seekTo(mediaPlayer.getDuration());
                }

            }
        });


        //backwards

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // get current song position
                int currentPosition = mediaPlayer.getCurrentPosition();
                // check if seekBackward time is greater than 0 sec
                int seekBackwardTime= 100000;
                if(currentPosition - seekBackwardTime >= 0){
                    // forward song
                    mediaPlayer.seekTo(currentPosition - seekBackwardTime);
                }else{
                    // backward to starting position
                    mediaPlayer.seekTo(0);
                }

            }
        });


        //  pause/play Button

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.pause();

            }
        });
        //play
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.start();
            }
        });




















    }








}

