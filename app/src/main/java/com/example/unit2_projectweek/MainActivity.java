package com.example.unit2_projectweek;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button playButton, pauseButton, stopButton;
    MediaPlayer mediaPlayer;
    int pausePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playButton = (Button) findViewById(R.id.play_button);
        pauseButton = (Button) findViewById(R.id.pause_button);
        stopButton = (Button) findViewById(R.id.stop_button);

        playButton.setOnClickListener(this);
        pauseButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_button:

                if (mediaPlayer == null) {
                    mediaPlayer = mediaPlayer.create(getApplicationContext(), R.raw.im_yours);
                    mediaPlayer.start();
                } else if(!mediaPlayer.isPlaying()) {
                    mediaPlayer.seekTo(pausePosition);
                    mediaPlayer.start();

                }
                break;

            case R.id.pause_button:
                if(mediaPlayer!=null) {
                    mediaPlayer.pause();
                    pausePosition=mediaPlayer.getCurrentPosition();

                }
                break;

            case R.id.stop_button:
                if (mediaPlayer == null) {
                    mediaPlayer.stop();
                    mediaPlayer=null;
                }
                break;
        }
    }
}
