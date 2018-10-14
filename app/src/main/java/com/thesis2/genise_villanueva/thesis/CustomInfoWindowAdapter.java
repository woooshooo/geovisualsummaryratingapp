package com.thesis2.genise_villanueva.thesis;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
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
    private ArrayList<Reviews> reviewsArrayList = new ArrayList<>();
    private ArrayList<Data> dataArrayList = new ArrayList<>();

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


        CustomListAdapter adapter = new CustomListAdapter(mContext, reviewsArrayList);

        /** Query reviews in Database.
         *
         * **/
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("review").orderByChild("location").equalTo(title);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reviewsArrayList.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot review : dataSnapshot.getChildren()) {
                        Reviews reviews = review.getValue(Reviews.class);
                        reviewsArrayList.add(reviews);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btn_review.setOnClickListener((View view1) -> {
//            Toast.makeText(mContext, "Showing reviews of " + marker.getTitle(), Toast.LENGTH_SHORT).show();
            AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
            dialog.setTitle(title);
            dialog.setCancelable(false);
            dialog.setAdapter(adapter, (dialogInterface, i) -> {
                Reviews user = (Reviews) adapter.getItem(i);
                AlertDialog.Builder builderInner = new AlertDialog.Builder(mContext);
                builderInner.setMessage(user.getReview() + "\n \n Reviewed by: "+ user.getUser());
                builderInner.setTitle(title);
                builderInner.setPositiveButton("Back", (dialogforexit, which) -> {
                    dialogforexit.dismiss();
                    dialog.show();
                });
                builderInner.show();
            });
            dialog.setNegativeButton("cancel", (dialog1, position) -> dialog1.dismiss());
            dialog.show();
        });

        btn_info.setOnClickListener((View view12) -> {
//            Toast.makeText(mContext, "Showing info of " + marker.getTitle(), Toast.LENGTH_SHORT).show();
            marker.hideInfoWindow();
            showDialogBox();
        });

    }

    @Override
    public View getInfoWindow(Marker marker) {
        renderWindowText(marker, mWindow);
        return mWindow;
    }

    private void showDialogBox() {
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setCancelable(false);
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


        /**Querying location infos from firebase**/
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("data").orderByChild("location").equalTo(title);
        query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataArrayList.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Data data = ds.getValue(Data.class);
                        dataArrayList.add(data);
                        assert data != null;
                        if (data.getLocation().equals(title)){
                            LocationInfo locationInfo = data.getLocationInfo();
                            String newRating;

                            if (Double.parseDouble(locationInfo.getRating()) > 5.1){
                                newRating = String.valueOf(Double.parseDouble(locationInfo.getRating()) / 2);
                            } else {
                                newRating = locationInfo.getRating();
                            }
                            msg.setText("Address: "+ locationInfo.getAddress()+ " \nWebsite: "+locationInfo.getWebsite()+"\nContact: "+locationInfo.getContactno()+"\nRating: "+newRating);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




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
//                    msg.setText("Address: "+ address+ " \nWebsite: "+website+"\nContact: "+contact+"\nRating: "+rating);
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

