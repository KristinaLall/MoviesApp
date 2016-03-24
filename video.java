package com.example.w0274203.assign4_movie;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.VideoView;

public class video extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        //Variables
        String movieToPlay = "";
        String uriPath = "";
        int videoFile;
        Bundle extras = getIntent().getExtras();

        //Get the movie name from the bundle sent from the details page
        if (extras != null)
            movieToPlay = extras.getString("MovieFileName");

        //Depending on what the user has selected, the correct video file is set.
        if (movieToPlay.equals("findingdory"))
            videoFile =  R.raw.findingdory;
        else if (movieToPlay.equals("kungfupanda3"))
            videoFile =  R.raw.kungfupanda3;
        else if (movieToPlay.equals("junglebook"))
            videoFile =  R.raw.junglebook;
        else if (movieToPlay.equals("secretlifeofpets"))
            videoFile =  R.raw.secretlifeofpets;
        else if (movieToPlay.equals("angrybirds"))
            videoFile =  R.raw.angrybirds;
        else if (movieToPlay.equals("zootopia"))
            videoFile =  R.raw.zootopia;
        else
            videoFile =  R.raw.aliceinwonderland2;

        //Sets the Uri path for the video file based on user selection
        uriPath = "android.resource://" + getPackageName() + "/" + videoFile;

        //Play the video file from the raw folder.
        VideoView videoView_playVideo = (VideoView) findViewById(R.id.videoView_playVideo);
        videoView_playVideo.setVideoPath(uriPath);
        videoView_playVideo.setMediaController(new MediaController(this));
        videoView_playVideo.start();
        videoView_playVideo.requestFocus();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_video, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
