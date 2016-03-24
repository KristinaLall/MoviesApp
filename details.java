package com.example.w0274203.assign4_movie;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RatingBar.OnRatingBarChangeListener;

import java.math.BigDecimal;

public class details extends Activity {

    //Make Objects for Controls
    private TextView textView_Title;
    private ImageView imageView_LargePicture;
    private TextView textView_Description;
    private RatingBar ratingBar_MovieRating;
    private Button button_delete;
    private Button button_PlayTrailer;

    //Variables
    String title = "";
    String description = "";
    float rating = 0;
    long id;
    String image = "";
    DBAdapter db;
    String movieName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //Connect XML and Objects
        textView_Title = (TextView) findViewById(R.id.textView_Title);
        imageView_LargePicture = (ImageView) findViewById(R.id.imageView_LargePicture);
        textView_Description = (TextView) findViewById(R.id.textView_Description);
        ratingBar_MovieRating = (RatingBar) findViewById(R.id.ratingBar_MovieRating);
        button_delete = (Button) findViewById(R.id.button_Delete);
        button_PlayTrailer = (Button) findViewById(R.id.button_PlayTrailer);

        db = new DBAdapter(this);

        Bundle extras = getIntent().getExtras();

        //Retrieving items from the bundle and storing them in variables
        if(extras != null) {
            id = extras.getLong("Id");
            title = extras.getString("Title");
            description = extras.getString("Description");
            image = extras.getString("LargeThumbnailPic");
            if (extras.getString("Rating").equals("0.0")) //to handle if user updates the rating to 0 stars
                rating = 0;
            else
                rating = Integer.parseInt(extras.getString("Rating"));

            movieName = extras.getString("ThumbnailPic");

            textView_Title.setText(title);
            textView_Description.setText(description);
            int imageIds = getResources().getIdentifier(image, "drawable", "com.example.w0274203.assign4_movie");
            imageView_LargePicture.setImageResource(imageIds);
            ratingBar_MovieRating.setRating(rating);
        }

        //Create Listener for Play Button
            View.OnClickListener ocl_button_PlayTrailer = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent("video");
                    Bundle extras = new Bundle();
                    extras.putString("MovieFileName", movieName);
                    i.putExtras(extras);
                    startActivityForResult(i, 1);
                }
            };//end listener

        //Create Listener for Delete Button
        View.OnClickListener ocl_button_Delete = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.open();
                db.deleteMovie(id);
                Toast.makeText(getBaseContext(), "Movie Deleted!", Toast.LENGTH_SHORT).show();
                db.close();
                Intent i = new Intent("main");
                startActivityForResult(i, 1);
                finish();
            }
        };//end listener


        //Updating the Rating for the movie
            ratingBar_MovieRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    float updatedRating = ratingBar_MovieRating.getRating();
                    String ratingString = new BigDecimal(Double.toString(updatedRating)).stripTrailingZeros().toPlainString();
                    db.open();
                    if (db.updateMovieRating(id, ratingString)) { //Update rating in the database
                        Toast.makeText(getBaseContext(), "Movie Rating Updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getBaseContext(), "Error, Rating not Updated", Toast.LENGTH_SHORT).show();
                    }//end else
                    db.close();
                }
            });

        //Connect Listeners to Controls
        button_PlayTrailer.setOnClickListener(ocl_button_PlayTrailer);
        button_delete.setOnClickListener(ocl_button_Delete);

    }//end onCreate


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
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
