package com.thesis2.genise_villanueva.thesis;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mapbox.mapboxsdk.annotations.Marker;
import com.vatsal.imagezoomer.ZoomAnimation;


public class ImagesController {
    private static final String TAG = "ImagesController";
    private Context mContext;
    private int abreeza[] = new int[]{R.drawable.abreeza_0,R.drawable.abreeza_1};
    private int aldevinco[] = new int[]{R.drawable.aldevinco_0,R.drawable.aldevinco_1};
    private int bankerohan[] = new int[]{R.drawable.bangkerohan_0,R.drawable.bangkerohan_1};
    private int bemwa[] = new int[]{R.drawable.bemwa_0,R.drawable.bemwa_1};
    private int bluejaz[] = new int[]{R.drawable.bluejaz_0,R.drawable.bluejaz_1};
    private int butterfly[] = new int[]{R.drawable.butterfly_0,R.drawable.butterfly_1};
    private int christina[] = new int[]{R.drawable.christina_0,R.drawable.christina_1};
    private int coralgarden[] = new int[]{R.drawable.coralgarden_0,R.drawable.coralgarden_1};
    private int crocodilepark[] = new int[]{R.drawable.crocodilepark_0,R.drawable.crocodilepark_1};

    String title;
    TextView tvTitle;
    private Activity activity;

    ImagesController(Context context) {
        this.mContext = context;
        this.activity = (Activity) context;
    }

    public void renderImages(Marker marker, View view, TextView theTitle){
        title = marker.getTitle();
        tvTitle = theTitle;
        ImageButton imageButton1 = view.findViewById(R.id.imageButton1);
        ImageButton imageButton2 = view.findViewById(R.id.imageButton2);
        long duration = 500;

        imageButton1.setOnHoverListener((view1, motionEvent) -> {
            ZoomAnimation zoomAnimation = new ZoomAnimation(activity);
            zoomAnimation.zoom(view1, duration);
            return false;
        });

        imageButton2.setOnHoverListener((view12, motionEvent) -> {
            ZoomAnimation zoomAnimation = new ZoomAnimation(activity);
            zoomAnimation.zoom(view12, duration);
            return false;
        });

        if (title.contains("Abreeza")){
            imageButton1.setImageResource(abreeza[0]);
            imageButton2.setImageResource(abreeza[1]);
        }
        if (title.contains("Aldevinco")){
            imageButton1.setImageResource(aldevinco[0]);
            imageButton2.setImageResource(aldevinco[1]);
        }
        if (title.contains("Bankerohan")){
            imageButton1.setImageResource(bankerohan[0]);
            imageButton2.setImageResource(bankerohan[1]);
        }
        if (title.contains("BEMWA")){
            imageButton1.setImageResource(bemwa[0]);
            imageButton2.setImageResource(bemwa[1]);
        }
        if (title.contains("Bluejaz")){
            imageButton1.setImageResource(bluejaz[0]);
            imageButton2.setImageResource(bluejaz[1]);
        }
        if (title.contains("Butterfly")){
            imageButton1.setImageResource(butterfly[0]);
            imageButton2.setImageResource(butterfly[1]);
        }
        if (title.contains("Christina")){
            imageButton1.setImageResource(christina[0]);
            imageButton2.setImageResource(christina[1]);
        }
        if (title.contains("Coral")){
            imageButton1.setImageResource(coralgarden[0]);
            imageButton2.setImageResource(coralgarden[1]);
        }
        if (title.contains("Crocodile")){
            imageButton1.setImageResource(crocodilepark[0]);
            imageButton2.setImageResource(crocodilepark[1]);
        }

    }
}

