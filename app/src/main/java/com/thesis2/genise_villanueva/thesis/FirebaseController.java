package com.thesis2.genise_villanueva.thesis;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import timber.log.Timber;

public class FirebaseController {
    private static final String TAG = "FirebaseController";
    private Context mContext;
    private Bundle mBundle;
    private List<Reviews> reviewsList;
    private AssetsController assetsController;
    private AssetManager assetManager;
    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    private FirebaseFirestore db;
    // [END declare_database_ref]
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
// ...

    FirebaseController(Context context, Bundle bundle) {
        this.mContext = context;
        this.mBundle = bundle;
        this.assetManager = mContext.getAssets();
        mAuth = FirebaseAuth.getInstance();
        assetsController = new AssetsController(context);

    }

    public void initializeDB() {

        // [START initialize_database_ref]
        if (mDatabase == null) {
            database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);
            mDatabase = database.getReference();
        }
        mDatabase.keepSynced(true);
        reviewsList = new ArrayList<>();

        // [END initialize_database_ref]
        // Access a Cloud Firestore instance from your Activity
    }

    public void viewData() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reviewsList.clear();
                for (DataSnapshot review : dataSnapshot.getChildren()) {
                    Reviews reviews = review.getValue(Reviews.class);
                    reviewsList.add(reviews);
                }

                AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                TextView customTitle = new TextView(mContext);

                customTitle.setText(reviewsList.get(0).review + " " + reviewsList.get(1).review);
                customTitle.setPadding(10, 10, 10, 10);   // Set Position
                customTitle.setGravity(Gravity.CENTER);
                customTitle.setTextColor(Color.WHITE);
                customTitle.setTextSize(20);
                alertDialog.setCustomTitle(customTitle);
                alertDialog.setCustomTitle(customTitle);
                alertDialog.show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(mContext, "Error: " + databaseError, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Importing data in Firebase
     **/
    public void writeDatastoFirebase() {
        JSONArray jsonArrayData;
        JSONObject jsonObjectData;
        JSONArray jsonArray;
        JSONObject jsonObject;
        try {
            jsonArrayData = new JSONArray(assetsController.loadDataFromAsset());
            jsonObjectData = new JSONObject();
            jsonArray = new JSONArray(assetsController.loadCoordinatesFromAsset());
            jsonObject = new JSONObject();
            for (int x = 0; x < jsonArrayData.length(); x++) {
                jsonObjectData = jsonArrayData.getJSONObject(x);
                jsonObject = jsonArray.getJSONObject(x);
                String location = jsonObjectData.getString("Name");
                int reviewCount = jsonObjectData.getInt("ReviewCount");
                double subjectivityScore = jsonObjectData.getDouble("SubjectivityScoreAverage");
                int positive = jsonObjectData.getInt("Positive");
                int positiveGTAvg = jsonObjectData.getInt("PositiveGTAvg");
                int positiveLTAvg = jsonObjectData.getInt("PositiveLTAvg");
                int negative = jsonObjectData.getInt("Negative");
                int negativeGTAvg = jsonObjectData.getInt("NegativeGTAvg");
                int negativeLTAvg = jsonObjectData.getInt("NegativeLTAvg");
                int neutral = jsonObjectData.getInt("Neutral");
                int neutralGTAvg = jsonObjectData.getInt("NeutralGTAvg");
                int neutralLTAvg = jsonObjectData.getInt("NeutralLTAvg");
                String address = jsonObjectData.getString("Address");
                String website = jsonObjectData.getString("Website");
                String contact = jsonObjectData.getString("Contact");
                String rating = jsonObjectData.getString("Rating");
                double lat = jsonObject.getDouble("Lat");
                double lng = jsonObject.getDouble("Lng");
                String data_id = mDatabase.child("data").push().getKey();
                Coordinates coordinates = new Coordinates(lat, lng);
                LocationInfo locationInfo = new LocationInfo(address, website, contact, rating);
                SentimentInfo sentimentInfo = new SentimentInfo(reviewCount, subjectivityScore, positive, positiveGTAvg, positiveLTAvg, negative, negativeGTAvg, negativeLTAvg, neutral, neutralGTAvg, neutralLTAvg);
                Data data = new Data(data_id,location,coordinates,locationInfo,sentimentInfo);
                assert data_id != null;
                mDatabase = database.getReference("data");
                mDatabase.child(data_id).setValue(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Timber.e("Data added: %s", data.getLocation().toString());
                                Toast.makeText(mContext, "Data added", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Timber.e(e, "Failure to add data: %s", data.toString());
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Importing reviews in Firebase
     **/
    public void writeReviewstoFirebase() {
        String[] files;
        String json;
        try {
            files = assetManager.list("reviews");
            for (String filename : files) {
                Log.d(TAG, "Assets folder file: %s: " + filename);
                JSONArray jsonArrayReview;
                JSONObject jsonObjectReview;
                try {
//                    InputStream inputStream = mContext.getAssets().open(filename);
                    InputStream inputStream = assetManager.open("reviews/" + filename);
                    int size = inputStream.available();
                    byte[] buffer = new byte[size];
                    inputStream.read(buffer);
                    inputStream.close();
                    json = new String(buffer, "UTF-8");
                    try {
                        jsonArrayReview = new JSONArray(json);
                        jsonObjectReview = new JSONObject();
                        for (int x = 0; x < jsonArrayReview.length(); x++) {
                            jsonObjectReview = jsonArrayReview.getJSONObject(x);
                            String user = jsonObjectReview.getString("User");
                            String[] output = filename.split(Pattern.quote("."));
                            String location = output[0];
                            String review = jsonObjectReview.getString("Review");
                            String review_id = mDatabase.child("review").push().getKey();
                            Reviews newReview = new Reviews(review_id, user, location, review);
                            assert review_id != null;
                            mDatabase.getDatabase().getReference("review").child(review_id).setValue(newReview)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Timber.e("Review added: %s", newReview.getLocation());
                                            Toast.makeText(mContext, "Reviews added", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Timber.e(e, "Failure to add review: %s", newReview.toString());
                                            Toast.makeText(mContext, "Failed to add", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException ex) {
                    Timber.e("loadReviewJSONFromAsset: %s", ex.getMessage());
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removing reviews in Firebase
     **/
    public void removeReviewsfromFirebase(){
        mDatabase.removeValue();
    }


}
