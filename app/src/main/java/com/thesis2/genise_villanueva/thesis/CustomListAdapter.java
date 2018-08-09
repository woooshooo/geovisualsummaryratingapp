package com.thesis2.genise_villanueva.thesis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CustomListAdapter extends BaseAdapter {
    private Context context; //context
    private ArrayList<Reviews> reviews; //data source of the list adapter

    //public constructor
    public CustomListAdapter(Context context, ArrayList<Reviews> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @Override
    public int getCount() {
        return reviews.size(); //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return reviews.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.layout_reviewlist, parent, false);
        }

        // get current item to be displayed
        Reviews currentItem = (Reviews) getItem(position);

        // get the TextView for item name and item description
        TextView tvUser = convertView.findViewById(R.id.tvUser);
        TextView tvReview = (TextView) convertView.findViewById(R.id.tvReview);

        //sets the text for item name and item description from the current item object
        tvUser.setText(currentItem.getUser());
        tvReview.setText(currentItem.getReview());

        // returns the view for the current row
        return convertView;
    }
}