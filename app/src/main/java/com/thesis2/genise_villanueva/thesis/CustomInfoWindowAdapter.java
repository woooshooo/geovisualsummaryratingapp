package com.thesis2.genise_villanueva.thesis;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.maps.MapboxMap;

public class CustomInfoWindowAdapter implements MapboxMap.InfoWindowAdapter {

    private static final String TAG = "CustomInfoWindowAdapter";
    private final View mWindow;
    private Context mContext;
    private MainActivity mainActivity;

    CustomInfoWindowAdapter(Context context) {
        this.mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);
        this.mContext = context;
    }

    private void renderWindowText(Marker marker, View view){

        String title = marker.getTitle();
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        ImageView imageView1 = view.findViewById(R.id.imageView1);
        ImageView imageView2 = view.findViewById(R.id.imageView2);
        Button btn_review = view.findViewById(R.id.btn_review);
        Button btn_info = view.findViewById(R.id.btn_info);

        btn_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Showing reviews of "+ marker.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Showing info of "+ marker.getTitle(), Toast.LENGTH_SHORT).show();
                marker.hideInfoWindow();

                // 1. Instantiate an AlertDialog.Builder with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setTitle(marker.getTitle())
                        .setView(View.inflate(mContext, R.layout.layout_data, null));
                // 3. Get the AlertDialog from create()
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    });


        if(!title.equals("")){
            tvTitle.setText(title);
        }

//        String snippet = marker.getSnippet();
//        TextView tvSnippet = view.findViewById(R.id.tvSnippet);
//
//        if(!snippet.equals("")){
//            tvSnippet.setText(snippet);
//        }

    }

    @Override
    public View getInfoWindow(Marker marker) {
        renderWindowText(marker, mWindow);
        return mWindow;
    }



}

