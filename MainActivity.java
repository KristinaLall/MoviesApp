package com.example.w0274203.assign4_movie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.database.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends Activity {

    //Make Objects for Controls
    ListView listView_movieList;
    ArrayList<String> titles = new ArrayList<String>();
    ArrayList<Integer> images = new ArrayList<Integer>();
    ArrayList<Integer> lgImages = new ArrayList<Integer>();
    Map<String, String> myHashMap = new HashMap<String, String>();

    DBAdapter db = new DBAdapter (MainActivity.this);
    Button button_Add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            String destPath = "/data/data/" + getPackageName() + "/database/MyDB";
            File f = new File(destPath);
            if (!f.exists()) {
                CopyDB(getBaseContext().getAssets().open("mydb"),
                        new FileOutputStream(destPath));
            }//end if
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }//end catch

        //This line gets commented out after the first time you run the program.
        //insertDataIntoDatabase();

        getMovies();
        populateListView();

        //Connect XML and Objects
        button_Add = (Button) findViewById(R.id.button_Add);

        //Create Listeners
        View.OnClickListener ocl_button_Add = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent("add_movie");
                startActivityForResult(i, 1);
                finish();
            }
        };

        //Connect Listeners to Controls
        button_Add.setOnClickListener(ocl_button_Add);

    }//end onCreate

    private void insertDataIntoDatabase() {
        //insert data into database
        db.open();
        db.insertMovie("Finding Dory", "5", "\n" + "Release Date: June 17 2016" + "\n" + "Director: Andrew Stanton" + "\n" + "Plot: With help from Nemo and Marlin (Albert Brooks), Dory (Ellen DeGeneres) the forgetful fish embarks on a quest to reunite with her mother (Diane Keaton) and father (Eugene Levy). ", "findingdory", "largefindingdory");
        db.insertMovie("Kung Fu Panda 3", "5", "\n" + "Release Date: January 29 2016"+ "\n" + "Directors: Jennifer Yuh Nelson, Alessandro Carloni" + "\n" + "Plot: After reuniting with his long-lost father (Bryan Cranston), Po (Jack Black) must train a village of clumsy pandas to help him defeat the villainous Kai (J.K. Simmons). ", "kungfupanda3", "largekungfupanda3");
        db.insertMovie("Jungle Book", "5", "\n" + "Release Date: April 15 2016" + "\n" + "Director: John Favreau" + "\n" + "Plot: After a fearsome tiger threatens his life, Mowgli (Neel Sethi), a boy raised by wolves, leaves his jungle home and, guided by a stern panther (Ben Kingsley) and a free-spirited bear (Bill Murray), sets out on a journey of self-discovery. ", "junglebook","largejunglebook");
        db.insertMovie("The Secret Life of Pets", "5",  "\n" + "Release Date: July 8 2016" + "\n" + "Directors: Chris Renaud, Yarrow Cheney" + "\n" + "Plot: Taking place in a Manhattan apartment building, Max's life as a favorite pet is turned upside down when his owner Katie brings home a sloppy mongrel named Duke, who was rescued from the pound.", "secretlifeofpets", "largesecretlifeofpets");
        db.insertMovie("Angry Birds", "4", "\n" + "Release Date: May 12 2016" + "\n" + "Directors: Clay Kaytis, Fergal Reilly" + "\n" + "Plot: Three birds (Jason Sudeikis, Josh Gad, Danny McBride) investigate the mysterious arrival of pigs to their island paradise.", "angrybirds", "largeangrybirds");
        db.close();
    }//end insertDataIntoDatabase

    //Get all the movies from the database
        public void getMovies() {
        db.open();
        Cursor c = db.getAllMovies();
        if(c.moveToFirst())
        {
            do{
                titles.add(c.getString(1));
                String image = c.getString(4);
                String largeImage = c.getString(5);
                int moviePicId = getResources().getIdentifier(image, "drawable", "com.example.w0274203.assign4_movie");
                int largemoviePicId = getResources().getIdentifier(largeImage, "drawable", "com.example.w0274203.assign4_movie");
                images.add(moviePicId);
                lgImages.add(largemoviePicId);
                myHashMap.put(c.getString(1), c.getString(0));
            }while(c.moveToNext());
        }//end if
    }//end getMovies

    public void populateListView()
    {
        CustomList adapter = new CustomList(MainActivity.this, titles, images);
        listView_movieList = (ListView) findViewById(R.id.listView_movieList); //Connect XML and Objects
        listView_movieList.setAdapter(adapter);

        listView_movieList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String titleList = (String) listView_movieList.getItemAtPosition(position);
                long movieListId = Long.parseLong(myHashMap.get(titleList));

                db.open();
                Cursor c = db.getMovie(movieListId);

                //Storing the database items into variables
                if (c.moveToFirst()) {
                    String rating = c.getString(2);
                    String description = c.getString(3);
                    String imageThumbnail = c.getString(4);
                    String largeImageThumbnail = c.getString(5);

                    db.close();

                    //Go to the details page
                    Intent i = new Intent("details");
                    Bundle extras = new Bundle();

                    //Sending Data to the details page.
                    extras.putLong("Id", movieListId);
                    extras.putString("Title", titleList);
                    extras.putString("Rating", rating);
                    extras.putString("Description", description);
                    extras.putString("ThumbnailPic", imageThumbnail);
                    extras.putString("LargeThumbnailPic", largeImageThumbnail);

                    i.putExtras(extras);
                    startActivityForResult(i, 1);
                }//end if
            }
        });//end listener
    }//end populateListView

    public void CopyDB(InputStream inputStream,OutputStream outputStream)
            throws IOException{
        //copy 1k bytes at a time
        byte[] buffer = new byte[1024];
        int length;
        while((length = inputStream.read(buffer)) > 0)
        {
            outputStream.write(buffer,0,length);
        }
        inputStream.close();
        outputStream.close();

    }//end method CopyDB

}//end MainActivity
