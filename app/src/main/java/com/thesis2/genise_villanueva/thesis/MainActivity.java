package com.thesis2.genise_villanueva.thesis;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, String.valueOf(R.string.access_token));
        setContentView(R.layout.activity_main);
        mapView = findViewById(R.id.mapView);
        TextView tvLocation = findViewById(R.id.tvLocation);
        TextView tvReviewCount = findViewById(R.id.tvReviewCount);
        TextView tvScore = findViewById(R.id.tvScore);
        TextView tvPositive = findViewById(R.id.tvPositive);
        TextView tvPositiveGT = findViewById(R.id.tvPositiveGT);
        TextView tvPositiveLT = findViewById(R.id.tvPositiveLT);
        TextView tvNegative = findViewById(R.id.tvNegative);
        TextView tvNegativeGT = findViewById(R.id.tvNegativeGT);
        TextView tvNegativeLT = findViewById(R.id.tvNegativeLT);
        TextView tvNeutral = findViewById(R.id.tvNeutral);
        TextView tvNeutralGT = findViewById(R.id.tvNeutralGT);
        TextView tvNeutralLT = findViewById(R.id.tvNeutralLT);

        mapView.onCreate(savedInstanceState);

        if (Mapbox.isConnected()) {
            Log.d(TAG, "onCreate: isConnected to Mapbox");
            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(MapboxMap mapboxMap) {
                    mapboxMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MainActivity.this));
                    // Customize map with markers, polylines, etc.
                    //Adding Markers
                    try {
                        JSONArray jsonArray;
                        JSONObject jsonObject = null;
                        jsonArray = new JSONArray(loadCoordinatesFromAsset());
                        jsonObject = new JSONObject();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.getJSONObject(i);
                            String loc = jsonObject.getString("Location");
                            double lat = jsonObject.getDouble("Lat");
                            double lng = jsonObject.getDouble("Lng");
                            LatLng newLatLng = new LatLng(lat, lng);
                            mapboxMap.addMarker(new MarkerOptions()
                                    .position(newLatLng)
                                    .title(loc)
                                    .snippet("Lat:" + lat + "\nLng:" + lng));
                        }
                        LatLng center = new LatLng(7.0910885, 125.6112563);
                        mapboxMap.animateCamera(CameraUpdateFactory.newLatLng(center), 1000);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(@NonNull Marker marker) {
                            mapboxMap.easeCamera(CameraUpdateFactory.newLatLng(marker.getPosition()), 500);

                            JSONArray jsonArrayData;
                            JSONObject jsonObjectData = null;
                            try {
                                jsonArrayData = new JSONArray(loadDataFromAsset());
                                jsonObjectData = new JSONObject();
                                for (int x = 0; x < jsonArrayData.length(); x++) {
                                    jsonObjectData = jsonArrayData.getJSONObject(x);
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

                                    if (location.equals(marker.getTitle())) {
                                        tvLocation.setText("Location: " + location);
                                        tvReviewCount.setText("Review Count: " + reviewCount);
                                        tvScore.setText("Average Subjectivity Score: " + subjectivityScore);
                                        tvPositive.setText("Positive Reviews: " + positive);
                                        tvPositiveGT.setText("Positive Reviews Highly Subjective: " + positiveGTAvg);
                                        tvPositiveLT.setText("Positive Reviews Highly Objective: " + positiveLTAvg);
                                        tvNegative.setText("Negative Reviews: " + negative);
                                        tvNegativeGT.setText("Negative Reviews Highly Subjective: " + negativeGTAvg);
                                        tvNegativeLT.setText("Negative Reviews Highly Objective: " + negativeLTAvg);
                                        tvNeutral.setText("Neutral Reviews: " + neutral);
                                        tvNeutralGT.setText("Neutral Reviews Highly Subjective: " + neutralGTAvg);
                                        tvNeutralLT.setText("Neutral Reviews Highly Objective: " + neutralLTAvg);
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            return false;
                        }
                    });
                }
            });
        } else {
            Log.e(TAG, "onCreate: !isConnect to Mapbox");
        }
    }

    //Turn Coordinates JSON to String from Assets folder
    public String loadCoordinatesFromAsset() {
        String json = null;
        try {
            InputStream inputStream = this.getAssets().open("coordinates.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            Log.e(TAG, "loadCoordinatesJSONFromAsset: " + ex.getMessage());
            return null;
        }
        return json;
    }
    //Turn Data JSON to String from Assets folder
    public String loadDataFromAsset() {
        String json = null;
        try {
            InputStream inputStream = this.getAssets().open("data.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            Log.e(TAG, "loadDataJSONFromAsset: " + ex.getMessage());
            return null;
        }
        return json;
    }


    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

}
