package com.thesis2.genise_villanueva.thesis;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class CustomInfoWindowAdapter implements MapboxMap.InfoWindowAdapter {

    private static final String TAG = "CustomInfoWindowAdapter";
    private final View mWindow;
    private Context mContext;
    private MainActivity mainActivity;
    private AssetsController assetsController;
    String title;
    private JSONArray jsonArrayData;
    private JSONObject jsonObjectData;
    private int dpAsPixels;
    private Marker globalMarker;
//    private int images[] = new int[]{R.drawable.abreeza_0,R.drawable.abreeza_1};

    CustomInfoWindowAdapter(Context context) {
        this.mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);
        this.mContext = context;
        assetsController = new AssetsController(context);
        float scale = mContext.getResources().getDisplayMetrics().density;
        dpAsPixels = (int) (10* scale + 0.5f);
    }

    private void renderWindowText(Marker marker, View view) {
        globalMarker = marker;
        ImagesController imagesController = new ImagesController(mContext);
        title = marker.getTitle();
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        if (!title.equals("")) {
            tvTitle.setText(title);
            imagesController.renderImages(marker, view, tvTitle);
        }
        Button btn_review = view.findViewById(R.id.btn_review);
        Button btn_info = view.findViewById(R.id.btn_info);

        ArrayList<Reviews> reviewsArrayList = new ArrayList<>();
        reviewsArrayList.add(new Reviews("id","user","location","Abreeza Mall is a convenient place to shop and eat when staying at Seda Hotel in JP Laurel, Davao City. It is right across the hotel. There are a number of restaurants to choose from for lunch and dinner- fastfoods, regular dine-in e.g, Japanese (Sumosan), Shakey’s, Jollibee, Yellow Cab, For snacks and coffee, you can go to Starbuck’s , J.Company , Dunkin Donuts or one of the food stalls/carts like Fruits in Ice Cream, etc. Most of the shops are those found in Manila. Foreign brands are present - Mango, Esprit, Gap, Giordano, Charles & Keith, Hush Puppies, H&M, etc. A few interesting local brands are available like Chimes, Cocoon (nice formal wear at reasonable prices), Just G (for tweens and teens), Mendez. There are gadget shops on the upper floor. There is also a Robinson’s Dept Store (poor rating for me) and a Robinson’s Supermarket (good rating for me). If you just want to stroll around, mall is ok but if you want to shop, try other malls."));
        reviewsArrayList.add(new Reviews("id2","user1","location","A great mall with all kinds of products westerners are used to seeing. Very good choice of restaurants and the prices are excellent too."));
        reviewsArrayList.add(new Reviews("id","user","location","Abreeza Mall is a convenient place to shop and eat when staying at Seda Hotel in JP Laurel, Davao City. It is right across the hotel. There are a number of restaurants to choose from for lunch and dinner- fastfoods, regular dine-in e.g, Japanese (Sumosan), Shakey’s, Jollibee, Yellow Cab, For snacks and coffee, you can go to Starbuck’s , J.Company , Dunkin Donuts or one of the food stalls/carts like Fruits in Ice Cream, etc. Most of the shops are those found in Manila. Foreign brands are present - Mango, Esprit, Gap, Giordano, Charles & Keith, Hush Puppies, H&M, etc. A few interesting local brands are available like Chimes, Cocoon (nice formal wear at reasonable prices), Just G (for tweens and teens), Mendez. There are gadget shops on the upper floor. There is also a Robinson’s Dept Store (poor rating for me) and a Robinson’s Supermarket (good rating for me). If you just want to stroll around, mall is ok but if you want to shop, try other malls."));
        reviewsArrayList.add(new Reviews("id2","user1","location","A great mall with all kinds of products westerners are used to seeing. Very good choice of restaurants and the prices are excellent too."));
        reviewsArrayList.add(new Reviews("id","user","location","Abreeza Mall is a convenient place to shop and eat when staying at Seda Hotel in JP Laurel, Davao City. It is right across the hotel. There are a number of restaurants to choose from for lunch and dinner- fastfoods, regular dine-in e.g, Japanese (Sumosan), Shakey’s, Jollibee, Yellow Cab, For snacks and coffee, you can go to Starbuck’s , J.Company , Dunkin Donuts or one of the food stalls/carts like Fruits in Ice Cream, etc. Most of the shops are those found in Manila. Foreign brands are present - Mango, Esprit, Gap, Giordano, Charles & Keith, Hush Puppies, H&M, etc. A few interesting local brands are available like Chimes, Cocoon (nice formal wear at reasonable prices), Just G (for tweens and teens), Mendez. There are gadget shops on the upper floor. There is also a Robinson’s Dept Store (poor rating for me) and a Robinson’s Supermarket (good rating for me). If you just want to stroll around, mall is ok but if you want to shop, try other malls."));
        reviewsArrayList.add(new Reviews("id2","user1","location","A great mall with all kinds of products westerners are used to seeing. Very good choice of restaurants and the prices are excellent too."));
        reviewsArrayList.add(new Reviews("id","user","location","Abreeza Mall is a convenient place to shop and eat when staying at Seda Hotel in JP Laurel, Davao City. It is right across the hotel. There are a number of restaurants to choose from for lunch and dinner- fastfoods, regular dine-in e.g, Japanese (Sumosan), Shakey’s, Jollibee, Yellow Cab, For snacks and coffee, you can go to Starbuck’s , J.Company , Dunkin Donuts or one of the food stalls/carts like Fruits in Ice Cream, etc. Most of the shops are those found in Manila. Foreign brands are present - Mango, Esprit, Gap, Giordano, Charles & Keith, Hush Puppies, H&M, etc. A few interesting local brands are available like Chimes, Cocoon (nice formal wear at reasonable prices), Just G (for tweens and teens), Mendez. There are gadget shops on the upper floor. There is also a Robinson’s Dept Store (poor rating for me) and a Robinson’s Supermarket (good rating for me). If you just want to stroll around, mall is ok but if you want to shop, try other malls."));
        reviewsArrayList.add(new Reviews("id2","user1","location","A great mall with all kinds of products westerners are used to seeing. Very good choice of restaurants and the prices are excellent too."));
        reviewsArrayList.add(new Reviews("id","user","location","Abreeza Mall is a convenient place to shop and eat when staying at Seda Hotel in JP Laurel, Davao City. It is right across the hotel. There are a number of restaurants to choose from for lunch and dinner- fastfoods, regular dine-in e.g, Japanese (Sumosan), Shakey’s, Jollibee, Yellow Cab, For snacks and coffee, you can go to Starbuck’s , J.Company , Dunkin Donuts or one of the food stalls/carts like Fruits in Ice Cream, etc. Most of the shops are those found in Manila. Foreign brands are present - Mango, Esprit, Gap, Giordano, Charles & Keith, Hush Puppies, H&M, etc. A few interesting local brands are available like Chimes, Cocoon (nice formal wear at reasonable prices), Just G (for tweens and teens), Mendez. There are gadget shops on the upper floor. There is also a Robinson’s Dept Store (poor rating for me) and a Robinson’s Supermarket (good rating for me). If you just want to stroll around, mall is ok but if you want to shop, try other malls."));
        reviewsArrayList.add(new Reviews("id2","user1","location","A great mall with all kinds of products westerners are used to seeing. Very good choice of restaurants and the prices are excellent too."));
        CustomListAdapter adapter = new CustomListAdapter(mContext, reviewsArrayList);

        btn_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Showing reviews of " + marker.getTitle(), Toast.LENGTH_SHORT).show();
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle(title);
                dialog.setCancelable(false);
                dialog.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Reviews user = (Reviews) adapter.getItem(i);
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(mContext);
                        builderInner.setMessage(user.getReview() + "\n \n Reviewed by: "+ user.getUser());
                        builderInner.setTitle(title);
                        builderInner.setPositiveButton("Back", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogforexit,int which) {
                                dialogforexit.dismiss();
                                dialog.show();
                            }
                        });
                        builderInner.show();
                    }
                });
                dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Showing info of " + marker.getTitle(), Toast.LENGTH_SHORT).show();
                marker.hideInfoWindow();
                showDialogBox();
            }
        });

    }

    @Override
    public View getInfoWindow(Marker marker) {
        renderWindowText(marker, mWindow);
        return mWindow;
    }

    private void showDialogBox() {
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
        //msg.setText("I am a Custom Dialog Box. \n Please Customize me.");
        msg.setGravity(Gravity.LEFT);
        msg.setTextColor(Color.WHITE);
        msg.setLinksClickable(true);
        msg.setLinkTextColor(Color.YELLOW);
        msg.setAutoLinkMask(Linkify.ALL);
        msg.setPadding(dpAsPixels,dpAsPixels,dpAsPixels,dpAsPixels);

        try {
            jsonArrayData = new JSONArray(assetsController.loadDataFromAsset());
            jsonObjectData = new JSONObject();
            for (int i = 0; i < jsonArrayData.length(); i++) {
                jsonObjectData = jsonArrayData.getJSONObject(i);
                String location = jsonObjectData.getString("Name");
                if (title.contentEquals(location)){
                    String address = jsonObjectData.getString("Address");
                    String website = jsonObjectData.getString("Website");
                    String contact = jsonObjectData.getString("Contact");
                    String rating = jsonObjectData.getString("Rating");
                    msg.setText("Address: "+ address+ " \nWebsite: "+website+"\nContact: "+contact+"\nRating: "+rating);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        msg.setMovementMethod(LinkMovementMethod.getInstance());
        alertDialog.setView(msg);

        // Set Button
        // you can more buttons
//        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"OK", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                // Perform Action on Button
//            }
//        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "EXIT", new DialogInterface.OnClickListener() {
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

