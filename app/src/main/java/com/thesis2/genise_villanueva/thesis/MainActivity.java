package com.thesis2.genise_villanueva.thesis;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
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
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private MapView mapView;
    private PieChart pieChart;
    private List<String> arrayString = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, String.valueOf(R.string.access_token));
        setContentView(R.layout.activity_main);
        ScrollView scrollView = findViewById(R.id.scrollViewLayout);
        pieChart = findViewById(R.id.pieChart);
        pieChart.setTransparentCircleRadius(10);
        pieChart.setUsePercentValues(true);
        pieChart.setCenterText("Percentage of Polarity");
        pieChart.setCenterTextSize(14);
        pieChart.setDrawCenterText(true);
        pieChart.setTransparentCircleRadius(50);
        pieChart.setCenterTextColor(Color.LTGRAY);
        pieChart.setEntryLabelColor(Color.LTGRAY);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setMinimumWidth(500);
        pieChart.setMinimumHeight(500);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(70);
        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(true);
        pieChart.animateXY(2000,2000, Easing.EasingOption.EaseInBack, Easing.EasingOption.EaseOutBack);
        mapView = findViewById(R.id.mapView);
        FloatingActionButton btn_center = findViewById(R.id.btn_center);
        TextView tvSubjectivity = findViewById(R.id.tvSubjectivity);
        ProgressBar subjectivityBar = findViewById(R.id.subjectivityBar);
        putEntryinList();
        mapView.onCreate(savedInstanceState);

        if (Mapbox.isConnected()) {
            Log.d(TAG, "onCreate: isConnected to Mapbox");
            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(MapboxMap mapboxMap) {
                    mapboxMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MainActivity.this));
                    // Customize map with markers, polylines, etc.
                    // Create an Icon object for the marker to use
                    IconFactory iconFactory = IconFactory.getInstance(MainActivity.this);
                    Icon icon = iconFactory.fromResource(R.mipmap.green_pin_marker);

                    // center button

                    btn_center.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            LatLng center = new LatLng(7.0910885, 125.6112563);
                            mapboxMap.animateCamera(CameraUpdateFactory.newLatLng(center), 1000);
                        }
                    });

                    //Adding Markers
                    try {
                        JSONArray jsonArray;
                        JSONObject jsonObject;
                        JSONArray jsonArrayData;
                        JSONObject jsonObjectData;
                        jsonArray = new JSONArray(loadCoordinatesFromAsset());
                        jsonObject = new JSONObject();
                        jsonArrayData = new JSONArray(loadDataFromAsset());
                        jsonObjectData = new JSONObject();
                        MarkerOptions markerOptions = new MarkerOptions();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.getJSONObject(i);
                            jsonObjectData = jsonArrayData.getJSONObject(i);
                            String loc = jsonObject.getString("Location");
                            String location = jsonObjectData.getString("Name");
                            if (loc.equals(location)){
                                Timber.d("loc is equal to location");
                                int positive = jsonObjectData.getInt("Positive");
                                int negative = jsonObjectData.getInt("Negative");
                                if (positive > negative) {
                                    markerOptions.icon(icon);
                                }
                            } else {
                                Timber.e("loc IS NOT equal to location");
                            }
                            markerOptions.title(loc);
                            double lat = jsonObject.getDouble("Lat");
                            double lng = jsonObject.getDouble("Lng");
                            LatLng newLatLng = new LatLng(lat, lng);
                            markerOptions.position(newLatLng);
                            mapboxMap.addMarker(markerOptions);

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

                            List<PieEntry> entries = new ArrayList<>();

                            JSONArray jsonArrayData;
                            JSONObject jsonObjectData;
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
                                        entries.add(new PieEntry(positive, "Positive"));
                                        entries.add(new PieEntry(neutral, "Neutral"));
                                        entries.add(new PieEntry(negative,"Negative"));
                                        int subjectivityScoreBar = (int) Math.round(subjectivityScore*100);
                                        subjectivityBar.setProgress(subjectivityScoreBar, true);
                                        tvSubjectivity.setText("Subjectivity: "+ subjectivityScoreBar +"%");
                                    }
                                    PieDataSet dataSet = new PieDataSet(entries,"Reviews");
                                    PieData data = new PieData(dataSet);
                                    Highlight h = new Highlight(0, 0,0); // dataset index for piechart is always 0
                                    pieChart.highlightValues(new Highlight[] { h });
                                    pieChart.getDescription().setEnabled(false);
                                    dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                                    dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                                    dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                                    data.setValueTextColor(Color.LTGRAY);
                                    dataSet.setValueLineColor(Color.LTGRAY);
                                    dataSet.setValueLinePart1OffsetPercentage(80.f);
                                    dataSet.setValueLinePart1Length(0.2f);
                                    dataSet.setValueLinePart2Length(0.4f);
                                    pieChart.getLegend().setTextColor(Color.LTGRAY);
                                    pieChart.setData(data);
                                    pieChart.invalidate();
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
        String json;
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

    private void putEntryinList() {
        JSONArray jsonArrayforString;
        JSONObject jsonObjectforString;
        try {
            jsonArrayforString = new JSONArray(loadDataFromAsset());
            jsonObjectforString = new JSONObject();
            for (int x = 0; x < jsonArrayforString.length(); x++) {
                jsonObjectforString = jsonArrayforString.getJSONObject(x);
                String location = jsonObjectforString.getString("Name");
                arrayString.add(location);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
