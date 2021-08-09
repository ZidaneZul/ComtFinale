package com.example.musicstream;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PlaySongActivity extends AppCompatActivity {

    private String title = "";
    private String artiste = "";
    private String fileLink = "";
    private int drawable;
    private int currentIndex = -1;

    private MediaPlayer player = new MediaPlayer();
    private Button btnPlayPause = null;
    private SongCollection songCollection = new SongCollection();
    private SongCollection originalSongCollection = new SongCollection();

    List<Song> shuffleList = Arrays.asList(songCollection.songs);

    SeekBar seekbar;
    Handler handler = new Handler();
    Runnable runnable;

    Button shuffleBtn;
    Boolean shuffleFlag = false;
    Button repeatBtn;
    Boolean repeatFlag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);

        if (player.isPlaying()) {
            player = null;
        }
        seekbar = (SeekBar) findViewById(R.id.songSeekBar);

        btnPlayPause = findViewById(R.id.btnPlayPause);
        Bundle songData = this.getIntent().getExtras();
        currentIndex = songData.getInt("index");
        Log.d("temasek", "Retrieved position is: " + currentIndex);
        displaySongBasedOnIndex(currentIndex);
        playSong(fileLink);


        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                seekbar.setMax(player.getDuration());
                updateSeekbar();
            }
        });
        player.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                double ratio = percent / 100;
            }
        });

        updateSeekbar();

        repeatBtn = findViewById(R.id.repeatBtn);
        shuffleBtn = findViewById(R.id.shuffleBtn);

    }

    Runnable p_bar = new Runnable() {
        @Override
        public void run() {
            Log.d("temasek", "running");
        }
    };

    public void displaySongBasedOnIndex(int selectedIndex) {
        Song song = songCollection.getCurrentSong(selectedIndex);
        title = song.getTitle();
        artiste = song.getArtiste();
        fileLink = song.getFileLink();
        drawable = song.getDrawable();

        TextView txtTitle = findViewById(R.id.txtSongTitle);
        txtTitle.setText(title);

        TextView textArtiste = findViewById(R.id.txtArtist);
        textArtiste.setText(artiste);

        ImageView iCoverArt = findViewById(R.id.imgCoverArt);
        iCoverArt.setImageResource(drawable);
    }

    public void playSong(String songUrl) {
        try {
            player.reset();
            player.setDataSource(songUrl);
            player.prepare();
            player.start();
            gracefullyStopsWhenMusicEnds();

            btnPlayPause.setText("PAUSE");
            setTitle(title);

            player.seekTo(0);
            System.out.println("This is the song Duration: " + player.getDuration());
            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        player.seekTo(progress);
                        seekBar.setProgress(progress);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        gracefullyStopsWhenMusicEnds();
    }

    private void gracefullyStopsWhenMusicEnds() {
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {

                if(repeatFlag){
                    playOrPauseMusic(null);
                }
                else{
                    btnPlayPause.setText("Play");
                }


                Toast.makeText(getBaseContext(), "The song had ended and the OnCompleteListener is activated \n" + "The button text is changed to 'PLAY'",
                        Toast.LENGTH_LONG).show();
                btnPlayPause.setText("PLAY");
                seekbar.setProgress(0);
            }
        });
    }

    public void playOrPauseMusic(View view) {
        if (player.isPlaying()) {
            player.pause();
            btnPlayPause.setText("PLAY");
        } else {
            player.start();
            btnPlayPause.setText("PAUSE");
        }
    }


    public void playNext(View view) {
        currentIndex = songCollection.getNextSong(currentIndex);
        Toast.makeText(this, "After clicking playNext, \nthe current index of this song\n"
                + "in the songCollection array is now :" + currentIndex, Toast.LENGTH_LONG).show();
        Log.d("temasek", "After playNext, the index is now :" + currentIndex);
        displaySongBasedOnIndex(currentIndex);
        playSong(fileLink);
    }

    public void playPrevious(View view) {
        currentIndex = songCollection.getPrevSong(currentIndex);
        Toast.makeText(this, "After clicking playPrevious, \nthe current index of this song\n"
                + "in the songCollection array is now :" + currentIndex, Toast.LENGTH_LONG).show();
        Log.d("temasek", "After playPrev, the index is now :" + currentIndex);
        displaySongBasedOnIndex(currentIndex);
        playSong(fileLink);
    }

    @Override
    public void onBackPressed() {
        if (player != null) {
            player.release();
            if (handler != null)
                handler.removeCallbacksAndMessages(null);
        }
        finish();
        super.onBackPressed();
    }

    public void updateSeekbar() {
        int currPos = player.getCurrentPosition();
        seekbar.setProgress(currPos);
        runnable = new Runnable() {

            @Override
            public void run() {
                updateSeekbar();
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    public void shuffleSong(View view) {
        if (shuffleFlag) {
            shuffleBtn.setBackgroundResource(R.drawable.shuffle_off);
        } else {
            shuffleBtn.setBackgroundResource(R.drawable.shuffle_on);
            Collections.shuffle(shuffleList);
            for (int i =0; i < shuffleList.size(); i++){
                Log.d("shuffle", shuffleList.get(i).getTitle());
            }
        }

        shuffleFlag = !shuffleFlag;
    }

    public void repeatSong(View view) {
        if (repeatFlag) {
            repeatBtn.setBackgroundResource(R.drawable.repeat_off);
        } else {
            repeatBtn.setBackgroundResource(R.drawable.repeat_on);
        }
        repeatFlag = !repeatFlag;
    }
}

