package com.example.unit2_projectweek;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    Button pauseButton, skipButton, previousButton;
    static MediaPlayer mediaPlayer;
    TextView songTextLabel;
    SeekBar songSeekbar;
    int position;
    String sname;

    ArrayList<File> mySongs;
    Thread updateseekBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        pauseButton = (Button) findViewById(R.id.pause_button);
        skipButton = (Button) findViewById(R.id.next_button);
        previousButton = (Button) findViewById(R.id.previous_button);

        songTextLabel = (TextView) findViewById(R.id.songNameTextView);
        songSeekbar = (SeekBar) findViewById(R.id.seekBar);

        getSupportActionBar().setTitle("Now Playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        updateseekBar = new Thread(){


            @Override
            public void run() {
                super.run();

                int totalDuration = mediaPlayer.getDuration();
                int currentPosition = 0;

                while (currentPosition<totalDuration){
                    try{
                        sleep(500);
                        currentPosition = mediaPlayer.getCurrentPosition();
                        songSeekbar.setProgress(currentPosition);

                    }
                    catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }

            }
        };

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        mySongs=(ArrayList) bundle.getParcelable("songs");

        sname = mySongs.get(position).getName().toString();

        final String songName = i.getStringExtra("songname");

        songTextLabel.setText(songName);
        songTextLabel.setSelected(true);


        position = bundle.getInt("pos", 0);

        Uri u = Uri.parse((mySongs.get(position).toString()));

        mediaPlayer = MediaPlayer.create(getApplicationContext(),u);
        songSeekbar.setMax(mediaPlayer.getDuration());

        updateseekBar.start();

        songSeekbar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        songSeekbar.getThumb().setColorFilter(getResources().getColor(R.color.colorAccent),PorterDuff.Mode.SRC_IN);

        songSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                songSeekbar.setMax(mediaPlayer.getDuration());

                if(mediaPlayer.isPlaying()){
                    pauseButton.setBackgroundResource(R.drawable.ic_play_circle_filled_black_24dp);
                    mediaPlayer.pause();
                }
                else
                {
                    pauseButton.setBackgroundResource(R.drawable.ic_pause_circle_filled_black_24dp);
                    mediaPlayer.start();
                }
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaPlayer.stop();
                mediaPlayer.release();
                position = ((position+1)%mySongs.size());

                Uri u = Uri.parse(mySongs.get(position).toString());

                mediaPlayer = MediaPlayer.create(getApplicationContext(),u);

                sname = mySongs.get(position).getName().toString();
                songTextLabel.setText(sname);

                mediaPlayer.start();
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mediaPlayer.stop();
                mediaPlayer.release();

                position = ((position-1)<0)?(mySongs.size()):(position-1);
                Uri u = Uri.parse(mySongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),u);
                songTextLabel.setText(sname);

                mediaPlayer.start();
            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
