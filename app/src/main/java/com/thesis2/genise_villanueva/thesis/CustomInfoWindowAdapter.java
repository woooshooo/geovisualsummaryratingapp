package com.thesis2.genise_villanueva.thesis;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class CustomInfoWindowAdapter implements MapboxMap.InfoWindowAdapter {

    private static final String TAG = "CustomInfoWindowAdapter";
    private final View mWindow;
    private Context mContext;
    String title;
    private MainActivity mainActivity;
    private int images[] = new int[]{R.drawable.abreeza_0,R.drawable.abreeza_1};

    CustomInfoWindowAdapter(Context context) {
        this.mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);
        this.mContext = context;
    }

    private void renderWindowText(Marker marker, View view){

        title = marker.getTitle();
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        ImageView imageView1 = view.findViewById(R.id.imageView1);
        ImageView imageView2 = view.findViewById(R.id.imageView2);
        Button btn_review = view.findViewById(R.id.btn_review);
        Button btn_info = view.findViewById(R.id.btn_info);

        imageView1.setImageResource(images[0]);
        imageView2.setImageResource(images[1]);

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
                showDialogBox();
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

    private void showDialogBox(){
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();

        // Set Custom Title
        TextView customTitle = new TextView(mContext);
        // Title Properties
        customTitle.setText(title);
        customTitle.setPadding(10, 10, 10, 10);   // Set Position
        customTitle.setGravity(Gravity.CENTER);
        customTitle.setTextColor(Color.WHITE);
        customTitle.setTextSize(20);
        alertDialog.setCustomTitle(customTitle);

        // Set Message
        TextView msg = new TextView(mContext);
        // Message Properties
        msg.setText("I am a Custom Dialog Box. \n Please Customize me.");
        msg.setGravity(Gravity.CENTER_HORIZONTAL);
        msg.setTextColor(Color.WHITE);
        alertDialog.setView(msg);

        // Set Button
        // you can more buttons
//        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"OK", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                // Perform Action on Button
//            }
//        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"BACK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Perform Action on Button
            }
        });

        new Dialog(getApplicationContext());
        alertDialog.show();

        // Set Properties for OK Button
//        final Button okBT = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
//        LinearLayout.LayoutParams neutralBtnLP = (LinearLayout.LayoutParams) okBT.getLayoutParams();
//        neutralBtnLP.gravity = Gravity.FILL_HORIZONTAL;
//        okBT.setPadding(50, 10, 10, 10);   // Set Position
//        okBT.setTextColor(Color.WHITE);
//        okBT.setLayoutParams(neutralBtnLP);

        final Button cancelBT = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams negBtnLP = (LinearLayout.LayoutParams) cancelBT.getLayoutParams();
        negBtnLP.gravity = Gravity.FILL_HORIZONTAL;
        cancelBT.setTextColor(Color.WHITE);
        cancelBT.setLayoutParams(negBtnLP);

    }




}

