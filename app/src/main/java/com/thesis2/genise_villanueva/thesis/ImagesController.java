package com.thesis2.genise_villanueva.thesis;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mapbox.mapboxsdk.annotations.Marker;

public class ImagesController {

    private Context mContext;
    private int images[] = new int[]{R.drawable.abreeza_0,R.drawable.abreeza_1};
    String title;
    TextView tvTitle;
    ImageView imageView1;
    ImageView imageView2;
    ImagesController(Context context) {
        this.mContext = context;
    }
    public void renderImages(Marker marker, View view, TextView theTitle){
        title = marker.getTitle();
        tvTitle = theTitle;
        imageView1 = view.findViewById(R.id.imageView1);
        imageView2 = view.findViewById(R.id.imageView2);


        if (title.contentEquals(tvTitle.getText())){
            imageView1.setImageResource(images[0]);
            imageView2.setImageResource(images[1]);
        }

    }
}

