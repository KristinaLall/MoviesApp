package com.example.w0274203.assign4_movie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;


import java.lang.reflect.Field;
import java.util.ArrayList;

public class addMovie extends AppCompatActivity {

    //Make Objects for Controls
    Spinner spinner_NewMovies;
    EditText editText_EditDescription;
    EditText editText_EditName;
    Button button_ConfirmAdd;
    DBAdapter db;
    ArrayList<String> newMovies = new ArrayList<String>();
    String movie;
    String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);

        //Connect XML and Objects
        spinner_NewMovies = (Spinner)findViewById(R.id.spinner_NewMovies);
        editText_EditDescription = (EditText) findViewById(R.id.editText_EditDescription);
        editText_EditName = (EditText) findViewById(R.id.editText_EditName);
        button_ConfirmAdd = (Button) findViewById(R.id.button_ConfirmAdd);

        db = new DBAdapter(this);

        //Try the method that reads the files in the Raw folder.
        try {
            listRaw();
        } catch (IllegalAccessException e)
        {
            Toast.makeText(getBaseContext(), "File Chooser", Toast.LENGTH_SHORT).show();
        }//end catch

        //Methods for Spinner and Add Button
        populateSpinner();
        buttonAddMovie();
    }//end onCreate


    //reference: http://stackoverflow.com/questions/6539715/android-how-do-can-i-get-a-list-of-all-files-in-a-folder
    //Loops through the raw folder and adds all the movies to an arrayList.
    public void listRaw() throws IllegalAccessException {
        Field[] fields=R.raw.class.getFields();
        for(int count=0; count < fields.length; count++){
            int resourceID=fields[count].getInt(fields[count]);
            newMovies.add(fields[count].getName());
        }//end for
    }//end listRaw

    //Add all videos to the spinner object.
    public void populateSpinner(){
        //load the array adapter for the image/trailer items from resources array list
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,newMovies);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_NewMovies.setAdapter(adapter);

        //listener for spinner
        spinner_NewMovies.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {

                movie = spinner_NewMovies.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });//end listener

    }//end createDropDowns

    //Function for Add movie
    public void buttonAddMovie()
    {
        View.OnClickListener ocl_buttonConfirmAdd = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Validation for user input on movie name.
                String nameEntered = editText_EditName.getText().toString();
                if (isValidString(nameEntered))
                    Toast.makeText(getBaseContext(),"Please enter a movie name!",Toast.LENGTH_SHORT).show();
                else
                {
                    //Adds the new movie to the database based on user selection and input.
                    db.open();
                    db.insertMovie(editText_EditName.getText().toString(), "5", editText_EditDescription.getText().toString(), movie, "large" + movie);
                    db.close();

                    //Go back to Main Activity page to display the added movies.
                    Intent i = new Intent("main");
                    startActivityForResult(i, 1);
                    finish();

                }//end else
            }
        };

        //Connect Listener to Controls
        button_ConfirmAdd.setOnClickListener(ocl_buttonConfirmAdd);

    }//end buttonAddMovie

    //Validation for user input
    public boolean isValidString(String nameEntered)
    {
        return (nameEntered.trim().equals(""));
    }//end validString

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_movie, menu);
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
