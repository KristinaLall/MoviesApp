package com.example.w0274203.assign4_movie;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


//Class for custom list view with thumbnail and movie name.
public class CustomList extends ArrayAdapter<String>{

    //Variables
    private final Activity context;
    private final ArrayList<String> web;
    private final ArrayList<Integer> imageId;

    //Methods for Adding the Listview with a Thumbnail
    public CustomList(Activity context, ArrayList<String> web, ArrayList<Integer> imageId) {
        super(context, R.layout.activity_custom_list, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.activity_custom_list, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.textView_ListTitle);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView_thumbnail);
        txtTitle.setText(web.get(position));
        imageView.setImageResource(imageId.get(position)); //Position is for
        return rowView;
    }
}//end class CustomList
